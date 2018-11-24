package item;


public abstract class Enemy extends Item {
	private int health;

	public Enemy(int health, String image) {
		super(image);
		this.health = health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	public int getHealth() {
		return this.health;
	}
	
	public abstract boolean takeDamage();
}
