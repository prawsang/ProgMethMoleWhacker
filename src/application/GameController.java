package application;

import java.util.ArrayList;

import component.Block;
import component.BlockPane;
import component.BombPane;
import item.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import logic.RandomLogic;
import logic.ScoreLogic;

public class GameController {

	private BlockPane blockPane;
	private Label scoreLabel;
	private BombPane bombPane;
	private ScoreLogic logic;
	private RandomLogic randomLogic;
	private GraphicsContext effects;
	
	private boolean lose;
	
	private int speed;
	public static ArrayList<Integer> available;
	
	//PowerUps
	private int bombs;
	private Bomb bomb;
	private boolean fever;
	private GraphicsContext feverEffect;
	
	public GameController(
			BlockPane blockPane, 
			Label scoreLabel, 
			BombPane bombPane,
			GraphicsContext effects,
			GraphicsContext feverEffects
	) {
		this.scoreLabel = scoreLabel;
		
		this.bombPane = bombPane;
		this.blockPane = blockPane;
		
		this.effects = effects;
		
		this.bombs = 0;
		this.bomb = new Bomb(this);
		this.fever = false;
		this.feverEffect = feverEffects;
		
		this.logic = new ScoreLogic();
		this.randomLogic = new RandomLogic();
		this.speed = 1000;
		available = new ArrayList<Integer>();

		// Add blocks to block pane
		Image hit = new Image(Resources.HIT);
		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i, blockPane);
			block.setOnMouseClicked(new BlockEventHandler(block));
			
			// Hit effects
			block.setOnMousePressed((e) -> {
				if (block.getCurrentItem() instanceof Enemy) {	
					effects.drawImage(
							hit, 
							e.getSceneX()- hit.getWidth()/4, 
							e.getSceneY()-hit.getHeight()/4, 
							hit.getWidth()/2, 
							hit.getHeight()/2
						);
				}
			});
			block.setOnMouseReleased((e) -> {
				effects.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			});
			
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
					
					Image boom = new Image(Resources.BIGBOOM);
					effects.drawImage(
							boom, 
							(Main.WIDTH - boom.getWidth()/2)/2, 
							(Main.HEIGHT - boom.getHeight()/2)/2,
							boom.getWidth()/2,
							boom.getHeight()/2
						);
					startEffectsTimer();
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
	
	// Random
	private void randomGame() {
		int itemsAtOnce = randomLogic.randomItemsAtOnce(this.speed);
		for (int i = 0; i < itemsAtOnce; i++) {
			int position = randomLogic.randomPosition(available);
			Block block = (Block) blockPane.getTiles().getChildren().get(position);
			int randomItem = randomLogic.randomItem(available, this.speed, this.fever);
			Item item;
			switch (randomItem) {
				case 0: item = new NormalEnemy();
				case 1: item = new StrongEnemy();
				case 2: item = new Bomb(this);
				case 3: item = new PowerUp(Resources.FEVERSTAR) {
					public void usePowerUp() {
						startFever();
					}
				};
				case 4: item = new PowerUp(Resources.DYNAMITE) {
					public void usePowerUp() {
						killAdjacentEnemies(position);
					}
				};
				default: item = new NormalEnemy();
			}
			if (item instanceof PowerUp) {
				block.setCurrentItemWithTimer(item, 3000);
			} else {
				block.setCurrentItem(item);
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
		this.logic.setScoreMultiplier(3);
		this.fever = true;
		this.feverEffect.drawImage(new Image(Resources.FEVER), 0, 0, Main.WIDTH, Main.HEIGHT);
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(10000);
				this.logic.setScoreMultiplier(1);
				this.fever = false;
				this.feverEffect.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
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
					block.clearNode();
					if (block.getCurrentItem() instanceof Enemy) {
						this.logic.addScore(100);
					} else {
						if (block.hasRunningTimer()) block.stopTimer();
					}
					available.add(k);
					effects.drawImage(
							new Image(Resources.BOOM), 
							blockPane.getX(k) + 53, 
							blockPane.getY(k) + 110, 
							Main.BLOCKSIZE, 
							Main.BLOCKSIZE
						);
				}
			}
		}
		effects.drawImage(
				new Image(Resources.BOOM), 
				blockPane.getX(position) + 53, 
				blockPane.getY(position) + 110, 
				Main.BLOCKSIZE, 
				Main.BLOCKSIZE
			);
		startEffectsTimer();
	}
	
	// Effects
	private void startEffectsTimer() {
		Thread t = new Thread(()->  {
			try {
				Thread.sleep(500);
				effects.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
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

