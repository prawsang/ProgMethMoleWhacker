package item;

import application.Main;
import application.Resources;

public class FeverStar extends PowerUp {
	public FeverStar() {
		super(Resources.FEVER_STAR);
	}
	public void usePowerUp() {
		Main.gameController.startFever();
	}
}
