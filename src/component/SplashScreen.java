package component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SplashScreen extends VBox {
	
	private Button startButton;
	private Button exitButton;
	
	public SplashScreen() {
		Title title = new Title();
		
		startButton = new CustomButton("Start Game",0);
		exitButton = new CustomButton("Exit",0);
	
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
