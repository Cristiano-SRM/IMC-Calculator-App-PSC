package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage Stage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("PaginaLoginInicio.fxml"));
		Stage.setTitle("Log In!");
		Stage.setScene(new Scene(root, 600, 400));
		Stage.show();
		// classe main que seta o stage e a scene da pagina do começo, inicializando a GUI
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
