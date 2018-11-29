package application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class Block extends StackPane {
	
	private Node currentNode;
	private final int index;
	private Thread thread;
	
	public Block(int index) {
		this.index = index;
		
		setPrefWidth(80);
		setPrefHeight(80);
		setStyle("-fx-background-color: #ddd");
	}
	
	public void setCurrentNode(Node n) {
		this.currentNode = n;
		this.getChildren().clear();
		this.getChildren().add(n);
	}
	public void setCurrentNodeWithTimer(Node n, int duration) {
		this.thread = new Thread(() -> {
			try {
				Platform.runLater(()->{
					this.currentNode = n;
					this.getChildren().clear();
					this.getChildren().add(n);
				});
				
				Thread.sleep(duration);
				Platform.runLater(()->{
					this.clearNode();
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		this.thread.start();
	}
	public boolean hasRunningTimer() {
		return this.thread != null;
	}
	public void stopTimer() {
		if (this.thread != null) this.thread.interrupt();
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
