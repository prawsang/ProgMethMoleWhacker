package application;
import javafx.stage.Stage;
import component.BlockPane;
import component.BombPane;
import component.SplashScreen;
import component.GameOver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {
	
	// Scene
	private Scene scene;
	private static StackPane root;
	private SplashScreen splashScreen;
	private static GameOver gameOver;
	
	// Components
	private BlockPane blockPane;
	private Label scoreLabel;
	private BombPane bombPane;
	
	// Controller
	GameController gameController;

	@Override
	public void start(Stage primaryStage) {
		root = new StackPane();
		
		scene = new Scene(root);
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Titan+One");
		
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
		Canvas feverEffects = new Canvas(Constants.WIDTH, Constants.HEIGHT);
		feverEffects.setMouseTransparent(true);
		root.getChildren().add(feverEffects);
		
		// Boom
		Canvas effects = new Canvas(Constants.WIDTH, Constants.HEIGHT);
		effects.setMouseTransparent(true);
		root.getChildren().add(effects);
		
		// Labels
		BorderPane labels = new BorderPane();
		gc.drawImage(new Image(Resources.SCORE), (Constants.WIDTH-207)/2, 0, 207, 88);
		
		scoreLabel = new Label("0");
		scoreLabel.setPadding(new Insets(28,0,0,0));
		scoreLabel.setPrefWidth(Constants.WIDTH);
		scoreLabel.setAlignment(Pos.CENTER);
		scoreLabel.setTextFill(Color.web("white"));
		scoreLabel.setStyle("-fx-font-family: 'Titan One', cursive; -fx-font-size: 30px");
		labels.setTop(scoreLabel);
		
		bombPane = new BombPane();
		bombPane.drawBombPane(0);
		labels.setBottom(bombPane);
		labels.setMouseTransparent(true);
		root.getChildren().add(labels);	
		
		// Splash Screen
		splashScreen = new SplashScreen();
		root.getChildren().add(splashScreen);
		setUpSplashScreenEvents();
		
		// Game Over
		gameOver = new GameOver(0);
		setUpGameOverEvents();
		
		// GameController
		gameController = new GameController(
				blockPane, 
				scoreLabel, 
				bombPane,
				effects.getGraphicsContext2D(), 
				feverEffects.getGraphicsContext2D()
			);
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
	private void setUpSplashScreenEvents() {
		Button startButton = splashScreen.getStartButton();
		Button exitButton = splashScreen.getExitButton();
		
		startButton.setOnAction((e) -> {
			gameController.startGameLoop();
			root.getChildren().remove(splashScreen);
		});
		exitButton.setOnAction((e) -> {
			System.exit(0);
		});
	}
	
	// Game Over
	private void setUpGameOverEvents() {
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
