package item;

public class StrongEnemy extends Enemy {
	public StrongEnemy() {
		super(3, "blue");
	}
	
	public boolean takeDamage() {
		this.setHealth(this.getHealth()-1);
		if (this.getHealth() <= 0) {
			return false;
		}
		return true;
	}
}
