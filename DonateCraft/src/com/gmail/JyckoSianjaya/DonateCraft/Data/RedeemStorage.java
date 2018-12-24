package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class RedeemStorage {
	private static RedeemStorage instance;
	private final ArrayList<RedeemCode> Redeemcodes = new ArrayList<RedeemCode>();
	private YamlConfiguration yaml;
	private RedeemStorage() {
	}
	public final void LoadRedeemFile() {
		final File file = new File(DonateCraft.getInstance().getDataFolder(), "Redeems.yml");
		final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection redeems = yaml.getConfigurationSection("redeems");
		final Set<String> keys = redeems.getKeys(false);
		for (final String str : keys) {
			final int amount = redeems.getInt(str);
			if (amount <= 0) continue;
			final Cash cash = new Cash(amount);
			final RedeemCode code = new RedeemCode(str, cash);
			this.addCode(code);
		}
	}
	public final  static RedeemStorage getInstance() {
		if (instance == null) {
			instance = new RedeemStorage();
		}
		return instance;
	}
	public final void addCode(final RedeemCode code) {
		this.Redeemcodes.add(code);
	}
	public final void removeCode(final RedeemCode code) {
		this.Redeemcodes.remove(code);
	}
	public final void clearCodes() {
		this.Redeemcodes.clear();
	}
	public final RedeemCode getRedeemCode(final String code){
		for (RedeemCode ecode : Redeemcodes) {
			if (ecode.getCode().equals(code)) {
				return ecode;
			}
	    }
		return null;
	}
	public final boolean hasCode(final String code) {
		for (final RedeemCode ecode : Redeemcodes) {
			if (ecode.getCode().equals(code)) {
				return true;
			}
			continue;
		}
		return false;
	}
	public final ArrayList<RedeemCode> getRedeemCodes() {
		return this.Redeemcodes;
	}
}
