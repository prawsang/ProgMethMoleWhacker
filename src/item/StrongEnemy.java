package item;

import application.Resources;

public class StrongEnemy extends Enemy {
	public StrongEnemy() {
		super(3, Resources.STRONG_ENEMY);
	}
	
	public boolean takeDamage() {
		this.setHealth(this.getHealth()-1);
		if (this.getHealth() <= 0) {
			return false;
		}
		return true;
	}
}
