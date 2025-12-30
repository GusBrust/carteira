package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class adicionarController {

    private Database db;

    // Campos para Transação
    @FXML
    private ChoiceBox<String> cbTipo;
    @FXML
    private TextField txtValor;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private ChoiceBox<String> cbCategoria;
    @FXML
    private ChoiceBox<String> cbDividaTransacao;
    @FXML
    private DatePicker dpData;
    @FXML
    private Button btnAdicionarTransacao;


    @FXML
    void initialize() {
        // Carrega o Database
        db = Database.carregar();

        // Inicializa os campos de transação
        inicializarCamposTransacao();
        
        // Atualiza a lista de dívidas disponíveis
        atualizarListaDividas();
    }

    private void inicializarCamposTransacao() {
        // Popula o ChoiceBox de tipo de transação
        cbTipo.getItems().addAll("Receita", "Despesa", "Transferência");
        
        // Popula o ChoiceBox de categorias
        for (Categoria categoria : db.getCategorias()) {
            cbCategoria.getItems().add(categoria.getNome());
        }
        
        // Popula o ChoiceBox de dívidas (para vincular despesas)
        cbDividaTransacao.getItems().add("Nenhuma");
        for (Divida divida : db.getDividas()) {
            if (!divida.estaPaga()) {
                cbDividaTransacao.getItems().add(divida.getNomeEntidade());
            }
        }
        
        // Define a data padrão como hoje
        dpData.setValue(LocalDate.now());

        
        // Listener para mostrar/ocultar campos baseado no 
        cbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Transferência".equals(newVal)) {
                cbCategoria.setDisable(true);
                cbDividaTransacao.setDisable(true);
            } else {
                cbCategoria.setDisable(false);
                if ("Despesa".equals(newVal)) {
                    cbDividaTransacao.setDisable(false);
                } else {
                    cbDividaTransacao.setDisable(true);
                }
            }
        });
    }


    @FXML
    void adicionarTransacao() {
        try {
            // Validações
            if (cbTipo.getSelectionModel().isEmpty()) {
                mostrarErro("Selecione o tipo de transação.");
                return;
            }

            String tipo = cbTipo.getSelectionModel().getSelectedItem();
            String valorStr = txtValor.getText().trim();
            String descricao = txtDescricao.getText().trim();

            if (valorStr.isEmpty()) {
                mostrarErro("Digite o valor da transação.");
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorStr.replace(",", "."));
                if (valor <= 0) {
                    mostrarErro("O valor deve ser maior que zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido. Use números (ex: 100.50).");
                return;
            }

            if (descricao.isEmpty()) {
                descricao = "Sem descrição";
            }

            // Obtém a data
            LocalDate dataLocalDate = dpData.getValue();
            if (dataLocalDate == null) {
                dataLocalDate = LocalDate.now();
            }
            LocalDateTime data = dataLocalDate.atStartOfDay();

            // Obtém a conta
            Conta conta = db.getConta();
            if (conta == null) {
                mostrarErro("Conta não encontrada.");
                return;
            }

            // Cria a transação baseada no tipo
            Transacao transacao = null;
            Categoria categoria = null;

            if (!"Transferência".equals(tipo)) {
                String categoriaNome = cbCategoria.getSelectionModel().getSelectedItem();
                if (categoriaNome == null || categoriaNome.isEmpty()) {
                    mostrarErro("Selecione uma categoria.");
                    return;
                }
                categoria = db.buscarCategoriaPorNome(categoriaNome);
                if (categoria == null) {
                    mostrarErro("Categoria não encontrada.");
                    return;
                }
            }

            if ("Receita".equals(tipo)) {
                transacao = new Receita(valor, descricao, data, categoria, conta);
            } else if ("Despesa".equals(tipo)) {
                String dividaNome = cbDividaTransacao.getSelectionModel().getSelectedItem();
                String dividaId = null;
                if (dividaNome != null && !dividaNome.isEmpty() && !"Nenhuma".equals(dividaNome)) {
                    dividaId = db.getIdDividaPorNome(dividaNome);
                }
                if (dividaId != null && !dividaId.isEmpty()) {
                    transacao = new Despesa(valor, descricao, data, categoria, conta, dividaId);
                } else {
                    transacao = new Despesa(valor, descricao, data, categoria, conta);
                }
            } else if ("Transferência".equals(tipo)) {
                // Para transferência, precisamos de uma conta de destino
                // Como temos apenas uma conta, não podemos fazer transferência
                mostrarErro("Transferências não estão disponíveis com apenas uma conta.");
                return;
            }

            if (transacao == null) {
                mostrarErro("Erro ao criar a transação.");
                return;
            }

            // Processa a transação
            if (db.processarTransacao(transacao)) {
                mostrarSucesso("Transação adicionada com sucesso!");
                limparCamposTransacao();
            } else {
                mostrarErro("Erro ao processar a transação. Verifique se há saldo suficiente.");
            }

        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void limparCamposTransacao() {
        cbTipo.getSelectionModel().clearSelection();
        txtValor.clear();
        txtDescricao.clear();
        cbCategoria.getSelectionModel().clearSelection();
        cbDividaTransacao.getSelectionModel().select("Nenhuma");
        dpData.setValue(LocalDate.now());
    }

    private void atualizarListaDividas() {
        cbDividaTransacao.getItems().clear();
        cbDividaTransacao.getItems().add("Nenhuma");
        for (Divida divida : db.getDividas()) {
            if (!divida.estaPaga()) {
                cbDividaTransacao.getItems().add(divida.getNomeEntidade());
            }
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
        // Já está na página de adicionar
    }

    @FXML
    void openDashboard(MouseEvent event) {
        navegarPara("Interface.fxml", event);
    }

    @FXML
    void openDividas(MouseEvent event) {
        navegarPara("dividas.fxml", event);
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
