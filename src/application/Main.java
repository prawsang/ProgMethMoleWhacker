package application;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {
	
	// Sizes
	public static final int WIDTH = 354;
	public static final int HEIGHT = 557;
	public static final int BLOCKSIZE = 80;
	public static final double BLOCKSPACING = 3;
	
	
	private BlockPane blockPane;
	private Label scoreLabel;
	private BombPane bombPane;

	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Titan+One");
		
		Canvas canvas = new Canvas(WIDTH,HEIGHT);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// Background
		gc.drawImage(new Image(Resources.BACKGROUND), 0, 0, WIDTH, HEIGHT);
		
		// Block Pane
		blockPane = new BlockPane();
		blockPane.setPadding(new Insets(100,53,0,53));
		root.getChildren().add(blockPane);
		
		// Frame
		gc.drawImage(new Image(Resources.FRAME), 0, 0, WIDTH, HEIGHT);
		
		// Labels
		BorderPane labels = new BorderPane();
		gc.drawImage(new Image(Resources.SCORE), (WIDTH-207)/2, 0, 207, 88);
		
		scoreLabel = new Label("0");
		scoreLabel.setPadding(new Insets(28,0,0,0));
		scoreLabel.setPrefWidth(WIDTH);
		scoreLabel.setAlignment(Pos.CENTER);
		scoreLabel.setTextFill(Color.web("white"));
		scoreLabel.setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 30px");
		labels.setTop(scoreLabel);
		
		bombPane = new BombPane();
		bombPane.drawBombPane(0);
		labels.setBottom(bombPane);
		labels.setMouseTransparent(true);
		root.getChildren().add(labels);
		
		// Start Game
		GameController gameController = new GameController(blockPane, scoreLabel, bombPane);
		gameController.setUpEnterEventHandler(scene);
		gameController.startGameLoop();

		// TODO Set up the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mole Whacker");
		primaryStage.setResizable(false);
		primaryStage.setWidth(WIDTH);
		primaryStage.setHeight(HEIGHT);
		primaryStage.show();
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
}
