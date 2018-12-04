package component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import application.Constants;

public class GameOver extends VBox {
	private Button startButton;
	private Button exitButton;
	
	private Label scoreLabel;
	
	public GameOver(int score) {
		startButton = new CustomButton("Play Again");
		exitButton = new CustomButton("Exit");
		
		setStyle("-fx-width: " + Constants.WIDTH + "px ;");
		setPadding(new Insets(40,10,40,10));
		
		Label gameOver = new Label("Game Over");
		gameOver.setStyle(style(40));
		gameOver.setPadding(new Insets(20,0,0,0));
		gameOver.setTextFill(Color.WHITE);
		
		scoreLabel = new Label("Your Score is: " + score);
		scoreLabel.setStyle(style(20));
		scoreLabel.setPadding(new Insets(0,0,20,0));
		scoreLabel.setTextFill(Color.WHITE);
		
		setSpacing(10);
		setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: rgba(244,114,24,0.5)");
		getChildren().addAll(gameOver, scoreLabel, startButton, exitButton);
	}
	
	public String style(int fontSize) {
		return "-fx-font-family: 'Titan One', cursive; -fx-font-size: " + fontSize + "px; ";
	}
	
	public void setScore(int score) {
		scoreLabel.setText("Your Score is: " + score);
	}
	public Button getStartButton() {
		return startButton;
	}

	public Button getExitButton() {
		return exitButton;
	}
}
