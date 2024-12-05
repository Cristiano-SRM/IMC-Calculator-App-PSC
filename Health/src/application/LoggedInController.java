package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoggedInController implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Label label_imc;
	
	@FXML
	private Label last_imc;
	
	@FXML
	private Button button_calculateIMC;
	
	@FXML
	private Label info_imc;
	
	@FXML
	private Label usernameLabel;
	
	@FXML
	private TextField tl_altura; 
	
	@FXML
	private TextField tl_peso;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    // caso button_logout seja clicado  sai da conta da pessoa
		button_logout.setOnAction(new EventHandler<ActionEvent>() {
		   @Override
		   public void handle(ActionEvent event) {
			   DBUtils.changeScene(event,"PaginaLoginInicio.fxml", "Log in!", null, null);
		   }
		});
		// caso button calculate imc seja apertado calcula o IMC e mostra na tela, junto com as informacoes
		button_calculateIMC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// chama o metodo que calculou o imc
                calcularIMC();
            }
        });
	}
	// metodo que calcula o imc
	private void calcularIMC() {
	    try {
	        // Obter os valores dos TextFields
	        double altura = Double.parseDouble(tl_altura.getText());
	        double peso = Double.parseDouble(tl_peso.getText());

	        // Verifique se os valores não são zero ou negativos
	        if (altura > 0 && peso > 0) {
	            // Calcular o IMC: IMC = peso / (altura * altura)
	            double imc = peso / (altura * altura);

	            // Atualizar a label com o resultado do IMC
	            label_imc.setText(String.format("%.2f", imc));
	            
	            // Chamar o método para atualizar a categoria de peso
                atualizarCategoriaIMC(imc);
       
	            // Salvar o IMC no banco de dados para o usuário logado
	            salvarIMCNoBanco(imc);  // Aqui você chama o método para salvar o IMC no banco
	        } else {
	            label_imc.setText("Altura e peso devem ser positivos!");
	        }
	    } catch (NumberFormatException e) {
	        label_imc.setText("Por favor, insira valores válidos.");
	    }
	}
	// mostra a label imc baseado no imc da pessoa
	 private void atualizarCategoriaIMC(double imc) {
	        if (imc < 18.5) {
	            info_imc.setText("Magreza");
	        } else if (imc >= 18.5 && imc < 24.9) {
	            info_imc.setText("Peso normal");
	        } else if (imc >= 25 && imc < 29.9) {
	            info_imc.setText("Sobrepeso");
	        } else if (imc >= 30.0 && imc < 39.9) {
	            info_imc.setText("Obesidade");
	        } else {
	        	info_imc.setText("Muito obeso");
	        }
	    }

        private void salvarIMCNoBanco(double imc) {
    	    Connection connection = null;
    	    PreparedStatement psUpdate = null;
    	    
    	    // Supondo que você tenha o nome do usuário armazenado em algum lugar
    	    String username = usernameLabel.getText().replace("!", "").trim();  // Ajuste conforme o seu caso

    	    try {
    	        // Conectar ao banco de dados
    	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthjavafx", "root", "Cris34769574!");

    	        // Atualizar o IMC na tabela do usuário (assumindo que a tabela tem uma coluna 'last_imc')
    	        String query = "UPDATE users SET lastIMC = ? WHERE username = ?";
    	        psUpdate = connection.prepareStatement(query);
    	        psUpdate.setDouble(1, imc);  // Definir o IMC calculado
    	        psUpdate.setString(2, username);  // Definir o nome do usuário
    	        psUpdate.executeUpdate();  // Executar a atualização
// tratamento de excessoes
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    } finally {
    	        if (psUpdate != null) {
    	            try {
    	                psUpdate.close();
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
	// escreve o nome de usuario na pagina logged-in
    public void setUserInformation(String username) {
	   usernameLabel.setText(username+"!");
   
	}
    @FXML
    // hyperlink abre 1 site que explica indice de massa corporal
    public void abrirLink(ActionEvent event) {
        try {
            // Abre o link no navegador padrão
            Desktop.getDesktop().browse(new URI("https://www.sistemasca.com/blog/160/o-que-e-imc-entenda-a-importancia-desse-protocolo-na-avaliacao-fisica?utm_source=google&utm_medium=pago&utm_campaign=Site-Vend-Max-Aca-1&gad_source=1&gclid=CjwKCAiAxqC6BhBcEiwAlXp450nTkmQ6B-3uhC2Qrorppmm_P1TgGnovaiCkR_yVZ3fPk-Gmq3Uj1xoCYI0QAvD_BwE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUserInformation1(String username) {
        // Garantir que o nome do usuário está corretamente exibido
        usernameLabel.setText(username + "!");
        
        // Recuperar o IMC anterior do banco de dados usando o username já configurado na label
        String usernameFromLabel = usernameLabel.getText().replace("!", "").trim();  // Pega o nome do usuário da label

        // Recuperar o IMC do banco de dados
        double imcAnterior = DBUtils.getLastIMC(usernameFromLabel);
        
        // Adicionar um print para depuração
        System.out.println("IMC anterior: " + imcAnterior);

        // Atualizar a label com o IMC anterior
        if (imcAnterior != 0.0) {
            last_imc.setText(String.format("%.2f", imcAnterior));
        } else {
            last_imc.setText("");
        }
    }




	}

