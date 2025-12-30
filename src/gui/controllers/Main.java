package gui.controllers;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * Classe principal da aplicação JavaFX.
 * Inicializa e exibe a interface gráfica do sistema de gerenciamento financeiro.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Main extends Application {
	
	/**
	 * Inicializa e exibe a janela principal da aplicação.
	 * 
	 * @param primaryStage Palco principal da aplicação
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/gui/views/Interface.fxml"));
			Scene scene = new Scene(root,881,665);
			scene.getStylesheets().add(getClass().getResource("/gui/views/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método principal que inicia a aplicação JavaFX.
	 * 
	 * @param args Argumentos da linha de comando
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
