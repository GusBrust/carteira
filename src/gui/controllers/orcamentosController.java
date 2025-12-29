package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class orcamentosController {

    @FXML
    private TableView<?> tblOrcamentos;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colValorLimite;

    @FXML
    private TableColumn<?, ?> colValorGasto;

    @FXML
    private TableColumn<?, ?> colProgresso;

    @FXML
    private TableColumn<?, ?> colPeriodo;

    @FXML
    private TableColumn<?, ?> colAcoes;

    @FXML
    void adicionarOrcamento(javafx.event.ActionEvent event) {
        // TODO: Implementar diálogo para adicionar novo orçamento
    }

    @FXML
    void openAdicionar(MouseEvent event) {
        // TODO: Implementar navegação para página de adicionar
    }

    @FXML
    void openDashboard(MouseEvent event) {
        // TODO: Implementar navegação para dashboard
    }

    @FXML
    void openDividas(MouseEvent event) {
        // TODO: Implementar navegação para dívidas
    }

    @FXML
    void openOrcamentos(MouseEvent event) {
        // Já está na página de orçamentos
    }

    @FXML
    void openTransacoes(MouseEvent event) {
        // TODO: Implementar navegação para transações
    }

    @FXML
    void initialize() {
        // Configurar colunas da tabela
        // TODO: Implementar binding com dados do Database
    }
}

