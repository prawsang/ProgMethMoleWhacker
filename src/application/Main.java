package application;
import javafx.stage.Stage;
import logic.HighScoreLogic;
import view.GameOver;
import view.SplashScreen;

import java.io.IOException;
import java.util.Optional;

import component.BlockPane;
import component.BombPane;
import component.ScorePane;
import component.HighScoreLabel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


public class Main extends Application {
	
	// Scene
	private static Scene scene;
	private static SplashScreen splashScreen;
	private static StackPane root;
	private static GameOver gameOver;
	
	// Components
	public static BlockPane blockPane;
	public static ScorePane scorePane;
	public static BombPane bombPane;
	public static HighScoreLabel highScoreLabel;
	
	// Effects
	public static Canvas effects;
	public static Canvas feverEffects;
	
	// Controller
	public static GameController gameController;

	@Override
	public void start(Stage primaryStage) {
		root = new StackPane();
		
		scene = new Scene(root);
		Font.loadFont(Main.class.getClassLoader().getResource("font/TitanOne-Regular.ttf").toExternalForm(), 10 );
		
		Canvas canvas = new Canvas(Constants.WIDTH,Constants.HEIGHT);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// Background
		gc.drawImage(new Image(Resources.BACKGROUND), 0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		// Block Pane
		blockPane = new BlockPane();
		root.getChildren().add(blockPane);
		
		// Frame
		gc.drawImage(new Image(Resources.FRAME), 0, 0, Constants.WIDTH, Constants.HEIGHT);
		
		// Fever Effects
		feverEffects = new Canvas(Constants.WIDTH, Constants.HEIGHT);
		feverEffects.setMouseTransparent(true);
		root.getChildren().add(feverEffects);
		
		// Effects
		effects = new Canvas(Constants.WIDTH, Constants.HEIGHT);
		effects.setMouseTransparent(true);
		root.getChildren().add(effects);
		
		// Labels
		BorderPane labels = new BorderPane();
		scorePane = new ScorePane("0");
		labels.setTop(scorePane);
		
		bombPane = new BombPane();
		labels.setBottom(bombPane);
		
		labels.setMouseTransparent(true);
		root.getChildren().add(labels);	
		
		// High score
		highScoreLabel = new HighScoreLabel();
		highScoreLabel.setMouseTransparent(true);
		root.getChildren().add(highScoreLabel);
		
		// Splash Screen
		splashScreen = new SplashScreen();
		root.getChildren().add(splashScreen);
		setUpSplashScreenEvents();
		
		// Game Over
		gameOver = new GameOver(0);
		setUpGameOverEvents();
		
		// GameController
		gameController = new GameController();
		gameController.setUpEnterEventHandler(scene);

		// TODO Set up the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mole Whacker");
		primaryStage.setResizable(false);
		primaryStage.setWidth(Constants.WIDTH);
		primaryStage.setHeight(Constants.HEIGHT);
		primaryStage.show();
	}
	
	// Splash Screen
	private static void setUpSplashScreenEvents() {
		Button startButton = splashScreen.getStartButton();
		Button exitButton = splashScreen.getExitButton();
		Button resetButton = splashScreen.getResetButton();
		
		startButton.setOnAction((e) -> {
			gameController.startGameLoop();
			root.getChildren().remove(splashScreen);
		});
		resetButton.setOnAction((e) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Are you sure you want to reset your high score?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					HighScoreLogic.resetHighScore();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				highScoreLabel.setHighScoreText(HighScoreLogic.getHighScore());
				alert.close();
			} else {
			    alert.close();
			}
		});
		exitButton.setOnAction((e) -> {
			System.exit(0);
		});
	}
	
	// Game Over
	private static void setUpGameOverEvents() {
		Button startButton = gameOver.getStartButton();
		Button exitButton = gameOver.getExitButton();
		
		startButton.setOnAction((e) -> {
			gameController.resetValues();
			gameController.startGameLoop();
			root.getChildren().remove(gameOver);
		});
		exitButton.setOnAction((e) -> {
			System.exit(0);
		});
	}
	
	public static void showGameOver( int score ) {
		gameOver.setScore(score);
		root.getChildren().add(gameOver);
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
}
