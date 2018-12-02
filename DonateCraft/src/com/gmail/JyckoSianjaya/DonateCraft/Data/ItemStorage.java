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


public class ItemStorage {
	private static ItemStorage instance;
	private HashMap<ItemStack, Action> itemaction = new HashMap<ItemStack, Action>();
	private HashMap<ItemStack, Integer> cost = new HashMap<ItemStack, Integer>();
	private HashMap<String, ItemStack> itemkey = new HashMap<String, ItemStack>();
	private ActionStorage act = ActionStorage.getInstance();
	private ItemStack denyitem = null;
	private ItemStack denyitem2 = null;
	private ItemStack denyitem3 = null;
	private ItemStorage() {
		LoadFiles();
		loadDeny();
	}
	private void loadDeny() {
		denyitem = new ItemStack(XMaterial.BARRIER.parseItem());
		ItemMeta denymt = denyitem.getItemMeta();
		denymt.setDisplayName(Utility.TransColor("&c&lWoopsy!"));
		ArrayList<String> lores = new ArrayList<String>();
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
	public ItemStack getDenyItem3() {
		return denyitem3.clone();
	}
	public ItemStack getDenyItem2() {
		return denyitem2.clone();
	}
	public ItemStack getDenyItem() {
		return denyitem.clone();
	}
	public static ItemStorage getInstance() {
		if (instance == null) {
			instance = new ItemStorage();
		}
		return instance; 
	}
	public Action getItemAction(ItemStack item) {
		return itemaction.get(item);
	}
	public void setCost(ItemStack item, Integer cost) {
		this.cost.put(item, cost);
	}
	public Integer getCost(ItemStack item) {
		return cost.get(item);
	}
	public Collection<String> getKeys() {
		return itemkey.keySet();
	}
	public void setItemAction(ItemStack item, Action action) {
		itemaction.put(item, action);
	}
	public void LoadFiles() {
		File f = new File(DonateCraft.getInstance().getDataFolder(), "Items.yml");
		YamlConfiguration it = YamlConfiguration.loadConfiguration(f);
		ConfigurationSection items = it.getConfigurationSection("Items");
		Set<String> keys = items.getKeys(false);
		for (String key : keys) {
			Integer xxdurability = items.getInt(key + ".DURABILITY");
			// Loads important stuff
			String itemname = TransColor(items.getString(key + ".ITEM_NAME"));
			List<String> itemlore = TransColor(items.getStringList(key + ".ITEM_LORE"));
			String requestedmat = items.getString(key + ".MATERIAL");
			Short durability = xxdurability.shortValue();
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
			Integer price = items.getInt(key + ".COST");
			String aname = items.getString(key + ".ACTION");
			String bperm = items.getString(key + ".BLACKLISTED_PERM");
			Action action = act.getAction(aname);
			String rperm = items.getString(key + ".REQUIRED_PERM");
			if (action == null) {
				Utility.sendConsole("[WARNING] There's no actual action for item key: " + key + ". In 'Items.yml'! Proceed with Caution!");
			}
			ItemStack item = new ItemStack(imaterial);
			item.setDurability(durability);
			NBTItem nitem = new NBTItem(item);
			nitem.setString("DCAction", aname);
			nitem.setInteger("DCCost", price);
			nitem.setString("BLISTPERM", bperm);
			nitem.setString("DCPerm", rperm);
			item = nitem.getItem();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(itemname);
			meta.setLore(itemlore);
			item.setItemMeta(meta);
			setItemAction(item, action);
			setItem(key, item);
			setCost(item, price);
		}
	}
	private String TransColor(String str) {
		return Utility.TransColor(str);
	}
	private List<String> TransColor(List<String> str) {
		return Utility.TransColor(str);
	}
	public void setItem(String key, ItemStack item) {
		itemkey.put(key, item);
	}
	public ItemStack getItem(String key) {
		return itemkey.get(key);
	}
}
