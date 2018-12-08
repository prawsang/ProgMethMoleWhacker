package application;

public class Resources {
	
	// IMAGES
	public static final String BACKGROUND = ClassLoader.getSystemResource("images/background.png").toString();
	public static final String FRAME = ClassLoader.getSystemResource("images/frame.png").toString();
	public static final String SCORE = ClassLoader.getSystemResource("images/score_background.png").toString();
	public static final String FEVER = ClassLoader.getSystemResource("images/fever.png").toString();
	
	public static final String NORMAL_ENEMY = ClassLoader.getSystemResource("images/enemy.png").toString();
	public static final String STRONG_ENEMY = ClassLoader.getSystemResource("images/strong_enemy.png").toString();
	public static final String FEVER_STAR = ClassLoader.getSystemResource("images/fever_star.png").toString();
	public static final String STREAK_STAR = ClassLoader.getSystemResource("images/streak_star.png").toString();
	public static final String BOMB = ClassLoader.getSystemResource("images/bomb.png").toString();
	public static final String DYNAMITE = ClassLoader.getSystemResource("images/dynamite.png").toString();
	
	public static final String BOMBS_0 = ClassLoader.getSystemResource("images/bombs_0.png").toString();
	public static final String BOMBS_1 = ClassLoader.getSystemResource("images/bombs_1.png").toString();
	public static final String BOMBS_2 = ClassLoader.getSystemResource("images/bombs_2.png").toString();
	public static final String BOMBS_3 = ClassLoader.getSystemResource("images/bombs_3.png").toString();
	
	public static final String BIG_BOOM = ClassLoader.getSystemResource("images/big_boom.png").toString();
	public static final String BOOM = ClassLoader.getSystemResource("images/boom.png").toString();
	public static final String HIT = ClassLoader.getSystemResource("images/hit.png").toString();
	
	// SOUNDS
	public static final String SONG = ClassLoader.getSystemResource("audio/song.m4a").toString();
	public static final String BOMB_SOUND = ClassLoader.getSystemResource("audio/bombSound.m4a").toString();
	public static final String DIE = ClassLoader.getSystemResource("audio/getHit.m4a").toString();
	public static final String COLLECT_BOMB = ClassLoader.getSystemResource("audio/collectBomb.m4a").toString();
	public static final String HIT_SOUND = ClassLoader.getSystemResource("audio/hit.m4a").toString();
}
