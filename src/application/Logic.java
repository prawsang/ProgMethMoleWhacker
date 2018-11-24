package application;

public class Logic {
	private int score;
	private int scoreMultiplier;
	
	public Logic() {
		scoreMultiplier = 1;
	}
	
	public int addScore(int score) {
		this.score += score * scoreMultiplier;
		return this.score;
	}
	public int getScore() {
		return this.score;
	}
	public int getScoreMultiplier() {
		return scoreMultiplier;
	}
	public void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
	}
}
