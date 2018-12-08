package item;

import application.Resources;

public class NormalEnemy extends Enemy {
	
	public NormalEnemy() {
		super(1, Resources.NORMAL_ENEMY);
	}
	
	public boolean takeDamage() {
		this.setHealth(0);
		return false;
	}
}
