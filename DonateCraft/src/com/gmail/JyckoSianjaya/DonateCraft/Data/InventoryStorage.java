package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ConfirmationHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.DCHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XMaterial;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.NBT.NBTItem;


public class InventoryStorage {
	private static InventoryStorage instance;
	private HashMap<String, Inventory> key = new HashMap<String, Inventory>();
	private Inventory confirmationinventory;
	private ItemStorage is = ItemStorage.getInstance();
	private InventoryStorage() {
		LoadConfirmation();
	}
	public static InventoryStorage getInstance() {
		if (instance == null) {
			instance = new InventoryStorage();
			}
		return instance;
	}
	public Inventory getConfirmationInventory() {
		return copy(confirmationinventory);
	}
	public Inventory getConfirmationInventory(Inventory toreturn, int cost, int ocost, ItemStack item) {
		return copy(confirmationinventory, toreturn, cost, item, ocost);
	}
	private Inventory copy(Inventory inventory) {
	    Inventory inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
	    ItemStack[] orginal = inv.getContents();
	    ItemStack[] clone = new ItemStack[orginal.length];

	    System.arraycopy(orginal, 0, clone, 0, orginal.length);

	    inv.setContents(clone);
	    return inv;
	}
	private Inventory copy(Inventory inventory, Inventory toreturn, int cost, ItemStack item, int ocost) {
	    Inventory inv = Bukkit.createInventory(new ConfirmationHolder(toreturn, cost, item, ocost), inventory.getSize(), inventory.getTitle());
	    inv.setContents(inventory.getContents().clone());
	    return inv;
	}
	public void setKey(String key, Inventory inv) {
		this.key.put(key, inv);
	}
	public Collection<String> getKeys() {
		return key.keySet();
	}
	public Inventory getInventory(String key) {
		return Utility.copy(this.key.get(key), new DCHolder());
	}
	public boolean hasKey(String key) {
		return this.key.keySet().contains(key);
	}
	public void LoadFiles() {
		File file = new File(DonateCraft.getInstance().getDataFolder(), "GUI.yml");
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection GUIS = yaml.getConfigurationSection("GUI");
		Set<String> guixx = GUIS.getKeys(false);
		for (String gui : guixx) {
			int size = GUIS.getInt(gui + ".Size");
			String title = Color(GUIS.getString(gui + ".Title"));
			ConfigurationSection itemz = GUIS.getConfigurationSection(gui + ".Items");
			Set<String> items = itemz.getKeys(false);
			Inventory inv = Bukkit.createInventory(new DCHolder(), size, title);
			for (String it : items) {
				String itemkey = itemz.getString(it);
				ItemStack ditem = is.getItem(itemkey);
				int cr = Integer.valueOf(it);
				inv.setItem(cr, ditem);
			}
			setKey(gui, inv);
		}
		LoadConfirmation();

	}
	private void LoadConfirmation() {
		FileConfiguration config = DonateCraft.getInstance().getConfig();
		ConfigurationSection confirmation = config.getConfigurationSection("confirmation");
		int guisize = confirmation.getInt(("size"));
		ConfigurationSection items = confirmation.getConfigurationSection("items");
		
		ConfigurationSection variable = items.getConfigurationSection("variable");
		ConfigurationSection yes = items.getConfigurationSection("confirm_yes");
		ConfigurationSection no = items.getConfigurationSection("confirm_no");
		ConfigurationSection neutrals = items.getConfigurationSection("neutral_items");
		Set<String> keys = neutrals.getKeys(false);
		
		Inventory dummy = Bukkit.createInventory(new DCHolder(), guisize, Color(confirmation.getString("title")));
		
		for (String str : keys) {
			String result = neutrals.getString(str);
			ItemStack ite = is.getItem(result);
			dummy.setItem(Integer.valueOf(str), ite);
		}
		ItemStack yesitem = new ItemStack(XMaterial.requestXMaterial(yes.getString("material"), Byte.valueOf("" + 0)).parseMaterial());
		yesitem.setDurability(Short.valueOf(yes.getString("durability")));
		String action = yes.getString("action");
		ItemMeta yitemmeta = yesitem.getItemMeta();
		yitemmeta.setDisplayName(Color(yes.getString("name")));
		yitemmeta.setLore(Color(yes.getStringList("lores")));
		yesitem.setItemMeta(yitemmeta);
		NBTItem nbtyes = new NBTItem(yesitem);
		nbtyes.setString("CCAction", action);
		nbtyes.setString("custom_actiontype", "yes");
		yesitem = nbtyes.getItem();
		dummy.setItem(yes.getInt("slot"), yesitem);
		
		ItemStack noitem = new ItemStack(XMaterial.requestXMaterial(no.getString("material"), Byte.valueOf("" + 0)).parseMaterial());
		noitem.setDurability(Short.valueOf(no.getString("durability")));
		String aaction = no.getString("action");
		ItemMeta nitemmeta = noitem.getItemMeta();
		nitemmeta.setDisplayName(Color(no.getString("name")));
		nitemmeta.setLore(Color(no.getStringList("lores")));
		noitem.setItemMeta(nitemmeta);
		NBTItem nbtno = new NBTItem(noitem);
		nbtno.setString("CCAction", aaction);
		nbtno.setString("custom_actiontype", "no");
		nbtno.setBoolean("DCConfirm_back", no.getBoolean("back_to_original_gui"));
		noitem = nbtno.getItem();
		dummy.setItem(no.getInt("slot"), noitem);
		
		ItemStack varitem = new ItemStack(XMaterial.valueOf(variable.getString("material")).parseMaterial());
		varitem.setDurability(Short.valueOf(variable.getString("durability")));
		NBTItem varnbt = new NBTItem(varitem);
		varnbt.setString("CCAction", variable.getString("action"));
		varnbt.setString("custom_actiontype", "var");
		varnbt.setBoolean("DCVar_original_lore", variable.getBoolean("use_original_lore"));
		varnbt.setBoolean("DCVar_original_material", variable.getBoolean("use_original_material"));
		varitem = varnbt.getItem();
		ItemMeta vimeta = varitem.getItemMeta();
		vimeta.setDisplayName(Color(variable.getString("name")));
		vimeta.setLore(Color(variable.getStringList("lores")));
		varitem.setItemMeta(vimeta);
		dummy.setItem(variable.getInt("slot"), varitem);
		confirmationinventory = dummy;
		setKey("confirmation_inventory", dummy);
	}
	private String Color(String str) {
		return Utility.TransColor(str);
	}
	@SuppressWarnings("unused")
	private List<String> Color(List<String> str) {
		return Utility.TransColor(str);
	}
}
