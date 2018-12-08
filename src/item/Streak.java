package item;

import application.Main;
import application.Resources;

public class Streak extends PowerUp {
	public Streak() {
		super(Resources.BOOM);
	}
	public void usePowerUp() {
		Main.gameController.startStreak();
	}
}
