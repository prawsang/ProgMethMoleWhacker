package application;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class Block extends StackPane {
	
	private Node currentNode;
	private final int index;
	
	public Block(int index) {
		this.index = index;
		
		setPrefWidth(50);
		setPrefHeight(50);
		setStyle("-fx-background-color: #ddd");
	}
	
	public void setCurrentNode(Node n) {
		this.currentNode = n;
		this.getChildren().clear();
		this.getChildren().add(n);
	}
	public Node getCurrentNode() {
		return this.currentNode;
	}
	public boolean isEmpty() {
		return this.getChildren().size() == 0;
	}
	
	public void clearNode() {
		this.getChildren().clear();
	}
	public int getIndex() {
		return index;
	}
}
