package gui.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class dividasController {

    @FXML
    private Button btnAdicionarDivida;

    @FXML
    private ChoiceBox<String> cbTipoDivida;

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
    private TextField txtValorPago;

    @FXML
    private TextField txtValorTotal;

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
