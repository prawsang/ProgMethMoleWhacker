package item;

import javafx.scene.layout.StackPane;

public abstract class Enemy extends StackPane {
	protected int health;
	protected String image;

	public Enemy(int health, String image) {
		this.health = health;
		this.image = image;
		
		setPrefWidth(50);
		setPrefHeight(50);
		setStyle("-fx-background-color:" + image);
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	public int getHealth() {
		return this.health;
	}
	
	public abstract boolean takeDamage();
}
