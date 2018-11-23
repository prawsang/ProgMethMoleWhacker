package application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import logic.*;


public class Main extends Application {
	
	public static BlockPane blockPane;

	@Override
	public void start(Stage primaryStage) {
		
		// TODO Create a root container
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(15));
		
		blockPane = new BlockPane();
		root.getChildren().add(blockPane);
		
		GameManager gameManager = new GameManager();
		gameManager.startGameLoop();

		// TODO Set up the stage
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Battle Monopoly");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
}
