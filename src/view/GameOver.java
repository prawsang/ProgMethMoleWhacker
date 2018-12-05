package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.HighScoreLogic;

import application.Constants;
import component.CustomButton;

public class GameOver extends VBox {
	private Button startButton;
	private Button exitButton;
	
	private Label scoreLabel;
	private Label highScore;
	
	public GameOver(int score) {
		startButton = new CustomButton("Play Again",1);
		exitButton = new CustomButton("Exit",1);
		
		setStyle("-fx-width: " + Constants.WIDTH + "px ;");
		setPadding(new Insets(40,10,40,10));
		
		Label gameOver = new Label("Game Over");
		gameOver.setStyle(style(40));
		gameOver.setPadding(new Insets(20,0,0,0));
		gameOver.setTextFill(Color.WHITE);
		getChildren().add(gameOver);
		
		scoreLabel = new Label("Your Score is: " + score);
		scoreLabel.setStyle(style(20));
		scoreLabel.setTextFill(Color.WHITE);
		getChildren().add(scoreLabel);
		
		highScore = new Label("New High Score!");
		highScore.setStyle(style(18));
		highScore.setTextFill(Color.WHITE);
		highScore.setPadding(new Insets(0,0,20,0));
		highScore.setVisible(false);
		getChildren().add(highScore);
		
		getChildren().addAll(startButton, exitButton);
		
		setSpacing(10);
		setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: rgba(244,114,24,0.5)");
	}
	
	public String style(int fontSize) {
		return "-fx-font-family: 'Titan One', cursive; -fx-font-size: " + fontSize + "px; ";
	}
	
	public void setScore(int score) {
		scoreLabel.setText("Your Score is: " + score);
		if (score > HighScoreLogic.getHighScore()) highScore.setVisible(true);
		else highScore.setVisible(false);
	}
	public Button getStartButton() {
		return startButton;
	}

	public Button getExitButton() {
		return exitButton;
	}
}
