package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.util.ArrayList;
import java.util.Random;

public class RandomResult extends Object {
	private ArrayList<String> possibilities = new ArrayList<String>();
	public RandomResult(ArrayList<String> possibilities) {
		this.possibilities = possibilities;
	}
	public void addPossibility(String chars) {
		possibilities.add(chars);
	}
	public ArrayList<String> getPossibilities() { return this.possibilities; }
	public String getRandom() {
		Random rand = new Random();
		int am = rand.nextInt(possibilities.size());
		return possibilities.get(am);
	}
}
