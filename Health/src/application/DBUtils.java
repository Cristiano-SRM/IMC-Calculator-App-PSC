package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DBUtils {
	// metodo change scene
	public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String password) {
	    Parent root = null;

	    // Certifica de que o arquivo FXML não seja nulo antes de carregá-lo
	    if (fxmlFile != null) {
	        try {
	            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
	            root = loader.load();
	            
	            // Certifique-se de que o controlador da cena carregada seja do tipo LoggedInController
	            if (loader.getController() instanceof LoggedInController) {
	                LoggedInController loggedInController = loader.getController();
	                if (username != null) {
	                    loggedInController.setUserInformation1(username); // Passa o username para o controlador
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    // Certifica de que root não seja nulo antes de mudar a cena
	    if (root != null) {
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setTitle(title);
	        stage.setScene(new Scene(root, 600, 400));
	        stage.show();
	    }
	}


// metodo sign up 
	public static void signUpUser(ActionEvent event, String username, String password) {
		Connection connection = null;
		PreparedStatement psInsert = null;
		PreparedStatement psCheckUserExists = null;
		ResultSet resultSet = null;
		// pega as informações em signupcontroller e coloca no banco de dados sql
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthjavafx?useSSL=false&serverTimezone=UTC", "root", "Cris34769574!");
			psCheckUserExists = connection.prepareStatement( "SELECT * FROM users WHERE username = ?" );
			psCheckUserExists.setString(1, username);
			resultSet = psCheckUserExists.executeQuery();
			
			if (!resultSet.next()) { // Se o usuário não existir, cria o novo
			    psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
			    psInsert.setString(1, username);
			    psInsert.setString(2, password);
			    psInsert.executeUpdate();
			    changeScene(event, "logged-in.fxml", "Welcome", username, null);
			    // alerta caso o username ja exista
			} else {
			    System.out.println("User Already Exists!");
			    Alert alert = new Alert(Alert.AlertType.ERROR);
			    alert.setContentText("You cannot use this username.");
			    alert.show();
			}
// pega as excessoes e aponta qual foi o erro no codigo caso ocorra e aonde ocorreu
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psCheckUserExists != null) {
				try {
					psCheckUserExists.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (psInsert != null) {
				try {
					psInsert.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			}
			}
	/* metodo log in user, recebe usuario e senha de controller.java quando a pessoa for fazer login, verifica com os arquivos no banco de dados 
	 * confere e faz log in 
	*/
	public static void logInUser(ActionEvent event, String username, String password) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    
	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthjavafx", "root", "Cris34769574!");
	        preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
	        preparedStatement.setString(1, username);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            String retrievedPassword = resultSet.getString("password");
	            if (retrievedPassword.equals(password)) {
	                // Aqui, chamamos changeScene com username para atualizar a label na tela mostrando o nome do usuario em logged in 
	                changeScene(event, "logged-in.fxml", "Welcome!", username, null);
	                
	            } else {
	            	// alerta de senha errada
	                System.out.println("Password did not match");
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setContentText("The provided credentials are incorrect!");
	                alert.show();
	            }
	        } else {
	            System.out.println("User not found in database!");
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setContentText("Provided credentials are incorrect!");
	            alert.show();
	        }
	        // tratamento de excessoes 
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
			 if (resultSet  !=  null) {
				 try {
					 resultSet.close();
				 } catch (SQLException e) {
					 e.printStackTrace();
				 }
			 }
			 if (preparedStatement != null) {
				 try {
					 preparedStatement.close();
				 } catch (SQLException e) {
					 e.printStackTrace();
				 }
			 }
			 if (connection != null) {
				 try {
					 connection.close();
				 } catch (SQLException e) {
					 e.printStackTrace();
				 }
			 }
		 }
	    
	}
	// metodo que pega o ultimo IMC calculado do banco de dados e informa ao metodo em loggedincontroller
	public static double getLastIMC(String username) {
		// faz a coneção com a mysql workbench
	    Connection connection = null;
	    PreparedStatement psQuery = null;
	    ResultSet resultSet = null;
	    double lastIMC = 0.0;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthjavafx", "root", "Cris34769574!");
	        // Corrigir o nome da coluna para "lastIMC"
	        psQuery = connection.prepareStatement("SELECT lastIMC FROM users WHERE username = ?");
	        psQuery.setString(1, username);
	        resultSet = psQuery.executeQuery();

	        if (resultSet.next()) {
	            lastIMC = resultSet.getDouble("lastIMC");
	        }
	        // tratamento de excessoes
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (resultSet != null) {
	            try {
	                resultSet.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (psQuery != null) {
	            try {
	                psQuery.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return lastIMC; /* retorna o ultimo imc da database (caso exista) ao "getLastImc" metodo estatico
	    em setuserinformation1 */
	}


}
	


