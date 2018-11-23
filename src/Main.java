import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		// TODO Create a root container
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(15));
		
		root.getChildren().add(new Label("Hello World"));
		root.getChildren().add(new Label("by Ming and Pakim"));
		
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
