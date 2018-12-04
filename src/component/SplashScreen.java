package component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SplashScreen extends VBox {
	
	private Button startButton;
	private Button exitButton;
	
	public SplashScreen() {
		Title title = new Title();
		
		startButton = new CustomButton("Start Game");
		exitButton = new CustomButton("Exit");
	
		setAlignment(Pos.CENTER);
		setSpacing(10);
		getChildren().addAll(title, startButton, exitButton);
	}
	
	public Button getStartButton() {
		return startButton;
	}
	public Button getExitButton() {
		return exitButton;
	}
}
