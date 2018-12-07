package item;

import application.Main;
import application.Resources;

public class Bomb extends PowerUp implements Collectible  {
	
	public Bomb() {
		super(Resources.BOMB);
	}
	
	public void collect() {
		Main.gameController.addBomb();
	}
	public void usePowerUp() {
		Main.gameController.clearBoard();
		Main.gameController.deleteBomb();
	}
}
