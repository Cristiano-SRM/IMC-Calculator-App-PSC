package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
	
	@FXML
	private Button button_login;
	
	@FXML
	private Button button_sign_up;
	
	@FXML
	private TextField tf_username;
	
	@FXML
	private TextField tf_password;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* cao clica no butao login muda a scene */
		button_login.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
                 DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());			}
			
		});
		/* caso clique no botao sign up muda a scene */
		button_sign_up.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBUtils.changeScene(event, "sign-up.fxml", "Sign Up!", null, null);
				
			}
			
		});
	}
	 

}
