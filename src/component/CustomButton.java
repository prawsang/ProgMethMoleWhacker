package component;

import application.Constants;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class CustomButton extends Button {
	public CustomButton(String text) {
		setText(text);
		setStyle(style(0));
		setPadding(new Insets(10,15,10,15));
		setTextFill(Color.WHITE);
	}
	public CustomButton(String text, int color) {
		// color = 0: orange, color = 1: white;
		setText(text);
		setStyle(style(color));
		setPadding(new Insets(10,15,10,15));
		setTextFill(color != 0 ? Color.web(Constants.ORANGE) : Color.WHITE);
	}
	
	private String style(int color) {
		return "-fx-background-color: " + (color == 0 ? Constants.ORANGE : "white") + ";"
				+ "-fx-font-family: 'Titan One', cursive;"
				+ "-fx-font-size: 16px;";
	}
}
