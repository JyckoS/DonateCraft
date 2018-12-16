package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class PlayerData {
	private static PlayerData instance;
	private final DonateCraft MainInst = DonateCraft.getInstance();
	private final CashBank cbank = CashBank.getInstance();
	private final ACCashBank acbank = ACCashBank.getInstance();
	private PlayerData() {
	}
	public final static PlayerData getInstance() {
		if (instance == null) {
			instance = new PlayerData();
		}
		return instance;
	}
	public final void setData(final Player p, final Cash cash) {
		DCRunnable.getInstance().addTask(new DCTask(){
			int health = 1;
			@Override
			public void runTask() {
		final File f = new File(MainInst.getDataFolder(), "playerdata" + File.separator +  p.getUniqueId() + ".yml");
		final YamlConfiguration file = YamlConfiguration.loadConfiguration(f);
		file.set("cash", cash.getCashAmount());
		file.set("uuid", p.getUniqueId().toString());
		file.set("nick", p.getName());
	    ACWallet accw = new ACWallet(0);
		if (acbank.getACWallet(p) != null) {
			accw = acbank.getACWallet(p);
		}
		if (accw.getAmount() <=0 && cash.getCashAmount() <= 0) return;
		file.set("ac-cash", accw.getAmount());
		try {
		if (!f.exists()) {
			f.createNewFile();
		}
		file.save(f);
		} catch (IOException e) {
		}
			}

			@Override
			public void reduceTicks() {
				// TODO Auto-generated method stub
				health--;
			}

			@Override
			public int getLiveTicks() {
				// TODO Auto-generated method stub
				return health;
			}
	});
	}
	public final Cash getCash(final Player p) {
		Cash cash2 = null;
		final UUID uu = p.getUniqueId();
		final File pfile = new File(MainInst.getDataFolder(), "playerdata" + File.separator + uu + ".yml");
		final YamlConfiguration pyml = YamlConfiguration.loadConfiguration(pfile);
		if (!pfile.exists()) {
			return null;
			/*
			try {
			pfile.createNewFile();
			final YamlConfiguration writer = YamlConfiguration.loadConfiguration(pfile);
			writer.set("cash", 0);
			writer.set("ac-cash", 0);
			writer.set("uuid", uu.toString());
			writer.save(pfile);
			} catch (final IOException e) {
				Utility.sendConsole("[DC] Couldn't save file for Player: " + p.getName());
			}
			*/
		}
		if (cbank.getCash(uu) == null) {
			final int cash = pyml.getInt("cash");
			cash2 = new Cash(cash);
			cbank.setCash(p, cash2);
		}
		if (acbank.getACWallet(uu) == null) {
			final int acc = pyml.getInt("ac-cash");
			acbank.setACWallet(uu, new ACWallet(acc));
		}
		cash2 = cbank.getCash(p);
		return cash2;
	}
}
