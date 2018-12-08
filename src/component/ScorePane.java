package component;

import application.Constants;
import application.Resources;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ScorePane extends StackPane {
	
	private Label label;
	
	public ScorePane(String text) {
		Image scoreBG = new Image(Resources.SCORE);
		Canvas canvas = new Canvas(scoreBG.getWidth()/2, scoreBG.getHeight()/2);
		canvas.getGraphicsContext2D().drawImage(scoreBG, 0, 0, 207, 88);
		getChildren().add(canvas);
		
		label = new Label(text);
		label.setPadding(new Insets(28,0,0,0));
		label.setPrefWidth(Constants.WIDTH);
		label.setAlignment(Pos.CENTER);
		label.setTextFill(Color.web("white"));
		label.setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 30px");
		getChildren().add(label);
	}
	
	public void setScoreLabelText(int score) {
		label.setText(Integer.toString(score));
	}
}
