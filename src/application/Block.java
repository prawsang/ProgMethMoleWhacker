package application;
import item.Item;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class Block extends StackPane {
	
	private Node currentNode;
	private final int index;
	private Thread thread;
	private Canvas canvas;
	
	public Block(int index) {
		this.index = index;
		this.canvas = new Canvas();
		canvas.setWidth(Main.BLOCKSIZE);
		canvas.setHeight(Main.BLOCKSIZE);
		getChildren().add(canvas);
		
		setPrefWidth(Main.BLOCKSIZE);
		setPrefHeight(Main.BLOCKSIZE);
//		setStyle("-fx-outline-style: solid; -fx-outline-width: 1px; -fx-outline-color: #ddd;");
	}
	
	public void setCurrentNode(Node n) {
		this.currentNode = n;
		this.clearNode();
		this.getChildren().add(n);
		
		Image img = new Image(((Item)this.getCurrentNode()).getImage());
		this.getGC().drawImage(img, (Main.BLOCKSIZE - img.getWidth()/2)/2, (Main.BLOCKSIZE - img.getHeight()/2)/2, img.getWidth()/2, img.getHeight()/2);
	}
	public void setCurrentNodeWithTimer(Node n, int duration) {
		this.thread = new Thread(() -> {
			try {
				Platform.runLater(()->{
					setCurrentNode(n);
				});
				
				Thread.sleep(duration);
				Platform.runLater(()->{
					this.clearNode();
					GameController.available.add(this.index);
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
		return this.getChildren().size() == 1;
	}
	
	public void clearNode() {
		if (this.getChildren().size() > 1) {
			this.getChildren().remove(this.getChildren().size()-1);
		}
		this.canvas.getGraphicsContext2D().clearRect(0, 0, Main.BLOCKSIZE, Main.BLOCKSIZE);
	}
	public int getIndex() {
		return index;
	}
	
	public GraphicsContext getGC() {
		return canvas.getGraphicsContext2D();
	}
}
