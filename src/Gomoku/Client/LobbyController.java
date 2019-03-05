package Gomoku.Client;

/**
 * @author dingweiran
 * @version 2019-3-4
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class LobbyController extends Application{
	@FXML
	private GridPane canvas;
	
	@FXML private ListView<Label> friendslist; @FXML private ListView<Label> historylist;
	
	@FXML
	private TextArea chatting;
	
	@FXML
	private TextField typing;
	
	@FXML
	private Button SendButton;
	
	@FXML private Circle table1;  @FXML private Circle table2;  @FXML private Circle table3;  @FXML private Circle table4;
	
	@FXML private Circle chair11; @FXML private Circle chair12; @FXML private Circle chair13; @FXML private Circle chair14;
	
	@FXML private Circle chair21; @FXML private Circle chair22; @FXML private Circle chair23; @FXML private Circle chair24;
	
	@FXML private Circle chair31; @FXML	private Circle chair32; @FXML private Circle chair33; @FXML private Circle chair34;
	
	@FXML private Circle chair41; @FXML private Circle chair42; @FXML private Circle chair44; @FXML private Circle chair43;
	
	//Add all the chairs to an array to simplify the code.
	private Circle[][] chairs = new Circle[4][4];
	
	private Circle[] tables = new Circle[4];
	
	//Switch to the game lobby after clent logged.
	public void showWindow() throws Exception {
		start(new Stage());
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
			Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Gomoku");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialize the layout and put all chairs to a two-dimension array for bulk operation.
	 */
	public void initialize() {
		chairs[0][0]=chair11; chairs[0][1]=chair12; chairs[0][2]=chair13; chairs[0][3]=chair14;
		chairs[1][0]=chair21; chairs[1][1]=chair22; chairs[1][2]=chair23; chairs[1][3]=chair24;
		chairs[2][0]=chair31; chairs[2][1]=chair32; chairs[2][2]=chair33; chairs[2][3]=chair34;
		chairs[3][0]=chair41; chairs[3][1]=chair42; chairs[3][2]=chair43; chairs[3][3]=chair44;
		tables[0]=table1; tables[1]=table2; tables[2]=table3; tables[3]=table4;
		GenerateFriendList();
		GenerateHistory();
	}
	
	
	/**
	 * Here is the method to handle event of clicking 16 chairs. After click an avaliable
	 * chair, the user will get in the game room.
	 * @param event
	 * @throws Exception 
	 */
	public void ChairEvent(MouseEvent event) throws Exception {
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				if(event.getSource() == chairs[i][j]) {		//find the chair that be clicked.
					if(!chairs[i][j].isDisable()) {			//check whether it is occupied
						ChairEventHelper(chairs[i][j]);
						return;
					}
				}
			}
		}
	}
	/**
	 * A helper method of ChairEvent(). It sends requests to server and update chair status.
	 * @throws Exception 
	 */
	public void ChairEventHelper(Circle chair) throws Exception {
		Stage stage = (Stage) canvas.getScene().getWindow();
		stage.close();
		
		PlayController board = new PlayController();
		board.showWindow();
		//these message should be sent to all clients.
//		chair.setFill(Color.GREY);
//		chair.setDisable(true);
	}
	
	
	/**
	 * The method to chat with all online user in the chatting room.
	 * @param event
	 */
	public void SendButtonClick(ActionEvent event) {
		if(typing.getText().length() > 0) {
			//  insert
			
			String message = typing.getText();
			typing.clear();
			chatting.appendText(message + "\n");
		}
	}
	
	
	/**
	 * Once user get in the game lobby, they will get access to their friends list
	 * and game history immediately. And these two method: GenerateFriendList(),
	 *  GenerateHistory() do this job.
	 */
	public void GenerateFriendList() {
		String online = "online";
		String username = "dingweiran";
		ObservableList<Label> list = FXCollections.observableArrayList();
		for(int i=0; i<20; i++) {
			//Read friend list from database.
			
			Label friend = new Label(username + " " + online);
			list.add(friend);
		}
		friendslist.setItems(list);
	}
	
	public void GenerateHistory() {
		String result = "Win";
		String date = "2017-8-12";
		ObservableList<Label> list = FXCollections.observableArrayList();
		for(int i=0; i<20; i++) {
			Label record = new Label(result + " " + date);
			list.add(record);
			//Recover the corresponding chess board when user click a record.
			
			record.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
				System.out.println("Reset!");
				record.setDisable(true);
			});
		}
		historylist.setItems(list);
	}
}
