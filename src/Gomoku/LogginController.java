package Gomoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LogginController {
	@FXML
	private AnchorPane Pane;
	@FXML
	private ImageView icon;
	@FXML
	private TextField UsernameText;
	@FXML
	private PasswordField PasswordText;
	@FXML
	private Button Loggin;
	@FXML
	private Button Register;

	private Client client;
    
    
	@FXML
	public void LogginButtonclick(ActionEvent event) throws Exception{
		String username = UsernameText.getText();
		String password = PasswordText.getText();
		client.login(username, password);
	}

	@FXML
	public void RegisterButtonclick(ActionEvent event) throws Exception{
		String username = UsernameText.getText();
		String password = PasswordText.getText();
		System.out.println(username + "\n" + password);
		client.register(username, password);
	}

	public void init(){
		if(this.client==null)
			this.client = new Client();
	}

	public void initialize() {
		init();
		System.out.println("initialize");
	}
	
}
