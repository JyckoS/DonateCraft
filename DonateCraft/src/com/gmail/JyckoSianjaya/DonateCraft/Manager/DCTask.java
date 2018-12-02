package com.gmail.JyckoSianjaya.DonateCraft.Manager;

public interface DCTask {
	int liveticks = 0;
	public void runTask();
	public void reduceTicks();
	public int getLiveTicks();
}
