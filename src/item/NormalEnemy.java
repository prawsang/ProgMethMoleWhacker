package item;

public class NormalEnemy extends Enemy {
	
	public NormalEnemy() {
		super(1, "red");
	}
	
	public boolean takeDamage() {
		this.setHealth(0);
		return false;
	}
}
