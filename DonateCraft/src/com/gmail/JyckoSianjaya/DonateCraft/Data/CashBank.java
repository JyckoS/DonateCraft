package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public class CashBank {
	private static CashBank instance;
	private HashMap<UUID, Cash> Cashes = new HashMap<UUID, Cash>();
	HashMap<Integer, String> cachecash = new HashMap<Integer, String>();
	private ArrayList<String> TOP_CASH_BALANCES = new ArrayList<String>();
	private CashBank() {
		LoadFiles();
		resetCashBalance();
		new BukkitRunnable() {
			@Override
			public void run() {
				resetCashTopBalance();
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 3600L, 3600L);
	}
	public static CashBank getInstance() {
		if (instance == null) {
			instance = new CashBank();
		}
		return instance;
	}
	public Boolean hasCash(Player p) {
		return (Cashes.containsKey(p.getUniqueId()));
	}
	public Cash getCash(Player p) {
		return Cashes.get(p.getUniqueId());
	}
	public Cash getCash(UUID uuid) {
		return Cashes.get(uuid);
	}
	public void setCash(Player p, Cash cash) {
		Cashes.put(p.getUniqueId(), cash);
	}
	public void setCash(UUID uuid, Cash cash) {
		Cashes.put(uuid, cash);
	}
	public void LoadFiles() {
		File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata" + File.separator);
		File[] cashfiles = folder.listFiles();
		for (File file : cashfiles) {
			String name = file.getName().replaceAll(".yml", "");
			UUID uuid = UUID.fromString(name);
			YamlConfiguration cashs = YamlConfiguration.loadConfiguration(file);
			int cash = cashs.getInt("cash");
			Cash cas = new Cash(cash);
			setCash(uuid, cas);
		}
	}
	public void resetCashTopBalance() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getCash(p) == null) {
				continue;
			}
			if (getCash(p).getCashAmount() <= 0) {
				continue;
			}
		}
		TOP_CASH_BALANCES.clear();
		File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Integer> caches = new ArrayList<Integer>();
		for (File ss : folder.listFiles()) {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			UUID uuid = UUID.fromString(yml.getString("uuid"));
			int acs = yml.getInt("cash");
			String owner = Bukkit.getOfflinePlayer(uuid).getName();
			caches.add(acs);
		}
		Collections.sort(caches);
		Collections.reverse(caches);
		for (int ie : caches) {
			addTop(ie);
		}
	}
	public void resetCashBalance() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getCash(p) == null) {
				continue;
			}
			if (getCash(p).getCashAmount() <= 0) {
				continue;
			}
		}
		cachecash.clear();
		TOP_CASH_BALANCES.clear();
		File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Integer> caches = new ArrayList<Integer>();
		for (File ss : folder.listFiles()) {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			UUID uuid = UUID.fromString(yml.getString("uuid"));
			int acs = yml.getInt("cash");
			String owner = Bukkit.getOfflinePlayer(uuid).getName();
			addCache(owner, acs);
			caches.add(acs);
		}
		Collections.sort(caches);
		Collections.reverse(caches);
		for (int ie : caches) {
			addTop(ie);
		}
	}
	private void addTop(int key) {
		this.TOP_CASH_BALANCES.add(cachecash.get(key) + "~" + key);
	}
	private void addCache(String u, int amount) {
		this.cachecash.put(amount, u);
	}
	public ArrayList<String> getTopBalance() {
		return this.TOP_CASH_BALANCES;
	}
	public String getTopBalances(int index) {
		return this.TOP_CASH_BALANCES.get(index);
	}
}
