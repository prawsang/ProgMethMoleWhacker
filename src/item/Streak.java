package item;

import application.Main;
import application.Resources;

public class Streak extends PowerUp {
	public Streak() {
		super(Resources.STREAK_STAR);
	}
	public void usePowerUp() {
		Main.gameController.startStreak();
	}
}
