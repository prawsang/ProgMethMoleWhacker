package component;

import application.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ScoreLabel extends Label {
	public ScoreLabel(String text) {
		setText(text);
		setPadding(new Insets(28,0,0,0));
		setPrefWidth(Constants.WIDTH);
		setAlignment(Pos.CENTER);
		setTextFill(Color.web("white"));
		setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 30px");
	}
}
