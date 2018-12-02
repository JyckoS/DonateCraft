package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.ConfirmationManager.ConfirmationSlot;

public class DataStorage {
	private static DataStorage instance;
	private Double discount;
	private HashMap<ConfirmationSlot, Integer> Slots = new HashMap<ConfirmationSlot, Integer>();
	private HashMap<Do, Boolean> Does = new HashMap<Do, Boolean>();
	private DataStorage() {
		LoadConfig();
	}
	public static DataStorage getInstance()
	{
		if (instance == null) {
			instance = new DataStorage();
			}
		return instance;
	}
	public void LoadConfig() {
		FileConfiguration config = DonateCraft.getInstance().getConfig();
		DonateCraft.getInstance().reloadConfig();
		discount = config.getDouble("current_discount");
		ConfigurationSection confirmation = config.getConfigurationSection("confirmation");
		ConfigurationSection items = confirmation.getConfigurationSection("items");
		int varslot = items.getInt("variable.slot");
		int yesslot = items.getInt("confirm_yes.slot");
		int noslot = items.getInt("confirm_no.slot");
		Boolean varitem_original_lore = items.getBoolean("variable.use_original_lore");
		Boolean varitem_original_material = items.getBoolean("variable.use_original_material");
		Boolean noitem_back_to_gui = items.getBoolean("confirm_no.back_to_original_gui");
		this.Slots.put(ConfirmationSlot.VARIABLE, varslot);
		this.Slots.put(ConfirmationSlot.YES, yesslot);
		this.Slots.put(ConfirmationSlot.NO, noslot);
		Does.put(Do.var_original_lore, varitem_original_lore);
		Does.put(Do.var_original_material, varitem_original_material);
		Does.put(Do.decline_back_gui, noitem_back_to_gui);
	}
	public Boolean getDoes(Do d) {
		return Does.get(d);
	}
	public enum Do {
		var_original_lore,
		var_original_material,
		decline_back_gui;
	}
	public Integer getConfirmationSlot(ConfirmationSlot s) {
		return Slots.get(s);
	}
	public Double getRawDiscount() {
		return discount;
	}
	public Double getDiscount() {
		return discount/100;
	}
	public void setDiscount(Double amount) {
		this.discount = amount;
	}
}
