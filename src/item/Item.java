package item;

import javafx.scene.layout.StackPane;

public class Item extends StackPane {
	
	private String image;
	
	public Item(String image) {
		this.image = image;
		
		setPrefWidth(50);
		setPrefHeight(50);
		setStyle("-fx-background-color:" + this.image);
	}
	
	public String getImage() {
		return this.image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
