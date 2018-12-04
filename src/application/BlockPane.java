package application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class BlockPane extends StackPane {
	
	private TilePane tiles;
	private Canvas canvas;
	
	public BlockPane() {
		tiles = new TilePane();
		
		tiles.setPrefRows(4);
		tiles.setPrefColumns(3);
		tiles.setTileAlignment(Pos.CENTER);
		tiles.setHgap(Main.BLOCKSPACING);
		tiles.setVgap(Main.BLOCKSPACING);
		tiles.setPadding(new Insets(100,53,0,53));
		
		canvas = new Canvas(Main.BLOCKSIZE*3 + Main.BLOCKSPACING*2, Main.BLOCKSIZE*4 + Main.BLOCKSPACING*3);
		canvas.setStyle("-fx-background-color: red");
		getChildren().addAll(canvas, tiles);
	}
	
	public TilePane getTiles() {
		return tiles;
	}
	
	public GraphicsContext getGC() {
		return canvas.getGraphicsContext2D();
	}
	
	public double getX(int index) {
		return (index % 3) * (Main.BLOCKSIZE+Main.BLOCKSPACING);
	}
	public double getY(int index) {
		return (Math.floor(index/3)) * (Main.BLOCKSIZE+Main.BLOCKSPACING);
	}
	
	public void drawGC(int index, String imagePath) {
		
		Image img = new Image(imagePath);
		this.getGC().drawImage(img, getX(index), getY(index), Main.BLOCKSIZE, Main.BLOCKSIZE);
	}
	
	public void clearGC(int index) {
		this.getGC().clearRect(getX(index), getY(index), Main.BLOCKSIZE, Main.BLOCKSIZE);
	}
	
	
}
