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
	private BombPane bombPane;
	private ScoreLogic logic;
	
	private boolean lose;
	
	// Random Algorithm
	private int speed;
	private static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	public static ArrayList<Integer> available;
	
	//PowerUps
	private int bombs;
	private Bomb bomb;
	private boolean fever;
	
	public GameController(
			BlockPane blockPane, 
			Label scoreLabel, 
			BombPane bombPane
	) {
		this.scoreLabel = scoreLabel;
		
		this.bombPane = bombPane;
		this.blockPane = blockPane;
		
		this.bombs = 0;
		this.bomb = new Bomb(this);
		this.fever = false;
		
		this.logic = new ScoreLogic();
		this.speed = 1000;
		available = new ArrayList<Integer>();

		// Add blocks to block pane
		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i);
			block.setOnMouseClicked(new BlockEventHandler(block));
			this.blockPane.getTiles().getChildren().add(block);
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
					bombPane.drawBombPane(getBombs());
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
			Item item = this.block.getCurrentItem();
			if (item instanceof Enemy) {
				// Take damage
				Enemy e = (Enemy) item;
				if (!e.takeDamage() || fever) {
					scoreLabel.setText(Integer.toString(logic.addScore(100)));
					blockPane.clearGC(block.getIndex());
					block.clearNode();
					available.add(block.getIndex());
				}
			}
			if (item instanceof PowerUp) {
				// Collect or use power up
				if (item instanceof Collectible) {
					Collectible c = (Collectible) item;
					if (item instanceof Bomb) {
						if (getBombs() < 3) {
							c.collect();
							bombPane.drawBombPane(getBombs());
						} else return;
					}
				} else {
					PowerUp p = (PowerUp) item;
					p.usePowerUp();
				}
				if (block.hasRunningTimer()) block.stopTimer();
				blockPane.clearGC(block.getIndex());
				block.clearNode();
				available.add(block.getIndex());
			}
		}
	}
	
	// Main game loop
	public void startGameLoop() {
		// Increases Speed
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(15000);
					if (this.speed > 600) {
						this.speed -= 50;
					}
					if (this.speed == 600) break;
					if (this.lose) break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		speedThread.start();
		
		// Random
		Thread t = new Thread(() ->  {
			while(true) {
				try {
					Thread.sleep(this.speed);
					Platform.runLater(() -> {
						this.randomGame();
					});
					if (this.lose) break;
					if (available.size() == 0) {
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
			if (available.size() <= 0) return;
			
			int position = new Random().nextInt(available.size());
			Block block = (Block) this.blockPane.getTiles().getChildren().get(available.get(position));

			random = randomProb[new Random().nextInt(randomProb.length)];
			available.remove(position);
				
			if (this.speed >= 900) {
				block.setCurrentItem(new NormalEnemy());
			} else {		
				if (random <= 6) {
					block.setCurrentItem(new NormalEnemy());
				} else {
					int item = randomProb[new Random().nextInt(randomProb.length)];
					if (item <= 5) {
						block.setCurrentItem(new StrongEnemy());
					}
					else if (item == 6) {
						block.setCurrentItemWithTimer(new Bomb(this),3000);
						blockPane.drawGC(block.getIndex(), block.getCurrentItem().getImage());
					}
					else if (item == 7 && !this.fever) {
						// Fever
						System.out.println("fever");
						block.setCurrentItemWithTimer(new PowerUp(Resources.FEVERSTAR) {
							public void usePowerUp() {
								startFever();
							}
						},3000);
					}
					else {
						block.setCurrentItem(new PowerUp(Resources.DYNAMITE) {
							// Kill Adjacent Enemies
							public void usePowerUp() {
								killAdjacentEnemies(block.getIndex());
							}
						});
					}
				}
			}
			blockPane.drawGC(block.getIndex(), block.getCurrentItem().getImage());
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
		for (Node node : this.blockPane.getTiles().getChildren()) {
			Block block = (Block) node;
			if (!block.isEmpty()) {
				if (block.getCurrentItem() instanceof Enemy) {
					this.logic.addScore(100);
				}
				block.clearNode();
				available.add(block.getIndex());
			}
		}
		scoreLabel.setText(Integer.toString(this.logic.getScore()));
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
				Block block =  (Block) this.blockPane.getTiles().getChildren().get(k);
				if (!block.isEmpty()) {
					blockPane.clearGC(block.getIndex());
					block.clearNode();
					if (block.getCurrentItem() instanceof Enemy) {
						this.logic.addScore(100);
					} else {
						if (block.hasRunningTimer()) block.stopTimer();
					}
					available.add(k);
				}
			}
		}
	}
	
	// Debug
	private void printAvailable() {
		int[] a = {1,1,1,1,1,1,1,1,1,1,1,1};
		for (int i : available) {
			a[i] = 0;
		}
		for (int j = 0; j < a.length; j++) {
			System.out.print(a[j]);
			if ((j+1) % 3 == 0 ) System.out.println("");
		}
		System.out.println("========");
	}
	private void colorAvailable() {
		for (Node n : blockPane.getTiles().getChildren()) {
			n.setStyle("-fx-background: none");
		}
		for (int i : available) {
			blockPane.getChildren().get(i).setStyle("-fx-background-color: #888");
		}
	}
}

