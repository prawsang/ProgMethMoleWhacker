package application;
import javafx.stage.Stage;

import java.net.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {
	
	// Sizes
	public static final int WIDTH = 354;
	public static final int HEIGHT = 557;
	public static final int BLOCKSIZE = 80;
	public static final double BLOCKSPACING = 1;
	
	
	private BlockPane blockPane;
	private Label scoreLabel;
	private BombPane bombPane;

	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		
		Scene scene = new Scene(root);
		
		scoreLabel = new Label("0");
		scoreLabel.setPadding(new Insets(0,0,45,0));
		
		blockPane = new BlockPane();
		blockPane.setPadding(new Insets(0,0,10,0));
		
		Canvas canvas = new Canvas(WIDTH,HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		bombPane = new BombPane();
		
		GameController gameController = new GameController(blockPane, scoreLabel, bombPane, gc);
		
		gc.drawImage(new Image(Resources.BACKGROUND), 0, 0, WIDTH, HEIGHT);
		
		gc.drawImage(new Image(Resources.SCORE), (WIDTH-207)/2, 0, 207, 88);
		
		VBox mainGame = new VBox();
		
		root.getChildren().add(canvas);
		mainGame.setPadding(new Insets(30,53,0,53));
		mainGame.getChildren().addAll(scoreLabel, blockPane, bombPane);
		mainGame.setAlignment(Pos.CENTER);
		
		root.getChildren().add(mainGame);
		
		Canvas frame = new Canvas(WIDTH,HEIGHT);
		frame.getGraphicsContext2D().drawImage(new Image(Resources.FRAME), 0, 0, WIDTH, HEIGHT);
		root.getChildren().add(frame);
		
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
