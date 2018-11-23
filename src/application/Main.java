package application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


public class Main extends Application {
	
	private BlockPane blockPane;
	private Label scoreLabel;

	@Override
	public void start(Stage primaryStage) {
		
		// TODO Create a root container
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(15));
		
		
		scoreLabel = new Label();
		blockPane = new BlockPane();
		
		GameManager gameManager = new GameManager(blockPane, scoreLabel);
		
		root.getChildren().addAll(scoreLabel, blockPane);
		
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
