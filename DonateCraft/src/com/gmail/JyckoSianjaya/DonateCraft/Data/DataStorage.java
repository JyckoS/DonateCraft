package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Database.SimpleSQL;
import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.ConfirmationManager.ConfirmationSlot;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Stock;

public final  class DataStorage {
	private static DataStorage instance;
	private Double discount;
	private final HashMap<ConfirmationSlot, Integer> Slots = new HashMap<ConfirmationSlot, Integer>();
	private final HashMap<Do, Boolean> Does = new HashMap<Do, Boolean>();
	private final HashMap<String, Stock> stocks = new HashMap<String, Stock>();
	private Boolean Use_sql = false;
	private SimpleSQL thesql = null;
	private DataStorage() {
		LoadConfig();
	}
	public boolean useSQL() {
		return this.Use_sql;
	}
	public void setStock(String item, Stock stock) {
		this.stocks.put(item, stock);
	}
	public Stock getStock(String item) {
		return stocks.get(item);
	}
	public boolean hasStock(String item) {
		return stocks.containsKey(item);
	}
	public final  static DataStorage getInstance()
	{
		if (instance == null) {
			instance = new DataStorage();
			}
		return instance;
	}
	public final void LoadConfig() {
		final FileConfiguration config = DonateCraft.getInstance().getConfig();
		DonateCraft.getInstance().reloadConfig();
		discount = config.getDouble("current_discount");
		final ConfigurationSection confirmation = config.getConfigurationSection("confirmation");
		final ConfigurationSection items = confirmation.getConfigurationSection("items");
		final int varslot = items.getInt("variable.slot");
		final int yesslot = items.getInt("confirm_yes.slot");
		final int noslot = items.getInt("confirm_no.slot");
		final Boolean use_sql = config.getBoolean("MySQL.use_sql");
		this.Use_sql = use_sql;
		if (use_sql) {
			thesql = SimpleSQL.setup(config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("database"), config.getString("user"), config.getString("pass"));
			
		}
		final Boolean varitem_original_lore = items.getBoolean("variable.use_original_lore");
		final Boolean varitem_original_material = items.getBoolean("variable.use_original_material");
		final Boolean noitem_back_to_gui = items.getBoolean("confirm_no.back_to_original_gui");
		this.Slots.put(ConfirmationSlot.VARIABLE, varslot);
		this.Slots.put(ConfirmationSlot.YES, yesslot);
		this.Slots.put(ConfirmationSlot.NO, noslot);
		Does.put(Do.var_original_lore, varitem_original_lore);
		Does.put(Do.var_original_material, varitem_original_material);
		Does.put(Do.decline_back_gui, noitem_back_to_gui);
	}
	public final void setSQL(boolean use) {
		this.Use_sql = use;
	}
	public final Boolean getDoes(Do d) {
		return Does.get(d);
	}
	public enum Do {
		var_original_lore,
		var_original_material,
		decline_back_gui;
	}
	public final Integer getConfirmationSlot(final ConfirmationSlot s) {
		return Slots.get(s);
	}
	public final Double getRawDiscount() {
		return discount;
	}
	public final  Double getDiscount() {
		return discount/100;
	}
	public final void setDiscount(final Double amount) {
		this.discount = amount;
	}
}
