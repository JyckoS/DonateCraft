package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;

public class RedeemStorage {
	private static RedeemStorage instance;
	private ArrayList<RedeemCode> Redeemcodes = new ArrayList<RedeemCode>();
	private RedeemStorage() {
	}
	public void LoadRedeemFile() {
		File file = new File(DonateCraft.getInstance().getDataFolder(), "Redeems.yml");
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection redeems = yaml.getConfigurationSection("redeems");
		Set<String> keys = redeems.getKeys(false);
		for (String str : keys) {
			int amount = redeems.getInt(str);
			Cash cash = new Cash(amount);
			RedeemCode code = new RedeemCode(str, cash);
			this.addCode(code);
		}
	}
	public static RedeemStorage getInstance() {
		if (instance == null) {
			instance = new RedeemStorage();
		}
		return instance;
	}
	public void addCode(RedeemCode code) {
		this.Redeemcodes.add(code);
	}
	public void removeCode(RedeemCode code) {
		this.Redeemcodes.remove(code);
	}
	public void clearCodes() {
		this.Redeemcodes.clear();
	}
	public RedeemCode getRedeemCode(String code){
		for (RedeemCode ecode : Redeemcodes) {
			if (ecode.getCode().equals(code)) {
				return ecode;
			}
	    }
		return null;
	}
	public boolean hasCode(String code) {
		for (RedeemCode ecode : Redeemcodes) {
			if (ecode.getCode().equals(code)) {
				return true;
			}
			continue;
		}
		return false;
	}
	public ArrayList<RedeemCode> getRedeemCodes() {
		return this.Redeemcodes;
	}
}
