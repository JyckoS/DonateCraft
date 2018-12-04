package com.gmail.JyckoSianjaya.DonateCraft.Manager;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;

public final  class DCRunnable {
	private static DCRunnable instance;
	private final ArrayList<DCTask> tasks = new ArrayList<DCTask>();
	private DCRunnable() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				for (final DCTask task : new ArrayList<DCTask>(tasks)) {
					if (task.getLiveTicks() < 1) {
						removeTask(task);
						continue;
					}
					task.runTask();
					task.reduceTicks();
				}
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 1L, 1L);
	}
	public final static DCRunnable getInstance() {
		if (instance == null) instance = new DCRunnable();
		return instance;
	}
	public final void addTask(DCTask task) {
		tasks.add(task);
	}
	public final void removeTask(DCTask task) {
		tasks.remove(task);
	}
}
