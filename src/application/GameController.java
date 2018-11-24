package application;

import java.util.ArrayList;
import java.util.Random;

import item.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameController {

	private BlockPane blockPane;
	private Label scoreLabel;
	private Label bombLabel;
	private Logic logic;
	
	private boolean lose;
	
	private int speed;
	public static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	private ArrayList<Integer> available;
	
	//PowerUps
	private int bombs;
	private Bomb bomb;
	private boolean fever;
	
	public GameController(BlockPane blockPane, Label scoreLabel, Label bombLabel) {
		this.scoreLabel = scoreLabel;
		this.bombLabel = bombLabel;
		this.blockPane = blockPane;
		
		this.bombs = 0;
		this.bomb = new Bomb(this);
		this.fever = false;
		
		this.logic = new Logic();
		this.speed = 1000;
		this.available = new ArrayList<Integer>();

		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i);
			block.setOnMouseClicked(new BlockEventHandler(block));
			this.blockPane.getChildren().add(block);
		}
	}
	
	// Press enter to use bomb (if available)
	public void setUpEnterEventHandler(Scene scene) {
		scene.setOnKeyReleased(new EnterEventHandler());
	}
	
	private class EnterEventHandler implements EventHandler<KeyEvent>{
		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER)){
				if (bombs > 0) {
					bomb.usePowerUp();
					bombLabel.setText("Bombs: " + getBombs());
				}
			}
		}
	}
	
	// Handle block clicks
	private class BlockEventHandler implements EventHandler<MouseEvent>{
		private Block block;
		public BlockEventHandler(Block block) {
			this.block = block;
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			if (block.isEmpty() || lose) return;
			Node node = this.block.getCurrentNode();
			if (node instanceof Enemy) {
				Enemy e = (Enemy) node;
				if (!e.takeDamage() || fever) {
					block.clearNode();
					available.add(block.getIndex());
					scoreLabel.setText("Score: " + logic.addScore(100));
				}
			}
			if (node instanceof PowerUp) {
				if (node instanceof Collectible) {
					Collectible c = (Collectible) node;
					if (node instanceof Bomb) {
						if (getBombs() < 3) {
							c.collect();
							bombLabel.setText("Bombs: " + getBombs());
						} else return;
					}
				} else {
					PowerUp p = (PowerUp) node;
					p.usePowerUp();
				}
				if (block.hasRunningTimer()) block.stopTimer();
				block.clearNode();
				available.add(block.getIndex());
			}
		}
	}
	
	// Main game loop (random)
	public void startGameLoop() {
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(15000);
					if (this.speed > 500) {
						this.speed -= 50;
					}
					if (this.lose) break;
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
					if (this.lose) break;
					if (this.available.size() == 0) {
						System.out.println("You Lose");
						this.lose = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t.start();
	}
	
	// Random Algorithm
	private void randomGame() {
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
			if (this.available.size() <= 0) return;
			
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
						int item = randomProb[new Random().nextInt(randomProb.length)];
						if (item <= 5) block.setCurrentNode(new StrongEnemy());
						else if (item == 6) block.setCurrentNodeWithTimer(new Bomb(this),3000);
						else if (item == 7 && !this.fever) 
							block.setCurrentNodeWithTimer(new PowerUp("green") {
								public void usePowerUp() {
									startFever();
								}
							},3000);
						else if (item == 8) block.setCurrentNode(new PowerUp("orange") {
							public void usePowerUp() {
								killAdjacentEnemies(block.getIndex());
							}
						});
					}
				}
			}
		}
	}
	
	// Power Up Storage
	public void addBomb() {
		if (this.bombs < 3) {
			this.bombs++;
		}
	}
	public void deleteBomb() {
		if (this.bombs > 0) {
			this.bombs--;
		}
	}
	public int getBombs() {
		return this.bombs;
	}
	
	// Power Ups
	public void clearBoard() {
		for (Node node : this.blockPane.getChildren()) {
			Block block = (Block) node;
			if (!block.isEmpty()) {
				if (block.getCurrentNode() instanceof Enemy) {
					this.logic.addScore(100);
				}
				block.clearNode();
			}
		}
		scoreLabel.setText("Score: " + this.logic.getScore());
		available.clear();
		for (int i = 0; i < 12; i++) {
			available.add(i);
		}
	}
	public void startFever() {
		Thread t = new Thread(() -> {
			try {
				this.logic.setScoreMultiplier(3);
				this.fever = true;
				Thread.sleep(10000);
				this.logic.setScoreMultiplier(1);
				this.fever = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}
	public void killAdjacentEnemies(int position) {
		int[] kill;
		
		switch (position%3) {
			case 0: kill = new int[]{position-3, position+1, position+3}; break;
			case 1: kill = new int[]{position-3, position-1, position+1, position+3}; break;
			case 2: kill = new int[]{position-3, position-1, position+3}; break;
			default: kill = new int[] {};
		}
		
		for (int k : kill) {
			if (k >=0 && k<12) {
				Block block =  (Block) this.blockPane.getChildren().get(k);
				if (!block.isEmpty()) {
					if (block.getCurrentNode() instanceof Enemy) {
						block.clearNode();
						this.logic.addScore(100);
					} else {
						block.clearNode();
						if (block.hasRunningTimer()) block.stopTimer();
					}
				}
				this.available.add(k);
			}
		}
	}
}
