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


public final class InventoryStorage {
	private static InventoryStorage instance;
	private final HashMap<String, Inventory> key = new HashMap<String, Inventory>();
	private Inventory confirmationinventory;
	private final ItemStorage is = ItemStorage.getInstance();
	private InventoryStorage() {
		LoadConfirmation();
	}
	public final static InventoryStorage getInstance() {
		if (instance == null) {
			instance = new InventoryStorage();
			}
		return instance;
	}
	public final Inventory getConfirmationInventory() {
		return copy(confirmationinventory);
	}
	public final Inventory getConfirmationInventory(Inventory toreturn, int cost, int ocost, ItemStack item) {
		return copy(confirmationinventory, toreturn, cost, item, ocost);
	}
	private final Inventory copy(final Inventory inventory) {
	    Inventory inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
	    final  ItemStack[] orginal = inv.getContents();
	    final  ItemStack[] clone = new ItemStack[orginal.length];

	    System.arraycopy(orginal, 0, clone, 0, orginal.length);

	    inv.setContents(clone);
	    return inv;
	}
	private final Inventory copy(final Inventory inventory, final Inventory toreturn, final int cost, final ItemStack item, final int ocost) {
		final Inventory inv = Bukkit.createInventory(new ConfirmationHolder(toreturn, cost, item, ocost), inventory.getSize(), inventory.getTitle());
	    inv.setContents(inventory.getContents().clone());
	    return inv;
	}
	public final void setKey(final String key, final Inventory inv) {
		this.key.put(key, inv);
	}
	public final Collection<String> getKeys() {
		return key.keySet();
	}
	public final Inventory getInventory(final String key) {
		return Utility.copy(this.key.get(key), new DCHolder());
	}
	public final boolean hasKey(final String key) {
		return this.key.keySet().contains(key);
	}
	public final void LoadFiles() {
		final File file = new File(DonateCraft.getInstance().getDataFolder(), "GUI.yml");
		final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection GUIS = yaml.getConfigurationSection("GUI");
		final Set<String> guixx = GUIS.getKeys(false);
		for (final String gui : guixx) {
			final int size = GUIS.getInt(gui + ".Size");
			final String title = Color(GUIS.getString(gui + ".Title"));
			final ConfigurationSection itemz = GUIS.getConfigurationSection(gui + ".Items");
			final Set<String> items = itemz.getKeys(false);
			final Inventory inv = Bukkit.createInventory(new DCHolder(), size, title);
			for (final String it : items) {
				final String itemkey = itemz.getString(it);
				final ItemStack ditem = is.getItem(itemkey);
				final int cr = Integer.valueOf(it);
				inv.setItem(cr, ditem);
			}
			setKey(gui, inv);
		}
		LoadConfirmation();

	}
	private final void LoadConfirmation() {
		final FileConfiguration config = DonateCraft.getInstance().getConfig();
		final ConfigurationSection confirmation = config.getConfigurationSection("confirmation");
		final int guisize = confirmation.getInt(("size"));
		final ConfigurationSection items = confirmation.getConfigurationSection("items");
		
		final ConfigurationSection variable = items.getConfigurationSection("variable");
		final ConfigurationSection yes = items.getConfigurationSection("confirm_yes");
		final ConfigurationSection no = items.getConfigurationSection("confirm_no");
		final ConfigurationSection neutrals = items.getConfigurationSection("neutral_items");
		final Set<String> keys = neutrals.getKeys(false);
		
		final Inventory dummy = Bukkit.createInventory(new DCHolder(), guisize, Color(confirmation.getString("title")));
		
		for (final String str : keys) {
			final String result = neutrals.getString(str);
			final ItemStack ite = is.getItem(result);
			dummy.setItem(Integer.valueOf(str), ite);
		}
		ItemStack yesitem = new ItemStack(XMaterial.requestXMaterial(yes.getString("material"), Byte.valueOf("" + 0)).parseMaterial());
		yesitem.setDurability(Short.valueOf(yes.getString("durability")));
		final String action = yes.getString("action");
		final ItemMeta yitemmeta = yesitem.getItemMeta();
		yitemmeta.setDisplayName(Color(yes.getString("name")));
		yitemmeta.setLore(Color(yes.getStringList("lores")));
		yesitem.setItemMeta(yitemmeta);
		final NBTItem nbtyes = new NBTItem(yesitem);
		nbtyes.setString("CCAction", action);
		nbtyes.setString("custom_actiontype", "yes");
		yesitem = nbtyes.getItem();
		dummy.setItem(yes.getInt("slot"), yesitem);
		
		ItemStack noitem = new ItemStack(XMaterial.requestXMaterial(no.getString("material"), Byte.valueOf("" + 0)).parseMaterial());
		noitem.setDurability(Short.valueOf(no.getString("durability")));
		final String aaction = no.getString("action");
		final ItemMeta nitemmeta = noitem.getItemMeta();
		nitemmeta.setDisplayName(Color(no.getString("name")));
		nitemmeta.setLore(Color(no.getStringList("lores")));
		noitem.setItemMeta(nitemmeta);
		final NBTItem nbtno = new NBTItem(noitem);
		nbtno.setString("CCAction", aaction);
		nbtno.setString("custom_actiontype", "no");
		nbtno.setBoolean("DCConfirm_back", no.getBoolean("back_to_original_gui"));
		noitem = nbtno.getItem();
		dummy.setItem(no.getInt("slot"), noitem);
		
		ItemStack varitem = new ItemStack(XMaterial.valueOf(variable.getString("material")).parseMaterial());
		varitem.setDurability(Short.valueOf(variable.getString("durability")));
		final NBTItem varnbt = new NBTItem(varitem);
		varnbt.setString("CCAction", variable.getString("action"));
		varnbt.setString("custom_actiontype", "var");
		varnbt.setBoolean("DCVar_original_lore", variable.getBoolean("use_original_lore"));
		varnbt.setBoolean("DCVar_original_material", variable.getBoolean("use_original_material"));
		varitem = varnbt.getItem();
		final ItemMeta vimeta = varitem.getItemMeta();
		vimeta.setDisplayName(Color(variable.getString("name")));
		vimeta.setLore(Color(variable.getStringList("lores")));
		varitem.setItemMeta(vimeta);
		dummy.setItem(variable.getInt("slot"), varitem);
		confirmationinventory = dummy;
		setKey("confirmation_inventory", dummy);
	}
	private final  String Color(final String str) {
		return Utility.TransColor(str);
	}
	@SuppressWarnings("unused")
	private final List<String> Color(final List<String> str) {
		return Utility.TransColor(str);
	}
}
