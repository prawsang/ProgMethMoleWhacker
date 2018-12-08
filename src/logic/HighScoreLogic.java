package logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HighScoreLogic {
	
	private static Path path = Paths.get("highscore.txt");
	private static int highscore;
	
	private static void getFile() {
		path = Paths.get("highscore.txt");
	}
	
	private static void createNewFile() {
		try {
		    Files.createFile(path);
		} catch (IOException ignored) {
		}
	}
	
	public static void writeHighScore(int score) {
		getFile();
		
		String fileContent = new String(); 
		if (score > getHighScore()) {
			fileContent = Integer.toString(score);
		} else return;
		
		try {
			Files.write(path, fileContent.getBytes());
		} catch (IOException e) {
			createNewFile();
			writeHighScore(score);
		}
	}
	
	public static void resetHighScore() throws IOException {
		getFile();
		
		String fileContent = new String("0"); 
		
		try {
			Files.write(path, fileContent.getBytes());
		} catch (IOException e) {
			createNewFile();
			resetHighScore();
		}
	}
	
	private static void readHighScore() {
		getFile();
		List<String> lines;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			highscore = 0;
			createNewFile();
			writeHighScore(0);
			return;
		}
		
		if (lines.isEmpty()) {
			lines.add("0");
		}
		try {
			highscore = Integer.parseInt(lines.get(0));
		} catch (NumberFormatException e) {
			highscore = 0;
		}
	}
	
	public static int getHighScore() {
		readHighScore();
		return highscore;
	}
}
