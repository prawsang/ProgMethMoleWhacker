import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class Block extends StackPane {
	
	private Node currentNode;
	
	public Block() {
		setPrefWidth(50);
		setPrefHeight(50);
		setStyle("-fx-background-color: #ddd");
	}
	
	public void setCurrentNode(Node n) {
		this.currentNode = n;
		this.getChildren().clear();
		this.getChildren().add(n);
	}
	public Object getCurrentNode() {
		return this.currentNode;
	}
}
