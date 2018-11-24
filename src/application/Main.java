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
	private Label bombLabel;

	@Override
	public void start(Stage primaryStage) {
		
		// TODO Create a root container
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(15));
		
		Scene scene = new Scene(root);
		
		scoreLabel = new Label();
		bombLabel = new Label();
		blockPane = new BlockPane();
		
		GameController gameController = new GameController(blockPane, scoreLabel, bombLabel);
		
		root.getChildren().addAll(scoreLabel, bombLabel, blockPane);
		
		gameController.setUpEnterEventHandler(scene);
		gameController.startGameLoop();

		// TODO Set up the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Battle Monopoly");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
}