package application;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SignUpController implements Initializable {
    @FXML
    private Button button_signup;
    
    @FXML Button button_log_in;
    
    @FXML
    private TextField tf_username;
    
    @FXML
    private TextField tf_password;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// se clicar no botao sign up pega o usuario e senha e manda para o dbutils metodo estatico signup, que coloca na database
		button_signup.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
		    		DBUtils.signUpUser(event, tf_username.getText(), tf_password.getText());
		    	} else {
		    		System.out.println("Please fill in all the information.");
		    		Alert alert = new Alert(Alert.AlertType.ERROR);
		    		alert.setContentText("Please fill in all the information to sign up!");
		    		alert.show();
		    	}
		    }
		});
		// se clicar no botao log-in muda a cena para a pagina de log in 
		button_log_in.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBUtils.changeScene(event, "PaginaLoginInicio.fxml", "Log in!", tf_username.getText(), null);
				
			}
		     
		});
		
	}

}
