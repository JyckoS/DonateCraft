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

public final class ACCashBank {
	private static ACCashBank instance;
	private final HashMap<UUID, ACWallet> accs = new HashMap<UUID, ACWallet>();
	private final HashMap<Integer, String> cacheaccash = new HashMap<Integer, String>();
	private final ArrayList<String> TOP_ACASH_BALANCES = new ArrayList<String>();
	private ACCashBank() {
		this.resetACashBalance();
		new BukkitRunnable() {
			@Override
			public void run() {
				synchronized(this) {
				resetTOPACashBalance();
				}
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 3600L, 3600L);
	}
	public static final ACCashBank getInstance() {
		if (instance == null) {
			instance = new ACCashBank();
		}
		return instance;
	}
	public final void removeACWallet(UUID uuid) {
		accs.remove(uuid);
	}
	public final void setACWallet(final Player p, final ACWallet ac) {
		this.accs.put(p.getUniqueId(), ac);
	}
	public final void setACWallet(final UUID uuid, final ACWallet ac) {
		this.accs.put(uuid, ac);
	}
	public final Boolean hasWallet(final Player p) {
		return accs.containsKey(p.getUniqueId());
	}
	public final ACWallet getACWallet(final Player p) {
		return accs.get(p.getUniqueId());
	}
	public final Boolean hasWallet(final UUID p) {
		return accs.containsKey(p);
	}
	public final ACWallet getACWallet(final UUID uuid) {
		return accs.get(uuid);
	}
	public final void resetTOPACashBalance() {
		if (DataStorage.getInstance().useSQL()) return;
		TOP_ACASH_BALANCES.clear();
		final File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		final ArrayList<Integer> caches = new ArrayList<Integer>();
		final HashMap<Integer, ArrayList<String>> owners = new HashMap<Integer, ArrayList<String>>();
		for (final File ss : folder.listFiles()) {
			final YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			final UUID uuid = UUID.fromString(yml.getString("uuid"));
			final int acs = yml.getInt("ac-cash");
			final String owner = yml.getString("nick");
			accs.put(uuid, new ACWallet(acs));
			if (acs == 0) continue;
			addCache(owner, acs);
			if (!caches.contains(acs)) {
			caches.add(acs);
			}
			if (owners.containsKey(acs)) {
				ArrayList<String> ownerz = owners.get(acs);
				ownerz.add(owner);
				owners.put(acs, ownerz);
				continue;
			}
			ArrayList<String> aa = new ArrayList<String>();
			aa.add(owner);
			owners.put(acs, aa);
		}
		Collections.sort(caches);
		Collections.reverse(caches);
		for (int ie : caches) {
			for (String str : owners.get(ie)) {
			addTop(ie, str);
			}
		}
	}
	public void resetACashBalance() {
		if (DataStorage.getInstance().useSQL()) return;
		cacheaccash.clear();
		TOP_ACASH_BALANCES.clear();
		final File folder = new File(DonateCraft.getInstance().getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		final ArrayList<Integer> caches = new ArrayList<Integer>();
		final HashMap<Integer, ArrayList<String>> owners = new HashMap<Integer, ArrayList<String>>();
		for (final File ss : folder.listFiles()) {
			final YamlConfiguration yml = YamlConfiguration.loadConfiguration(ss);
			final UUID uuid = UUID.fromString(yml.getString("uuid"));
			final int acs = yml.getInt("ac-cash");
			final String owner = yml.getString("cash");
			if (acs == 0) continue;
			addCache(owner, acs);
			if (!caches.contains(acs)) {
			caches.add(acs);
			}
			
			accs.put(uuid, new ACWallet(acs));
			if (owners.containsKey(acs)) {
				ArrayList<String> ownerz = owners.get(acs);
				ownerz.add(owner);
				owners.put(acs, ownerz);
				continue;
			}
			ArrayList<String> owns = new ArrayList<String>();
			owns.add(owner);
			owners.put(acs, owns);
		}
		Collections.sort(caches);
		Collections.reverse(caches);
		for (int ie : caches) {
			for (String str : owners.get(ie)) {
			addTop(ie, str);
			}
		}
	}
	private final void addTop(final int key, final String owner) {
		this.TOP_ACASH_BALANCES.add(owner + "~" + key);
	}
	private final void addCache(final String u, final int amount) {
		this.cacheaccash.put(amount, u);
	}
	public final ArrayList<String> getTopBalance() {
		return this.TOP_ACASH_BALANCES;
	}
	public final String getTopBalances(final int index) {
		return this.TOP_ACASH_BALANCES.get(index);
	}
}
