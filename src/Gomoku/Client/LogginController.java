package Gomoku.Client;
/**
 * This is the controller of loggin interface. Containing some operations to
 * action event.
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
	
	/**
	 * Action caused by clicking Register button.
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void RegisterButtonClick(ActionEvent event) throws Exception {
		//init();
		String username = UsernameText.getText();
		String password = PasswordText.getText();
		System.out.println(username + "\n" + password);
		//client.register(username, password);
		ErrorWindow("Username already exist!");

	}
	
	
    /**
     * Action caused by clicking Loggin button. Transfer the Username and Password 
     * to the server and check whether they exist in database and match. If it does,
     * turn to 
     * @param event
     * @throws Exception
     */
	@FXML
	public void LogginButtonclick(ActionEvent event) throws Exception {
		//init();
		String username = UsernameText.getText();
		String password = PasswordText.getText();
		if(client.login(username, password))
			changeWindow();
	}
	
	
	/**
	 * After logged in, turn to the game lobby and close loggin window.
	 * @throws Exception
	 */
	public void changeWindow() throws Exception {
		Stage stage = (Stage) Pane.getScene().getWindow();
	    stage.close();
	    LobbyController lobby = new LobbyController();
		lobby.showWindow();
	}
	
	
	/**
	 * When register or loggin fail, say, the username that user what to register is
	 * already taken or user type in wrong password, this method will give a small window
	 * to tell user what is going on. It will also tell user when register succeed.
	 * @param errormessage message about what is wrong.
	 */
	public void ErrorWindow(String message) {
		Group root = new Group();
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(12);
		
		Label label = new Label(message);
		label.setFont(new Font(18));
		Button button = new Button("Ok");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.close();
		});

		ColumnConstraints column = new ColumnConstraints(300);
		column.setHalignment(HPos.CENTER);
		pane.getColumnConstraints().add(column);
		
		pane.add(label, 0, 1);
		pane.add(button, 0, 3);
		root.getChildren().add(pane);
		
		Scene scene = new Scene(root,300,100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * When client is not created, create one.
	 */
	public void initialize(){
		if(client == null)
			this.client = new Client();
		System.out.println("Initialised");
	}
	
}
