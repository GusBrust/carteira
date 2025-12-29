package gui.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;



public class transacoesController {

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
    private Label lblTotalDespesas;

    @FXML
    private Label lblTotalReceitas;

    @FXML
    private TableView<Transacao> tblTransacoes;

    @FXML
    void openAdicionar(MouseEvent event) {

    }

    @FXML
    void openDashboard(MouseEvent event) {

    }

    @FXML
    void openDividas(MouseEvent event) {

    }

    @FXML
    void openTransacoes(MouseEvent event) {

    }

    @FXML
    void openOrcamentos(MouseEvent event) {

    }

}
