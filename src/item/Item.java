package item;

import javafx.scene.layout.StackPane;

public class Item extends StackPane {
	
	private String image;
	
	public Item(String image) {
		this.image = image;
		
		setMouseTransparent(true);
		setPrefWidth(80);
		setPrefHeight(80);
	}
	
	public String getImage() {
		return this.image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
