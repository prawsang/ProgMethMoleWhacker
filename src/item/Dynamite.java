package item;

import application.Main;
import application.Resources;
import javafx.scene.media.AudioClip;

public class Dynamite extends PowerUp {
	
	private int blockIndex;
	
	public Dynamite(int index) {
		super(Resources.DYNAMITE);
		this.blockIndex = index;
	}
	public void usePowerUp() {
		Main.gameController.killAdjacentEnemies(this.blockIndex);
		AudioClip useBombSound = new AudioClip(Resources.BOMB_SOUND);
		useBombSound.play();
	}
}
