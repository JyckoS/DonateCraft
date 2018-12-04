package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class DCLogger {
	private File logfile;
	private FileWriter fw;
	private PrintWriter pw;
	private ACCashBank acb = ACCashBank.getInstance();
	private CashBank cb = CashBank.getInstance();
	private static DCLogger instance;
	private DCLogger() throws IOException {
		InitializeStuff();
		new BukkitRunnable() {
			@Override
			public void run() {
					try {
						InitializeStuff();
						cb.resetCashBalance();
						acb.resetACashBalance();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO Auto-generated catch block
			}
		}.runTaskTimer(DonateCraft.getInstance(), 72000L, 72000L);
		}
	public final static DCLogger getInstance() {
		if (instance == null) {
			try {
				instance = new DCLogger();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	public final void InitializeStuff() throws IOException  {
		Utility.sendConsole("[DC] Reinitialized Logs!");
		final Date date = new Date();
		final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		final String cday = dateformat.format(date);
		logfile = new File(DonateCraft.getInstance().getDataFolder(), "logs" + File.separator + cday + "_DonateLogs.txt");
		if (!logfile.exists()) {
			logfile.createNewFile();
		}
		fw = new FileWriter(logfile, true);
		pw = new PrintWriter(fw);
	}
	public final void LogMessage(final String message) {
		final Date date = new Date();
		final SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		final String ctime = sd.format(date);
		pw.write(ctime + ":" + message + "\n");
		pw.flush();
	}
}
