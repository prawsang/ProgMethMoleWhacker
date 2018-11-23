package application;

import java.util.ArrayList;
import java.util.Random;

import item.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class GameManager {

	private BlockPane blockPane;
	private Label scoreLabel;
	private Logic logic;
	
	private int speed;
	public static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	private ArrayList<Integer> available;
	
	public GameManager(BlockPane blockPane, Label scoreLabel) {
		this.scoreLabel = scoreLabel;
		this.blockPane = blockPane;
		
		this.logic = new Logic();
		this.speed = 1000;
		this.available = new ArrayList<Integer>();

		for (int i = 0; i < 16; i++) {
			available.add(i);
			
			Block block = new Block(i);
			block.setOnMouseClicked(new BlockEventHandler(block));
			this.blockPane.getChildren().add(block);
		}
	}
	
	private class BlockEventHandler implements EventHandler<MouseEvent>{
		private Block block;
		public BlockEventHandler(Block block) {
			this.block = block;
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			if (block.isEmpty()) return;
			Node node = this.block.getCurrentNode();
			if (node instanceof Enemy) {
				Enemy e = (Enemy) node;
				if (!e.takeDamage()) {
					block.clearNode();
					available.add(block.getIndex());
					scoreLabel.setText("Score: " + logic.addScore(100));
				}
			}
		}
	}
	
	public void startGameLoop() {
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(15000);
					if (this.speed > 700) {
						this.speed -= 50;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		speedThread.start();
		
		Thread t = new Thread(() ->  {
			while(true) {
				try {
					Thread.sleep(this.speed);
					Platform.runLater(() -> {
						this.randomGame();
					});
					if (this.available.size() == 0) {
						System.out.println("You Lose");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t.start();
	}
	
	private void randomGame() {
		if (available.size() == 0) return;
		int itemsAtOnce = 1;
		int random = randomProb[new Random().nextInt(randomProb.length)];
		
		if (this.speed < 1000) {
			if (random <= 5) {
				itemsAtOnce = 1;
			} else {
				itemsAtOnce = 2;
			}
		} else if (this.speed < 800) {
			if (random <= 4) {
				itemsAtOnce = 1;
			} else if (random >= 7) {
				itemsAtOnce = 3;
			} else {
				itemsAtOnce = 2;
			}
		}
		
		for (int i = 0; i < itemsAtOnce; i++) {
			int position = new Random().nextInt(this.available.size());
			Block block = (Block) this.blockPane.getChildren().get(this.available.get(position));
			
			if (!this.available.isEmpty()) {
				random = randomProb[new Random().nextInt(randomProb.length)];
				this.available.remove(position);
				
				if (this.speed >= 900) {
					block.setCurrentNode(new NormalEnemy());
				} else {		
					if (random <= 6) {
						block.setCurrentNode(new NormalEnemy());
					} else {
						block.setCurrentNode(new StrongEnemy());
					}
				}
			}
		}
	}
	
	// Power Ups
	public void clearBoard() {
		for (Node node : this.blockPane.getChildren()) {
			Block block = (Block) node;
			if (!block.isEmpty()) {
				block.clearNode();
				this.logic.addScore(100);
			}
		}
		available.clear();
		for (int i = 0; i < 16; i++) {
			available.add(i);
		}
	}
}
