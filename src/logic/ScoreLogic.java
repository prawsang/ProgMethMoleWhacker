package logic;

public class ScoreLogic {
	private int score;
	private int scoreMultiplier;
	
	public ScoreLogic() {
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
	public int resetScore() {
		this.score = 0;
		return this.score;
	}
}
