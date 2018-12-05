package component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.HighScoreLogic;

public class HighScoreLabel extends VBox {
	
	private Label scoreLabel;
	
	public HighScoreLabel() {
		Label highscoreText = new Label("High Score");
		highscoreText.setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 10px");
		
		scoreLabel = new Label(Integer.toString(HighScoreLogic.getHighScore()));
		scoreLabel.setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 18px");
		
		scoreLabel.setTextFill(Color.WHITE);
		highscoreText.setTextFill(Color.WHITE);
		
		getChildren().addAll(highscoreText, scoreLabel);
	
		setAlignment(Pos.TOP_RIGHT);
		setPadding(new Insets(10));
	}
	
	public void setHighScoreText(int i) {
		scoreLabel.setText(Integer.toString(i));
	}
}
