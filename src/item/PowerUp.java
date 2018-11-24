package item;

public abstract class PowerUp extends Item {	
	public PowerUp(String image) {
		super(image);
	}
	
	public abstract void usePowerUp();
}
