package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;

public class ACCashBank {
	private static ACCashBank instance;
	private HashMap<UUID, ACWallet> accs = new HashMap<UUID, ACWallet>();
	HashMap<Integer, String> cacheaccash = new HashMap<Integer, String>();
	private ArrayList<String> TOP_ACASH_BALANCES = new ArrayList<String>();
	private ACCashBank() {
		this.resetACashBalance();
		new BukkitRunnable() {
			@Override
			public void run() {
				resetTOPACashBalance();
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 3600L, 3600L);
	}
	public static ACCashBank getInstance() {
		if (instance == null) {
			instance = new ACCashBank();
		}
		return instance;
	}
	public void setACWallet(Player p, ACWallet ac) {
		this.accs.put(p.getUniqueId(), ac);
	}
	public void setACWallet(UUID uuid, ACWallet ac) {
		this.accs.put(uuid, ac);
	}
	public Boolean hasWallet(Player p) {
		return accs.containsKey(p.getUniqueId());
	}
	public ACWallet getACWallet(Player p) {
		return accs.get(p.getUniqueId());
	}
	public ACWallet getACWallet(UUID uuid) {
		return accs.get(uuid);
	}
	public void resetTOPACashBalance() {
		TOP_ACASH_BALANCES.clear();
		File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Integer> caches = new ArrayList<Integer>();
		for (File ss : folder.listFiles()) {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			UUID uuid = UUID.fromString(yml.getString("uuid"));
			int acs = yml.getInt("ac-cash");
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
	public void resetACashBalance() {
		cacheaccash.clear();
		TOP_ACASH_BALANCES.clear();
		File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Integer> caches = new ArrayList<Integer>();
		for (File ss : folder.listFiles()) {
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			UUID uuid = UUID.fromString(yml.getString("uuid"));
			int acs = yml.getInt("ac-cash");
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
		this.TOP_ACASH_BALANCES.add(cacheaccash.get(key) + "~" + key);
	}
	private void addCache(String u, int amount) {
		this.cacheaccash.put(amount, u);
	}
	public ArrayList<String> getTopBalance() {
		return this.TOP_ACASH_BALANCES;
	}
	public String getTopBalances(int index) {
		return this.TOP_ACASH_BALANCES.get(index);
	}
}
