package application;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;

public class BlockPane extends TilePane {
	public BlockPane() {
		setPrefRows(4);
		setPrefColumns(4);
		setTileAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
	}
}
