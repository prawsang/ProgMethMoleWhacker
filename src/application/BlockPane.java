package application;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class BlockPane extends TilePane {
	
	public BlockPane() {	
		setPrefRows(4);
		setPrefColumns(3);
		setTileAlignment(Pos.CENTER);
		setHgap(Main.BLOCKSPACING);
		setVgap(Main.BLOCKSPACING);
	}
}
