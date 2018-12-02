package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Action;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XMaterial;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public class ActionStorage {
	private static ActionStorage instance;
	private HashMap<String, Action> actions = new HashMap<String, Action>();
	private HashMap<ItemStack, Action> dcactions = new HashMap<ItemStack, Action>();
	private ActionStorage() {
		LoadActions();
	}
	public static ActionStorage getInstance() {
		if (instance == null) {
			instance = new ActionStorage();
		}
		return instance;
	}
	public void LoadActions() {
		File file = new File(DonateCraft.getInstance().getDataFolder(), "Actions.yml");
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection actionsection = yaml.getConfigurationSection("Actions");
		Set<String> actions = actionsection.getKeys(false);
		for (String str : actions) {
			ArrayList<String> action = new ArrayList<String>(actionsection.getStringList(str));
			for (String act : action) {
			if (act.contains("%SNDS")) {
				String[] sta = act.replaceAll("%SNDS", "").split("-");
				String rsound = sta[0];
				try {
				XSound snd = XSound.requestXSound(rsound);
				Sound snde = snd.bukkitSound();
				} catch (IllegalArgumentException e) {
					Utility.sendConsole("&6DC >  &c[WARNING] There's no Available sound for sound name: " + rsound + ", in the Action: " + str);
				}
			}
			if (act.contains("%ITEM")) {
				String[] items = act.replaceAll("%ITEM", "").split("-");
				String material = items[0];
				try {
				XMaterial mat = XMaterial.fromString(material);
				Material bmat = mat.parseMaterial();
				} catch (IllegalArgumentException a) {
					Utility.sendConsole("&6DC > &c[WARNING] There's no available Material for Material: " + material + ", from Action: " + str);
				}
			}
			this.setAction(str, new Action(action));
		}
		}
	}
	public void setAction(ItemStack item, Action act) {
		this.dcactions.put(item, act);
	}
	public Action getAction(ItemStack item) {
		return dcactions.get(item);
	}
	public void setAction(String key, Action act) {
		actions.put(key, act);
	}
	public Action getAction(String key) {
		return actions.get(key);
	}
	public Collection<Action> getActions() {
		return actions.values();
	}
	public Collection<String> getKeys() {
		return actions.keySet();
	}
}
