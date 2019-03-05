package Gomoku.Client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayController extends Application{

	@FXML
	Canvas canvas;
	@FXML
	Label label_play01, label_play02, label_play03, label_name01, label_name02, label_name03, label_name04;
	@FXML
	Button back, play, enter;
	@FXML
	TextArea chat_Lobby;
	@FXML
	TextField chat_Input;

	private GraphicsContext gc;
	private static final int BOARD_WIDTH = 450;
	private static final int BOARD_GAP = 25;
	private static final int CANVAS_GAP = 25;
	private static final int ROWS = 18;
	private static final int CHESS_SIZE = 20;
	private Color COLOR_MARK = Color.valueOf("#FF7F27");
	private Color COLOR_BOARD = Color.valueOf("#FBE39B");
	private Color COLOR_LINE = Color.valueOf("#884B09");
	private String[] MARKX = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S" };
	private String[] MARKY = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19" };
	
	private Client client;
	
	private enum Chess {
		BLACK, WHITE, RED, BLUE
	}

	private Chess currentChess = Chess.BLACK;
	private Chess[][] game = new Chess[18][18];
	private Position lastPostion;

	@FXML
	protected void handleCanvasClicked(MouseEvent event) {

		Position p = convertPosition(event.getX() - CANVAS_GAP, event.getY() - CANVAS_GAP);
		if (currentChess == Chess.BLACK) {
			if(game[p.x][p.y]==null) {
				drawChess(currentChess, p);
				//client.addChess(p.x, p.y);
				currentChess = Chess.WHITE;
				game[p.x][p.y] = Chess.BLACK;
			}
			
		}

		else if (currentChess == Chess.WHITE) {
			if(game[p.x][p.y]==null) {
				drawChess(currentChess, p);
				currentChess = Chess.RED;
				game[p.x][p.y] = Chess.WHITE;
			}
		}

		else if (currentChess == Chess.RED) {
			if(game[p.x][p.y]==null) {
				drawChess(currentChess, p);
				currentChess = Chess.BLUE;
				game[p.x][p.y] = Chess.RED;
			}
		}

		else if (currentChess == Chess.BLUE) {
			if(game[p.x][p.y]==null) {
				drawChess(currentChess, p);
				currentChess = Chess.BLACK;
				game[p.x][p.y] = Chess.BLUE;
			}
		}

	}
	
	
	private Position convertPosition(double x, double y) {
		int pX = (int) ((x + BOARD_GAP / 2) / BOARD_GAP);
		int pY = (int) ((y + BOARD_GAP / 2) / BOARD_GAP);
		return new Position(pX, pY);
	}

	public void initialize() {
		System.out.println("Hello");
		gc = canvas.getGraphicsContext2D();
		drawChessBoard();
		addCoordinates();
	}

	private void drawChessBoard() {
		gc.setFill(COLOR_BOARD);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		gc.setStroke(COLOR_LINE);
		for (int i = 0; i <= ROWS; i++) {
			gc.strokeLine(CANVAS_GAP, CANVAS_GAP + i * BOARD_GAP, CANVAS_GAP + BOARD_WIDTH, CANVAS_GAP + i * BOARD_GAP);
			gc.strokeLine(CANVAS_GAP + i * BOARD_GAP, CANVAS_GAP, CANVAS_GAP + i * BOARD_GAP, CANVAS_GAP + BOARD_WIDTH);
		}
	}

	private void addCoordinates() {
		gc.setFill(COLOR_MARK);
		gc.setFont(Font.font(CANVAS_GAP / 2));
		for (int i = 0; i <= ROWS; i++) {
			gc.fillText(MARKX[i], i * BOARD_GAP + CANVAS_GAP - 5, CANVAS_GAP - 5);
			gc.fillText(MARKX[i], i * BOARD_GAP + CANVAS_GAP - 5, canvas.getHeight() - 5);
			gc.fillText(MARKY[i], 5, BOARD_GAP * i + CANVAS_GAP + 5);
			gc.fillText(MARKY[i], canvas.getWidth() - CANVAS_GAP + 5, BOARD_GAP * i + CANVAS_GAP + 5);

		}
	}

	private void drawChess(Chess chess, Position p) {
		double x = p.x * BOARD_GAP + CANVAS_GAP;
		double y = p.y * BOARD_GAP + CANVAS_GAP;
		switch (chess) {
		case BLACK:
			gc.setFill(Color.BLACK);
			gc.fillOval(x - CHESS_SIZE / 2, y - CHESS_SIZE / 2, CHESS_SIZE, CHESS_SIZE);
			break;
		case WHITE:
			gc.setFill(Color.WHITE);
			gc.fillOval(x - CHESS_SIZE / 2, y - CHESS_SIZE / 2, CHESS_SIZE, CHESS_SIZE);
			break;
		case RED:
			gc.setFill(Color.RED);
			gc.fillOval(x - CHESS_SIZE / 2, y - CHESS_SIZE / 2, CHESS_SIZE, CHESS_SIZE);
			break;
		case BLUE:
			gc.setFill(Color.BLUE);
			gc.fillOval(x - CHESS_SIZE / 2, y - CHESS_SIZE / 2, CHESS_SIZE, CHESS_SIZE);
			break;
		}
	}


	/**
	 * These two methods are used to switch windows from lobby.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("ChessBoard.fxml"));
			Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Gomoku");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showWindow() throws Exception {
		start(new Stage());
	}
}

class Position {
	int x;
	int y;

	Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
