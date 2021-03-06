package view;

import component.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SplashScreen extends VBox {
	
	private Button startButton;
	private Button exitButton;
	private Button resetButton;
	
	private class Title extends VBox {
		public Title() {
			Label label1 = new Label("Ming & Plakim's");
			Label label2 = new Label("Mole");
			Label label3 = new Label("Whacker");
			label1.setStyle(style(20));
			label2.setStyle(style(40));
			label3.setStyle(style(40));
			
			label1.setTextFill(Color.WHITE);
			label2.setTextFill(Color.WHITE);
			label3.setTextFill(Color.WHITE);
			
			setAlignment(Pos.CENTER);
			setPadding(new Insets(0,0,20,0));
			getChildren().addAll(label1, label2, label3);
		}
		
		private String style(int fontSize) {
			return "-fx-font-family: 'Titan One', cursive; "
					+ "-fx-font-size: " + fontSize + "px;";
		}
	}
	
	public SplashScreen() {
		Title title = new Title();
		
		startButton = new CustomButton("Start Game");
		exitButton = new CustomButton("Exit");
	
		resetButton = new CustomButton("Reset High Score",1);
		
		setAlignment(Pos.CENTER);
		setSpacing(10);
		getChildren().addAll(title, startButton, resetButton, exitButton);
	}
	
	public Button getStartButton() {
		return startButton;
	}
	public Button getResetButton() {
		return resetButton;
	}
	public Button getExitButton() {
		return exitButton;
	}
}
