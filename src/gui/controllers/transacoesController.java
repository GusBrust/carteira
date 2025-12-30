package gui.controllers;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Transacao;
import model.Database;
import model.Despesa;
import model.Receita;
import model.Transferencia;

/**
 * Controller da interface de visualização de transações.
 * Gerencia a exibição e remoção de transações.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class transacoesController {
    private Database db;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        db = Database.carregar();
        configurarColunas();
        atualizarTabela();
        atualizarTotalDespesas();
        atualizarTotalReceitas();
    }

    private void configurarColunas() {
        // Coluna Data - formata LocalDateTime para String
        colData.setCellValueFactory(param -> {
            Transacao transacao = param.getValue();
            if (transacao != null && transacao.getData() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    transacao.getData().format(dateFormatter)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Tipo - mostra o tipo da transação (Despesa, Receita, Transferência)
        colTipo.setCellValueFactory(param -> {
            Transacao transacao = param.getValue();
            if (transacao instanceof Despesa) {
                return new javafx.beans.property.SimpleStringProperty("Despesa");
            } else if (transacao instanceof Receita) {
                return new javafx.beans.property.SimpleStringProperty("Receita");
            } else if (transacao instanceof Transferencia) {
                return new javafx.beans.property.SimpleStringProperty("Transferência");
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Categoria - mostra o nome da categoria ou "N/A"
        colCategoria.setCellValueFactory(param -> {
            Transacao transacao = param.getValue();
            if (transacao.getCategoria() != null) {
                return new javafx.beans.property.SimpleStringProperty(transacao.getCategoria().getNome());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Método - mostra o nome da conta
        colMetodo.setCellValueFactory(param -> {
            Transacao transacao = param.getValue();
            if (transacao.getConta() != null) {
                return new javafx.beans.property.SimpleStringProperty(transacao.getConta().getNome());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Valor - formata o valor com 2 casas decimais
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValor.setCellFactory(column -> new TableCell<Transacao, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String valorFormatado = String.format("%.2f", item).replace(".", ",");
                    setText(valorFormatado + "€");
                }
            }
        });

        // Coluna Despesa Fixa - mostra "Sim" ou "Não" baseado na informação da despesa
        colDespesaFixa.setCellValueFactory(param -> {
            Transacao transacao = param.getValue();
            if (transacao instanceof Despesa) {
                Despesa despesa = (Despesa) transacao;
                return new javafx.beans.property.SimpleStringProperty(despesa.isDespesaFixa() ? "Sim" : "Não");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Não");
            }
        });
    }

    private void atualizarTabela() {
        tblTransacoes.getItems().clear();
        tblTransacoes.getItems().addAll(db.getTransacoes());
    }

    private void atualizarTotalDespesas() {
        double totalDespesas = 0;
        for (Transacao transacao : db.getTransacoes()) {
            if (transacao instanceof Despesa) {
                totalDespesas += transacao.getValor();
            }
        }
        String totalFormatado = String.format("%.2f", totalDespesas).replace(".", ",");
        lblTotalDespesas.setText(totalFormatado + "€");
    }

    private void atualizarTotalReceitas() {
        double totalReceitas = 0;
        for (Transacao transacao : db.getTransacoes()) {
            if (transacao instanceof Receita) {
                totalReceitas += transacao.getValor();
            }
        }
        String totalFormatado = String.format("%.2f", totalReceitas).replace(".", ",");
        lblTotalReceitas.setText(totalFormatado + "€");
    }

    @FXML
    void removerTransacaoSelecionada(ActionEvent event) {
        Transacao transacaoSelecionada = tblTransacoes.getSelectionModel().getSelectedItem();
        
        if (transacaoSelecionada == null) {
            mostrarErro("Selecione uma transação para remover.");
            return;
        }

        removerTransacao(transacaoSelecionada);
    }

    private void removerTransacao(Transacao transacao) {
        if (transacao == null) {
            mostrarErro("Selecione uma transação para remover.");
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.setHeaderText(null);
        
        String tipoTransacao = "";
        if (transacao instanceof Despesa) {
            tipoTransacao = "Despesa";
        } else if (transacao instanceof Receita) {
            tipoTransacao = "Receita";
        } else if (transacao instanceof Transferencia) {
            tipoTransacao = "Transferência";
        }
        
        confirmacao.setContentText("Tem certeza que deseja remover esta " + tipoTransacao + 
            " de " + String.format("%.2f", transacao.getValor()).replace(".", ",") + "€?");
        
        if (confirmacao.showAndWait().get().getButtonData().isCancelButton()) {
            return;
        }

        try {
            // Reverte a transação antes de remover (para atualizar o saldo)
            if (transacao.reverter()) {
                db.removerTransacao(transacao);
                mostrarSucesso("Transação removida com sucesso!");
                atualizarTabela();
                atualizarTotalDespesas();
                atualizarTotalReceitas();
            } else {
                mostrarErro("Erro ao reverter a transação. Não foi possível remover.");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao remover transação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private TableColumn<Transacao, String> colCategoria;

    @FXML
    private TableColumn<Transacao, String> colData;

    @FXML
    private TableColumn<Transacao, String> colDespesaFixa;

    @FXML
    private TableColumn<Transacao, String> colMetodo;

    @FXML
    private TableColumn<Transacao, String> colTipo;

    @FXML
    private TableColumn<Transacao, Double> colValor;

    @FXML
    private Button btnRemoverTransacao;

    @FXML
    private Label lblTotalDespesas;

    @FXML
    private Label lblTotalReceitas;

    @FXML
    private TableView<Transacao> tblTransacoes;

    /**
     * Método auxiliar para navegar entre telas
     */
    private void navegarPara(String fxmlFile, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/views/" + fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();
            
            // Mantém o tamanho atual da janela
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();
            
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/gui/views/application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openDashboard(MouseEvent event) {
        navegarPara("Interface.fxml", event);
    }
    
    @FXML
    void openTransacoes(MouseEvent event) {
        // Já está na página de transações
    }
    
    @FXML
    void openAdicionar(MouseEvent event) {
        navegarPara("adicionar.fxml", event);
    }
    
    @FXML
    void openOrcamentos(MouseEvent event) {
        navegarPara("orcamentos.fxml", event);
    }
    
    @FXML
    void openDividas(MouseEvent event) {
        navegarPara("dividas.fxml", event);
    }

}
