package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.util.ArrayList;

public class NumberStorage {
	private static NumberStorage instance;
	private RandomResult results = new RandomResult(new ArrayList<String>());
	public RandomResult getResults() { return results; }
	private NumberStorage() {
		results.addPossibility("A");

		results.addPossibility("B");

		results.addPossibility("C");

		results.addPossibility("D");

		results.addPossibility("E");

		results.addPossibility("F");

		results.addPossibility("G");

		results.addPossibility("H");

		results.addPossibility("I");

		results.addPossibility("J");

		results.addPossibility("K");

		results.addPossibility("L");
		
		results.addPossibility("M");

		results.addPossibility("N");

		results.addPossibility("O");

		results.addPossibility("P");

		results.addPossibility("Q");
		results.addPossibility("R");

		results.addPossibility("S");

		results.addPossibility("T");

		results.addPossibility("U");

		results.addPossibility("V");

		results.addPossibility("W");

		results.addPossibility("X");

		results.addPossibility("Y");

		results.addPossibility("Z");

		results.addPossibility("0");

		results.addPossibility("1");

		results.addPossibility("2");

		
		results.addPossibility("3");

		results.addPossibility("4");

		results.addPossibility("5");

		results.addPossibility("6");
		results.addPossibility("7");

		
		results.addPossibility("8");

		results.addPossibility("9");

		
	}
	public static NumberStorage getInstance() { if (instance == null) instance = new NumberStorage(); return instance; }
}
