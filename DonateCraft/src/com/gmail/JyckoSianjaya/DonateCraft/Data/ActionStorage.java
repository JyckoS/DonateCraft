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

public final class ActionStorage {
	private static ActionStorage instance;
	private final HashMap<String, Action> actions = new HashMap<String, Action>();
	private final HashMap<ItemStack, Action> dcactions = new HashMap<ItemStack, Action>();
	private ActionStorage() {
		LoadActions();
	}
	public static final ActionStorage getInstance() {
		if (instance == null) {
			instance = new ActionStorage();
		}
		return instance;
	}
	public final void LoadActions() {
		final File file = new File(DonateCraft.getInstance().getDataFolder(), "Actions.yml");
		final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection actionsection = yaml.getConfigurationSection("Actions");
		final Set<String> actions = actionsection.getKeys(false);
		for (final String str : actions) {
			ArrayList<String> action = new ArrayList<String>(actionsection.getStringList(str));
			for (final String act : action) {
			if (act.contains("%SNDS")) {
				final String[] sta = act.replaceAll("%SNDS", "").split("-");
				final String rsound = sta[0];
				try {
				final XSound snd = XSound.requestXSound(rsound);
				final Sound snde = snd.bukkitSound();
				} catch (IllegalArgumentException e) {
					Utility.sendConsole("&6DC >  &c[WARNING] There's no Available sound for sound name: " + rsound + ", in the Action: " + str);
				}
			}
			if (act.contains("%ITEM")) {
				final String[] items = act.replaceAll("%ITEM", "").split("-");
				final String material = items[0];
				try {
				final XMaterial mat = XMaterial.fromString(material);
				final Material bmat = mat.parseMaterial();
				} catch (IllegalArgumentException a) {
					Utility.sendConsole("&6DC > &c[WARNING] There's no available Material for Material: " + material + ", from Action: " + str);
				}
			}
			this.setAction(str, new Action(action));
		}
		}
	}
	public final void setAction(final ItemStack item, final Action act) {
		this.dcactions.put(item, act);
	}
	public final Action getAction(final ItemStack item) {
		return dcactions.get(item);
	}
	public final void setAction(final String key, final Action act) {
		actions.put(key, act);
	}
	public final Action getAction(final String key) {
		return actions.get(key);
	}
	public final Collection<Action> getActions() {
		return actions.values();
	}
	public final  Collection<String> getKeys() {
		return actions.keySet();
	}
}
