package item;

public class NormalEnemy extends Enemy {
	
	public NormalEnemy() {
		super(1, "red");
	}
	
	public boolean takeDamage() {
		this.setHealth(this.getHealth()-1);
		if (this.getHealth() <= 0) {
			return false;
		}
		return true;
	}
}
