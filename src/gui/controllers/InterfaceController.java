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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.application.Platform;
import model.Database;
import model.Conta;
import model.Categoria;
import model.Transacao;
import model.Despesa;
import model.Receita;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


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
    private BarChart<String, Number> barChartMensal;
    
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
        
        // Atualizar gráficos
        atualizarGrafico();
        atualizarGraficoBarras();
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
    
    private void atualizarGraficoBarras() {
        // Limpar dados anteriores
        barChartMensal.getData().clear();
        
        // Agrupar transações por mês
        Map<String, Double> despesasPorMes = new HashMap<>();
        Map<String, Double> receitasPorMes = new HashMap<>();
        
        DateTimeFormatter mesFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        
        for (Transacao transacao : db.getTransacoes()) {
            LocalDateTime data = transacao.getData();
            String mesAno = data.format(mesFormatter);
            
            if (transacao instanceof Despesa) {
                despesasPorMes.put(mesAno, despesasPorMes.getOrDefault(mesAno, 0.0) + transacao.getValor());
            } else if (transacao instanceof Receita) {
                receitasPorMes.put(mesAno, receitasPorMes.getOrDefault(mesAno, 0.0) + transacao.getValor());
            }
        }
        
        // Calcular totais
        double totalDespesas = 0.0;
        for (Double valor : despesasPorMes.values()) {
            totalDespesas += valor;
        }
        double totalReceitas = 0.0;
        for (Double valor : receitasPorMes.values()) {
            totalReceitas += valor;
        }
        
        // Criar séries de dados com totais na legenda
        XYChart.Series<String, Number> serieDespesas = new XYChart.Series<>();
        serieDespesas.setName(String.format("Despesas").replace(".", ","));
        
        XYChart.Series<String, Number> serieReceitas = new XYChart.Series<>();
        serieReceitas.setName(String.format("Receitas").replace(".", ","));
        
        // Coletar todos os meses únicos
        Map<String, Boolean> todosMeses = new HashMap<>();
        for (String mes : despesasPorMes.keySet()) {
            todosMeses.put(mes, true);
        }
        for (String mes : receitasPorMes.keySet()) {
            todosMeses.put(mes, true);
        }
        
        // Adicionar dados para cada mês (garantindo que ambos tenham valores)
        for (String mes : todosMeses.keySet()) {
            double despesas = despesasPorMes.getOrDefault(mes, 0.0);
            double receitas = receitasPorMes.getOrDefault(mes, 0.0);
            
            XYChart.Data<String, Number> dataDespesas = new XYChart.Data<>(mes, despesas);
            XYChart.Data<String, Number> dataReceitas = new XYChart.Data<>(mes, receitas);
            
            // Adicionar labels com valores dentro das barras
            if (despesas > 0) {
                javafx.scene.control.Label labelDespesas = new javafx.scene.control.Label(String.format("%.2f€", despesas).replace(".", ","));
                labelDespesas.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
                StackPane stackDespesas = new StackPane();
                stackDespesas.getChildren().add(labelDespesas);
                dataDespesas.setNode(stackDespesas);
            }
            
            if (receitas > 0) {
                javafx.scene.control.Label labelReceitas = new javafx.scene.control.Label(String.format("%.2f€", receitas).replace(".", ","));
                labelReceitas.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
                StackPane stackReceitas = new StackPane();
                stackReceitas.getChildren().add(labelReceitas);
                dataReceitas.setNode(stackReceitas);
            }
            
            serieDespesas.getData().add(dataDespesas);
            serieReceitas.getData().add(dataReceitas);
        }
        
        // Adicionar séries ao gráfico
        if (!serieDespesas.getData().isEmpty() || !serieReceitas.getData().isEmpty()) {
            barChartMensal.getData().add(serieDespesas);
            barChartMensal.getData().add(serieReceitas);
        }
        
        // Esconder completamente os eixos e suas marcas
        CategoryAxis xAxis = (CategoryAxis) barChartMensal.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChartMensal.getYAxis();
        
        xAxis.setVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        
        yAxis.setVisible(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setTickMarkVisible(false);
        
        // Configurar para não mostrar eixos e desabilitar animação
        barChartMensal.setAnimated(false);
        barChartMensal.setLegendVisible(true);
        
        // Configurar estilo inline apenas para propriedades simples
        barChartMensal.setStyle("-fx-background-color: transparent;");
        
        // Aplicar cores das barras programaticamente após o gráfico ser renderizado
        Platform.runLater(() -> {
            barChartMensal.lookupAll(".default-color0.chart-bar").forEach(node -> 
                node.setStyle("-fx-bar-fill: #f44336;")
            );
            barChartMensal.lookupAll(".default-color1.chart-bar").forEach(node -> 
                node.setStyle("-fx-bar-fill: #4CAF50;")
            );
        });
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
