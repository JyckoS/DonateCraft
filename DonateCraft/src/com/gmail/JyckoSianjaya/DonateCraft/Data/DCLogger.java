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

public class DCLogger {
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
	public static DCLogger getInstance() {
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
	public void InitializeStuff() throws IOException  {
		Utility.sendConsole("[DC] Reinitialized Logs!");
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		String cday = dateformat.format(date);
		logfile = new File(DonateCraft.getInstance().getDataFolder(), "logs" + File.separator + cday + "_DonateLogs.txt");
		if (!logfile.exists()) {
			logfile.createNewFile();
		}
		fw = new FileWriter(logfile, true);
		pw = new PrintWriter(fw);
	}
	public void LogMessage(String message) {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		String ctime = sd.format(date);
		pw.write(ctime + ":" + message + "\n");
		pw.flush();
	}
}
