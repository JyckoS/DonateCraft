package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Action;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XMaterial;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.NBT.NBTItem;


public final class ItemStorage {
	private static ItemStorage instance;
	private final HashMap<ItemStack, Action> itemaction = new HashMap<ItemStack, Action>();
	private final HashMap<ItemStack, Integer> cost = new HashMap<ItemStack, Integer>();
	private final HashMap<String, ItemStack> itemkey = new HashMap<String, ItemStack>();
	private final ActionStorage act = ActionStorage.getInstance();
	private ItemStack denyitem = null;
	private ItemStack denyitem2 = null;
	private ItemStack denyitem3 = null;
	private ItemStorage() {
		LoadFiles();
		loadDeny();
	}
	private void loadDeny() {
		denyitem = new ItemStack(XMaterial.BARRIER.parseItem());
		final ItemMeta denymt = denyitem.getItemMeta();
		denymt.setDisplayName(Utility.TransColor("&c&lWoopsy!"));
		final ArrayList<String> lores = new ArrayList<String>();
		lores.add("&7You can't buy this yet!");
		denymt.setLore(Utility.TransColor(lores));
		denyitem.setItemMeta(denymt);
		denyitem2 = denyitem.clone();
		lores.clear();
		lores.add("&7You can't buy this anymore!");
		denymt.setLore(Utility.TransColor(lores));
		denyitem2.setItemMeta(denymt);
		lores.clear();
		lores.add("&7You do not have enough cash!");
		denymt.setLore(Utility.TransColor(lores));
		denyitem3 = denyitem2.clone();
		denyitem3.setItemMeta(denymt);
	}
	public final ItemStack getDenyItem3() {
		return denyitem3.clone();
	}
	public final ItemStack getDenyItem2() {
		return denyitem2.clone();
	}
	public final ItemStack getDenyItem() {
		return denyitem.clone();
	}
	public final static ItemStorage getInstance() {
		if (instance == null) {
			instance = new ItemStorage();
		}
		return instance; 
	}
	public final Action getItemAction(final ItemStack item) {
		return itemaction.get(item);
	}
	public final void setCost(final ItemStack item, final Integer cost) {
		this.cost.put(item, cost);
	}
	public final Integer getCost(final ItemStack item) {
		return cost.get(item);
	}
	public final Collection<String> getKeys() {
		return itemkey.keySet();
	}
	public final void setItemAction(final ItemStack item, final Action action) {
		itemaction.put(item, action);
	}
	public final void LoadFiles() {
		final File f = new File(DonateCraft.getInstance().getDataFolder(), "Items.yml");
		final YamlConfiguration it = YamlConfiguration.loadConfiguration(f);
		final ConfigurationSection items = it.getConfigurationSection("Items");
		final Set<String> keys = items.getKeys(false);
		for (final String key : keys) {
			final 	Integer xxdurability = items.getInt(key + ".DURABILITY");
			// Loads important stuff
			final String itemname = TransColor(items.getString(key + ".ITEM_NAME"));
			final List<String> itemlore = TransColor(items.getStringList(key + ".ITEM_LORE"));
			final 	String requestedmat = items.getString(key + ".MATERIAL");
			final 	Short durability = xxdurability.shortValue();
			Material imaterial;
			try {
			imaterial = XMaterial.requestMaterial(requestedmat, Byte.valueOf("0"));
			}catch (NullPointerException e) {
			 	imaterial = XMaterial.STONE.parseMaterial();
				Utility.sendConsole("[WARNING] There's no actual item material for item key: " + requestedmat);
			}
			if (imaterial == null) {
				imaterial = XMaterial.STONE.parseMaterial();
			}
			final 	Integer price = items.getInt(key + ".COST");
			final 	String aname = items.getString(key + ".ACTION");
			final 		String bperm = items.getString(key + ".BLACKLISTED_PERM");
			final 		Action action = act.getAction(aname);
			final 		String rperm = items.getString(key + ".REQUIRED_PERM");
			if (action == null) {
				Utility.sendConsole("[WARNING] There's no actual action for item key: " + key + ". In 'Items.yml'! Proceed with Caution!");
			}
			ItemStack item = new ItemStack(imaterial);
			item.setDurability(durability);
			final NBTItem nitem = new NBTItem(item);
			nitem.setString("DCAction", aname);
			nitem.setInteger("DCCost", price);
			nitem.setString("BLISTPERM", bperm);
			nitem.setString("DCPerm", rperm);
			item = nitem.getItem();
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(itemname);
			meta.setLore(itemlore);
			item.setItemMeta(meta);
			setItemAction(item, action);
			setItem(key, item);
			setCost(item, price);
		}
	}
	private final String TransColor(final String str) {
		return Utility.TransColor(str);
	}
	private final List<String> TransColor(final List<String> str) {
		return Utility.TransColor(str);
	}
	public final void setItem(final String key, final ItemStack item) {
		itemkey.put(key, item);
	}
	public final ItemStack getItem(final String key) {
		return itemkey.get(key);
	}
}
