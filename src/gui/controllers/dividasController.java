package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Database;
import model.Divida;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Controller da interface de gerenciamento de dívidas.
 * Gerencia a criação, edição e remoção de dívidas.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class dividasController {

    private Database db;

    @FXML
    private Button btnAdicionarDivida;

    @FXML
    private Button btnRemoverDivida;

    @FXML
    private TableColumn<Divida, Double> colEmFalta;

    @FXML
    private TableColumn<Divida, String> colEntidade;

    @FXML
    private TableColumn<Divida, Double> colPago;

    @FXML
    private TableColumn<Divida, String> colTipo;

    @FXML
    private TableColumn<Divida, Double> colTotal;

    @FXML
    private TableView<Divida> tblDividas;

    @FXML
    private TextField txtEntidade;

    @FXML
    private TextField txtDescricaoDivida;

    @FXML
    private TextField txtValorTotal;

    @FXML
    private TextField txtValorPago;

    @FXML
    void initialize() {
        // Carrega o Database
        db = Database.carregar();

        // Configura as colunas da tabela
        configurarTabela();

        // Carrega as dívidas
        atualizarTabela();
    }

    private void configurarTabela() {
        // Configura as colunas para usar PropertyValueFactory
        colEntidade.setCellValueFactory(new PropertyValueFactory<>("NomeEntidade"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("Descricao"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPago.setCellValueFactory(new PropertyValueFactory<>("pago"));
        colEmFalta.setCellValueFactory(new PropertyValueFactory<>("emFalta"));

        // Formata os valores monetários
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "PT"));

        colTotal.setCellFactory(column -> new javafx.scene.control.TableCell<Divida, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        colPago.setCellFactory(column -> new javafx.scene.control.TableCell<Divida, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        colEmFalta.setCellFactory(column -> new javafx.scene.control.TableCell<Divida, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });
    }

    private void atualizarTabela() {
        tblDividas.getItems().clear();
        tblDividas.getItems().addAll(db.getDividas());
    }

    @FXML
    void adicionarDivida() {
        try {
            String entidade = txtEntidade.getText().trim();
            String descricao = txtDescricaoDivida.getText().trim();
            String totalStr = txtValorTotal.getText().trim();

            if (entidade.isEmpty()) {
                mostrarErro("Digite o nome da entidade.");
                return;
            }

            // Se a descrição estiver vazia, define como null
            if (descricao.isEmpty()) {
                descricao = null;
            }

            if (totalStr.isEmpty()) {
                mostrarErro("Digite o valor total da dívida.");
                return;
            }

            double total;
            try {
                total = Double.parseDouble(totalStr.replace(",", "."));
                if (total <= 0) {
                    mostrarErro("O valor total deve ser maior que zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido. Use números (ex: 1000.50).");
                return;
            }

            // Cria e adiciona a dívida
            Divida divida = new Divida(entidade, descricao, total);
            db.adicionarDivida(divida);

            mostrarSucesso("Dívida adicionada com sucesso!");
            limparCampos();
            atualizarTabela();

        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void removerDivida() {
        Divida dividaSelecionada = tblDividas.getSelectionModel().getSelectedItem();

        if (dividaSelecionada == null) {
            mostrarErro("Selecione uma dívida para remover.");
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja remover a dívida de " + dividaSelecionada.getNomeEntidade() + "?");
        
        if (confirmacao.showAndWait().get().getButtonData().isCancelButton()) {
            return;
        }

        try {
            db.removerDivida(dividaSelecionada);
            mostrarSucesso("Dívida removida com sucesso!");
            atualizarTabela();
        } catch (Exception e) {
            mostrarErro("Erro ao remover dívida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtEntidade.clear();
        txtDescricaoDivida.clear();
        txtValorTotal.clear();
        txtValorPago.clear();
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
    void openAdicionar(MouseEvent event) {
        navegarPara("adicionar.fxml", event);
    }

    @FXML
    void openDashboard(MouseEvent event) {
        navegarPara("Interface.fxml", event);
    }

    @FXML
    void openDividas(MouseEvent event) {
        // Já está na página de dívidas
    }

    @FXML
    void openTransacoes(MouseEvent event) {
        navegarPara("transacoes.fxml", event);
    }

    @FXML
    void openOrcamentos(MouseEvent event) {
        navegarPara("orcamentos.fxml", event);
    }
}
