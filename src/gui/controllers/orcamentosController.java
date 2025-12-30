package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Database;
import model.Orcamento;
import model.Categoria;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class orcamentosController {

    private Database db;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TableView<Orcamento> tblOrcamentos;

    @FXML
    private TableColumn<Orcamento, String> colCategoria;

    @FXML
    private TableColumn<Orcamento, Double> colValorLimite;

    @FXML
    private TableColumn<Orcamento, Double> colValorGasto;

    @FXML
    private TableColumn<Orcamento, String> colProgresso;

    @FXML
    private TableColumn<Orcamento, String> colPeriodo;

    @FXML
    private TableColumn<Orcamento, String> colAcoes;

    @FXML
    void initialize() {
        // Carrega o Database
        db = Database.carregar();

        // Configura as colunas da tabela
        configurarTabela();

        // Carrega os orçamentos
        atualizarTabela();
    }

    private void configurarTabela() {
        // Coluna Categoria
        colCategoria.setCellValueFactory(param -> {
            Orcamento orcamento = param.getValue();
            if (orcamento != null && orcamento.getCategoria() != null) {
                return new javafx.beans.property.SimpleStringProperty(orcamento.getCategoria().getNome());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Valor Limite - formata como moeda
        colValorLimite.setCellValueFactory(new PropertyValueFactory<>("valorLimite"));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "PT"));
        colValorLimite.setCellFactory(column -> new TableCell<Orcamento, Double>() {
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

        // Coluna Valor Gasto - formata como moeda
        colValorGasto.setCellValueFactory(new PropertyValueFactory<>("valorGasto"));
        colValorGasto.setCellFactory(column -> new TableCell<Orcamento, Double>() {
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

        // Coluna Progresso - mostra porcentagem e barra de progresso
        colProgresso.setCellValueFactory(param -> {
            Orcamento orcamento = param.getValue();
            if (orcamento != null) {
                double porcentagem = (orcamento.getValorGasto() / orcamento.getValorLimite()) * 100;
                if (porcentagem > 100) porcentagem = 100;
                return new javafx.beans.property.SimpleStringProperty(
                    String.format("%.1f%%", porcentagem)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("0%");
        });

        // Coluna Período - formata as datas
        colPeriodo.setCellValueFactory(param -> {
            Orcamento orcamento = param.getValue();
            if (orcamento != null && orcamento.getDataInicio() != null && orcamento.getDataFim() != null) {
                String inicio = orcamento.getDataInicio().format(dateFormatter);
                String fim = orcamento.getDataFim().format(dateFormatter);
                return new javafx.beans.property.SimpleStringProperty(inicio + " - " + fim);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Coluna Ações - botão de remover
        colAcoes.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty("Remover"));
        colAcoes.setCellFactory(column -> new TableCell<Orcamento, String>() {
            private final Button btnRemover = new Button("Remover");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnRemover.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                    btnRemover.setOnAction(event -> {
                        Orcamento orcamento = getTableView().getItems().get(getIndex());
                        removerOrcamento(orcamento);
                    });
                    setGraphic(btnRemover);
                }
            }
        });
    }

    private void atualizarTabela() {
        tblOrcamentos.getItems().clear();
        tblOrcamentos.getItems().addAll(db.getOrcamentos());
    }

    @FXML
    void adicionarOrcamento(javafx.event.ActionEvent event) {
        // Cria um diálogo para adicionar orçamento
        Dialog<Orcamento> dialog = new Dialog<>();
        dialog.setTitle("Novo Orçamento");
        dialog.setHeaderText("Adicionar um novo orçamento");

        // Cria os campos do formulário
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do orçamento");

        TextField txtValorLimite = new TextField();
        txtValorLimite.setPromptText("Valor limite (ex: 500.00)");

        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        for (Categoria categoria : db.getCategorias()) {
            cbCategoria.getItems().add(categoria.getNome());
        }

        // Layout do diálogo
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Valor Limite (€):"), 0, 1);
        grid.add(txtValorLimite, 1, 1);
        grid.add(new Label("Categoria:"), 0, 2);
        grid.add(cbCategoria, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Botões
        ButtonType btnAdicionar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAdicionar, btnCancelar);

        // Validação e processamento
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAdicionar) {
                try {
                    String nome = txtNome.getText().trim();
                    String valorLimiteStr = txtValorLimite.getText().trim();
                    String categoriaNome = cbCategoria.getSelectionModel().getSelectedItem();

                    if (nome.isEmpty()) {
                        mostrarErro("Digite o nome do orçamento.");
                        return null;
                    }

                    if (valorLimiteStr.isEmpty()) {
                        mostrarErro("Digite o valor limite do orçamento.");
                        return null;
                    }

                    if (categoriaNome == null || categoriaNome.isEmpty()) {
                        mostrarErro("Selecione uma categoria.");
                        return null;
                    }

                    double valorLimite;
                    try {
                        valorLimite = Double.parseDouble(valorLimiteStr.replace(",", "."));
                        if (valorLimite <= 0) {
                            mostrarErro("O valor limite deve ser maior que zero.");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        mostrarErro("Valor inválido. Use números (ex: 500.00).");
                        return null;
                    }

                    Categoria categoria = db.buscarCategoriaPorNome(categoriaNome);
                    if (categoria == null) {
                        mostrarErro("Categoria não encontrada.");
                        return null;
                    }

                    // Verifica se já existe um orçamento para esta categoria no mês atual
                    LocalDateTime agora = LocalDateTime.now();
                    for (Orcamento orcamentoExistente : db.getOrcamentos()) {
                        if (orcamentoExistente.getCategoria().equals(categoria)) {
                            LocalDateTime inicio = orcamentoExistente.getDataInicio();
                            LocalDateTime fim = orcamentoExistente.getDataFim();
                            if ((agora.isAfter(inicio) || agora.isEqual(inicio)) &&
                                (agora.isBefore(fim) || agora.isEqual(fim))) {
                                mostrarErro("Já existe um orçamento para esta categoria no mês atual.");
                                return null;
                            }
                        }
                    }

                    // Cria o orçamento
                    Orcamento orcamento = new Orcamento(nome, valorLimite, categoria);
                    db.criarOuAtualizarOrcamento(orcamento);
                    mostrarSucesso("Orçamento adicionado com sucesso!");
                    atualizarTabela();
                    return orcamento;
                } catch (Exception e) {
                    mostrarErro("Erro: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void removerOrcamento(Orcamento orcamento) {
        if (orcamento == null) {
            mostrarErro("Selecione um orçamento para remover.");
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja remover o orçamento '" + orcamento.getNome() + "'?");
        
        if (confirmacao.showAndWait().get().getButtonData().isCancelButton()) {
            return;
        }

        try {
            db.removerOrcamento(orcamento);
            mostrarSucesso("Orçamento removido com sucesso!");
            atualizarTabela();
        } catch (Exception e) {
            mostrarErro("Erro ao remover orçamento: " + e.getMessage());
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
        navegarPara("dividas.fxml", event);
    }

    @FXML
    void openOrcamentos(MouseEvent event) {
        // Já está na página de orçamentos
    }

    @FXML
    void openTransacoes(MouseEvent event) {
        navegarPara("transacoes.fxml", event);
    }
}
