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
		tiles.setHgap(Constants.BLOCK_SPACING);
		tiles.setVgap(Constants.BLOCK_SPACING);
		tiles.setPadding(new Insets(100,0,0,53));
		
		canvas = new Canvas(Constants.BLOCK_SIZE*3 + Constants.BLOCK_SPACING*2, Constants.BLOCK_SIZE*4 + Constants.BLOCK_SPACING*3);
		getChildren().addAll(canvas, tiles);
	}
	
	public TilePane getTiles() {
		return tiles;
	}
	
	public GraphicsContext getGC() {
		return canvas.getGraphicsContext2D();
	}
	
	public double getX(int index) {
		return (index % 3) * (Constants.BLOCK_SIZE+Constants.BLOCK_SPACING);
	}
	public double getY(int index) {
		return (Math.floor(index/3)) * (Constants.BLOCK_SIZE+Constants.BLOCK_SPACING);
	}
	
	public void drawGC(int index, String imagePath) {
		Image img = new Image(imagePath);
		this.getGC().drawImage(img, getX(index), getY(index), Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
	}
	
	public void clearGC(int index) {
		this.getGC().clearRect(getX(index), getY(index), Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
	}
	
	
}
