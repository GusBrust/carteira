package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class adicionarController {

    @FXML
    private Button btnAdicionar;

    @FXML
    private ChoiceBox<String> cbCategoria;

    @FXML
    private ChoiceBox<String> cbMetodo;

    @FXML
    private ChoiceBox<String> cbTipo;

    @FXML
    private CheckBox chkFixa;

    @FXML
    private DatePicker dpData;

    @FXML
    private TextField txtValor;

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

}

