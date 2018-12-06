package application;

import java.io.IOException;
import java.util.ArrayList;

import component.Block;
import component.BlockPane;
import component.BombPane;
import component.ScoreLabel;
import component.HighScoreLabel;

import item.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import logic.*;

public class GameController {
	
	// Components
	private BlockPane blockPane;
	private ScoreLabel scoreLabel;
	private BombPane bombPane;
	private HighScoreLabel highScoreLabel;
	
	// Logic
	private ScoreLogic scoreLogic;
	private RandomLogic randomLogic;
	private boolean running = false;
	private int speed;
	public static ArrayList<Integer> available;
	
	// Effects
	private GraphicsContext effects;
	private GraphicsContext feverEffect;
	
	//PowerUps
	private int bombs;
	private Bomb bomb;
	private boolean fever;
	
	// BGM
	private Thread bgSoundThread;
	private AudioClip sound = new AudioClip(Resources.SONG);
	
	public GameController(
			BlockPane blockPane, 
			ScoreLabel scoreLabel, 
			BombPane bombPane,
			HighScoreLabel highScoreLabel,
			GraphicsContext effects,
			GraphicsContext feverEffects
	) {
		this.scoreLabel = scoreLabel;
		
		this.bombPane = bombPane;
		this.blockPane = blockPane;
		this.highScoreLabel = highScoreLabel;
		
		this.effects = effects;
		
		this.bombs = 0;
		this.bomb = new Bomb(this);
		this.fever = false;
		this.feverEffect = feverEffects;
		
		this.scoreLogic = new ScoreLogic();
		this.randomLogic = new RandomLogic();
		this.speed = Constants.MAXINTERVAL;
		available = new ArrayList<Integer>();

		// Add blocks to block pane
		Image hit = new Image(Resources.HIT);
		for (int i = 0; i < 12; i++) {
			available.add(i);
			
			Block block = new Block(i, blockPane);
			block.setOnMouseClicked(new BlockEventHandler(block));
			
			// Hit effects
			block.setOnMousePressed((e) -> {
				if (block.getCurrentItem() instanceof Enemy && !block.isEmpty()) {	
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
				effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
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
					
					AudioClip useBombSound = new AudioClip(Resources.BOMBSOUND);
					useBombSound.play();
					
					Image boom = new Image(Resources.BIGBOOM);
					effects.drawImage(
							boom, 
							(Constants.WIDTH - boom.getWidth()/2)/2, 
							(Constants.HEIGHT - boom.getHeight()/2)/2,
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
			if (block.isEmpty() || !running) return;
			Item item = this.block.getCurrentItem();
			if (item instanceof Enemy) {
				// Take damage
				Enemy e = (Enemy) item;
				if (!e.takeDamage() || fever) {
					scoreLabel.setText(Integer.toString(scoreLogic.addScore(100)));
					block.clearNode();
					available.add(block.getIndex());	
					AudioClip getHitSound = new AudioClip(Resources.GETHIT);
					getHitSound.play();
				}
				AudioClip hitSound = new AudioClip(Resources.HITSOUND);
				hitSound.play();
			}
			if (item instanceof PowerUp) {
				// Collect or use power up
				if (item instanceof Collectible) {
					Collectible c = (Collectible) item;
					AudioClip collectItem  = new AudioClip(Resources.COLLECTBOMB);
					collectItem.play();
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
		this.running = true;
		// Increases Speed
		Thread speedThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(10000);
					if (this.speed > Constants.MININTERVAL) {
						this.speed -= Constants.INTERVALSTEP;
					}
					if (this.speed == Constants.MININTERVAL) break;
					if (!this.running) break;
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
					if (!this.running) break;
					if (available.size() == 0) {
						Platform.runLater(() -> {
							gameOver();
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t.start();
		
		bgSoundThread = new Thread(() -> {
				try {
					while(true) {
						sound.play();
						Thread.sleep(480000);
					
					}
				} catch (InterruptedException e){
					sound.stop();
					e.printStackTrace();
				}
			
		} );
		bgSoundThread.start();
	}
	
	// Reset Values
	public void resetValues() {
		clearBoard();
		this.effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.feverEffect.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		this.speed = Constants.MAXINTERVAL;
		this.scoreLabel.setText(Integer.toString(this.scoreLogic.resetScore()));
		this.fever = false;
		this.bombs = 0;
		this.bombPane.drawBombPane(0);
	}
	
	// Game Over
	private void gameOver() {
		this.running = false;
		this.fever = false;
		bgSoundThread.interrupt();
		Main.showGameOver(scoreLogic.getScore());
		try {
			HighScoreLogic.writeHighScore(scoreLogic.getScore());
		} catch (IOException e) {
			e.printStackTrace();
		}
		highScoreLabel.setHighScoreText(HighScoreLogic.getHighScore());
	}
	
	// Random
	private void randomGame() {
		if (available.size() <= 0) return;
		
		int itemsAtOnce = randomLogic.randomItemsAtOnce(this.speed);
		if (itemsAtOnce > available.size()) itemsAtOnce = available.size();
		
		for (int i = 0; i < itemsAtOnce; i++) {
			int position = randomLogic.randomPosition(available);
			Block block = (Block) blockPane.getTiles().getChildren().get(available.get(position));
			available.remove(position);
			
			int randomItem = randomLogic.randomItem(available, this.speed, this.fever);
			Item item;
			switch (randomItem) {
				case 0: item = new NormalEnemy(); break;
				case 1: item = new StrongEnemy(); break;
				case 2: item = new Bomb(this); break;
				case 3: item = new PowerUp(Resources.FEVERSTAR) {
					public void usePowerUp() {
						startFever();
					}
				}; break;
				case 4: item = new PowerUp(Resources.DYNAMITE) {
					public void usePowerUp() {
						killAdjacentEnemies(block.getIndex());
						AudioClip useBombSound = new AudioClip(Resources.BOMBSOUND);
						useBombSound.play();
					}
				}; break;
				default: item = new NormalEnemy();
			}
			if (randomItem == 2 || randomItem == 3) {
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
					this.scoreLogic.addScore(100);
				}
				block.clearNode();
				available.add(block.getIndex());
			}
		}
		scoreLabel.setText(Integer.toString(this.scoreLogic.getScore()));
	}
	public void startFever() {
		this.scoreLogic.setScoreMultiplier(3);
		this.fever = true;
		this.feverEffect.drawImage(new Image(Resources.FEVER), 0, 0, Constants.WIDTH, Constants.HEIGHT);
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(10000);
				this.scoreLogic.setScoreMultiplier(1);
				this.fever = false;
				this.feverEffect.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
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
						this.scoreLogic.addScore(100);
					} else {
						if (block.hasRunningTimer()) block.stopTimer();
					}
					available.add(k);
					effects.drawImage(
							new Image(Resources.BOOM), 
							blockPane.getX(k) + 53, 
							blockPane.getY(k) + 110, 
							Constants.BLOCKSIZE, 
							Constants.BLOCKSIZE
						);
				}
			}
		}
		effects.drawImage(
				new Image(Resources.BOOM), 
				blockPane.getX(position) + 53, 
				blockPane.getY(position) + 110, 
				Constants.BLOCKSIZE, 
				Constants.BLOCKSIZE
			);
		startEffectsTimer();
	}
	
	// Effects
	private void startEffectsTimer() {
		Thread t = new Thread(()->  {
			try {
				Thread.sleep(500);
				effects.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
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

