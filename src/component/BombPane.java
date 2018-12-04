package component;

import javafx.scene.image.Image;
import application.Resources;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class BombPane extends StackPane {
	
	private Canvas canvas;
	
	public BombPane() {
		this.canvas = new Canvas(146,94);
		getChildren().add(canvas);
		setAlignment(Pos.BOTTOM_CENTER);
	}
	
	public void drawBombPane(int bombs) {
		String imagePath;
		switch (bombs) {
			case 0: imagePath = Resources.BOMBS0; break;
			case 1: imagePath = Resources.BOMBS1; break;
			case 2: imagePath = Resources.BOMBS2; break;
			case 3: imagePath = Resources.BOMBS3; break;
			default: return;
		}
		Image img = new Image(imagePath);
		this.canvas.getGraphicsContext2D().clearRect(0, 0, img.getWidth()/2, img.getHeight()/2); 
		this.canvas.getGraphicsContext2D().drawImage(img, 0, 0, img.getWidth()/2, img.getHeight()/2);
	}
	
	
}
