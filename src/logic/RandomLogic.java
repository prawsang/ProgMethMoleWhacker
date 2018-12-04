package logic;

import java.util.ArrayList;
import java.util.Random;

public class RandomLogic {
	
	public static final int[] randomProb = {0,1,2,3,4,5,6,7,8};
	
	public int randomPosition(ArrayList<Integer> available) {
		return new Random().nextInt(available.size());
	}
	
	public int randomItemsAtOnce(int speed) {
		int itemsAtOnce = 1;
		int random = randomProb[new Random().nextInt(randomProb.length)];
		
		if (speed < 1000) {
			if (random <= 5) {
				itemsAtOnce = 1;
			} else {
				itemsAtOnce = 2;
			}
		} else if (speed < 800) {
			if (random <= 4) {
				itemsAtOnce = 1;
			} else if (random >= 7) {
				itemsAtOnce = 3;
			} else {
				itemsAtOnce = 2;
			}
		}
		return itemsAtOnce;
	}
	
	public int randomItem(ArrayList<Integer> available, int speed, boolean fever) {
		int position = new Random().nextInt(available.size());

		int random = randomProb[new Random().nextInt(randomProb.length)];
		available.remove(position);
				
		if (speed >= 900) {
			// Normal Enemy
			return 0;
		} else {		
			if (random <= 6) {
				return 0;
			} else {
				int item = randomProb[new Random().nextInt(randomProb.length)];
				if (item <= 5) {
					// Strong Enemy
					return 1;
				}
				else if (item == 6) {
					// Bomb
					return 2;
				}
				else if (item == 7 && !fever) {
					// Fever
					return 3;
				}
				else {
					// Kill Adjacent Enemies (Dynamite)
					return 4;
				}
			}
		}
	}
}