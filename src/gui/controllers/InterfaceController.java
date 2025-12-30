package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Database;
import model.Conta;
import model.Categoria;


public class InterfaceController {
    private Database db;
    
    @FXML
    private Label lblSaldo;
    
    @FXML
    private Label lblNomeConta;
    
    @FXML
    private Button btnEditarNome;
    
    @FXML
    private PieChart pieChartResumo;
    
    @FXML
    public void initialize() {
        // Carregar Database
        db = Database.carregar();
        atualizarInterface();
    }
    
    private void atualizarInterface() {
        // Obter conta única
        Conta conta = db.getConta();
        if (conta == null) {
            conta = new Conta("Conta Principal", 0.0);
            db.setConta(conta);
        }
        
        // Atualizar nome da conta no label
        lblNomeConta.setText(conta.getNome());
        
        // Atualizar saldo
        double saldoTotal = conta.getSaldo();
        String saldoFormatado = String.format("%.2f", saldoTotal).replace(".", ",");
        lblSaldo.setText(saldoFormatado + "€");
        
        // Atualizar gráfico com categorias
        atualizarGrafico();
    }
    
    @FXML
    void editarNomeConta(javafx.event.ActionEvent event) {
        Conta conta = db.getConta();
        if (conta == null) {
            return;
        }
        
        // Criar diálogo para editar o nome
        TextInputDialog dialog = new TextInputDialog(conta.getNome());
        dialog.setTitle("Editar Nome da Conta");
        dialog.setHeaderText("Alterar nome da conta");
        dialog.setContentText("Digite o novo nome da conta:");
        
        // Mostrar diálogo e processar resultado
        dialog.showAndWait().ifPresent(novoNome -> {
            String nomeTrimmed = novoNome.trim();
            if (nomeTrimmed != null && !nomeTrimmed.isEmpty()) {
                if (!nomeTrimmed.equals(conta.getNome())) {
                    conta.setNome(nomeTrimmed);
                    db.atualizarConta();
                    // Atualizar a interface
                    atualizarInterface();
                }
            } else {
                // Mostrar alerta se o nome estiver vazio
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText("Nome inválido");
                alert.setContentText("O nome da conta não pode estar vazio.");
                alert.showAndWait();
            }
        });
    }
    
    private void atualizarGrafico() {
        // Limpar dados anteriores
        pieChartResumo.getData().clear();
        
        // Adicionar apenas categorias que têm despesas
        for (Categoria categoria : db.getCategorias()) {
            double valor = db.getValorCategoria(categoria);
            if (valor > 0) {
                // Criar entrada no gráfico com nome da categoria e valor
                PieChart.Data data = new PieChart.Data(categoria.getNome(), valor);
                pieChartResumo.getData().add(data);
            }
        }
        
        // Se não houver dados, adicionar uma mensagem
        if (pieChartResumo.getData().isEmpty()) {
            PieChart.Data semDados = new PieChart.Data("Sem despesas", 1);
            pieChartResumo.getData().add(semDados);
        }
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
    void openDashboard(MouseEvent event) {
        // Recarregar dados ao voltar ao dashboard
        db = Database.carregar();
        atualizarInterface();
    }
    
    @FXML
    void openTransacoes(MouseEvent event) {
        navegarPara("transacoes.fxml", event);
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
