package item;

import application.GameController;

public class Bomb extends PowerUp implements Collectible  {
	
	private GameController gameController;
	
	public Bomb(GameController gameController) {
		super("yellow");
		this.gameController = gameController;
	}
	
	public void collect() {
		this.gameController.addBomb();
	}
	public void usePowerUp() {
		this.gameController.clearBoard();
		this.gameController.deleteBomb();
	}
}
