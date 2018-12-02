package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public class PlayerData {
	private static PlayerData instance;
	private DonateCraft MainInst = DonateCraft.getInstance();
	private CashBank cbank = CashBank.getInstance();
	private ACCashBank acbank = ACCashBank.getInstance();
	private PlayerData() {
	}
	public static PlayerData getInstance() {
		if (instance == null) {
			instance = new PlayerData();
		}
		return instance;
	}
	public void setData(Player p, Cash cash) throws IOException {
		File f = new File(MainInst.getDataFolder(), "playerdata" + File.separator +  p.getUniqueId() + ".yml");
		if (!f.exists()) {
			f.createNewFile();
		}
		YamlConfiguration file = YamlConfiguration.loadConfiguration(f);
		file.set("cash", cash.getCashAmount());
		file.set("uuid", p.getUniqueId().toString());
		ACWallet accw = new ACWallet(0);
		if (acbank.getACWallet(p) != null) {
			accw = acbank.getACWallet(p);
		}
		file.set("ac-cash", accw.getAmount());
		file.save(f);
	}
	public Cash getCash(Player p) {
		Cash cash2 = null;
		UUID uu = p.getUniqueId();
		File pfile = new File(MainInst.getDataFolder(), "playerdata" + File.separator + uu + ".yml");
		YamlConfiguration pyml = YamlConfiguration.loadConfiguration(pfile);
		if (!pfile.exists()) {
			try {
			pfile.createNewFile();
			YamlConfiguration writer = YamlConfiguration.loadConfiguration(pfile);
			writer.set("cash", 0);
			writer.set("ac-cash", 0);
			writer.set("uuid", uu.toString());
			writer.save(pfile);
			} catch (IOException e) {
				Utility.sendConsole("[DC] Couldn't save file for Player: " + p.getName());
			}
		}
		if (cbank.getCash(uu) == null) {
			int cash = pyml.getInt("cash");
			cash2 = new Cash(cash);
			cbank.setCash(p, cash2);
		}
		if (acbank.getACWallet(uu) == null) {
			int acc = pyml.getInt("ac-cash");
			acbank.setACWallet(uu, new ACWallet(acc));
		}
		cash2 = cbank.getCash(p);
		return cash2;
	}
}
