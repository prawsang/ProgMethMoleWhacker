package application;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;

import item.*;

public class BlockPane extends TilePane {
	public BlockPane() {
		setPrefRows(4);
		setPrefColumns(4);
		setTileAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
		
		for (int i = 0; i < 16; i++) {
			Block block = new Block(i);
			block.setOnMouseClicked((e) -> {
				if (block.getCurrentNode() == null) return;
				if (!((Enemy)(block.getCurrentNode())).takeDamage()) {
					block.clearNode();
					GameManager.addAvailable(block.getIndex());
					Main.addScore(100 * GameManager.getScoreMultiplier());
				}
			});
			getChildren().add(block);
		}
	}
}
