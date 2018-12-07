package component;
import application.Constants;
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
		tiles.setHgap(Constants.BLOCKSPACING);
		tiles.setVgap(Constants.BLOCKSPACING);
		tiles.setPadding(new Insets(100,0,0,53));
		
		canvas = new Canvas(Constants.BLOCKSIZE*3 + Constants.BLOCKSPACING*2, Constants.BLOCKSIZE*4 + Constants.BLOCKSPACING*3);
		getChildren().addAll(canvas, tiles);
	}
	
	public TilePane getTiles() {
		return tiles;
	}
	
	public GraphicsContext getGC() {
		return canvas.getGraphicsContext2D();
	}
	
	public double getX(int index) {
		return (index % 3) * (Constants.BLOCKSIZE+Constants.BLOCKSPACING);
	}
	public double getY(int index) {
		return (Math.floor(index/3)) * (Constants.BLOCKSIZE+Constants.BLOCKSPACING);
	}
	
	public void drawGC(int index, String imagePath) {
		Image img = new Image(imagePath);
		this.getGC().drawImage(img, getX(index), getY(index), Constants.BLOCKSIZE, Constants.BLOCKSIZE);
	}
	
	public void clearGC(int index) {
		this.getGC().clearRect(getX(index), getY(index), Constants.BLOCKSIZE, Constants.BLOCKSIZE);
	}
	
	
}
