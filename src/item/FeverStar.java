package item;

import application.Main;
import application.Resources;

public class FeverStar extends PowerUp {
	public FeverStar() {
		super(Resources.FEVERSTAR);
	}
	public void usePowerUp() {
		Main.gameController.startFever();
	}
}
