package com.gmail.JyckoSianjaya.DonateCraft.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage.Do;
import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.NBT.NBTItem;

public final class ConfirmationManager {
	private static ConfirmationManager mng;
	private final InventoryStorage invs = InventoryStorage.getInstance();
	private final DataStorage data = DataStorage.getInstance();
	private ConfirmationManager() {
	}
	public final static ConfirmationManager getInstance() {
		if (mng == null) {
			mng = new ConfirmationManager();
		}
		return mng;
	}
	public enum ConfirmationSlot {
		YES,
		NO,
		VARIABLE;
	}
	public final void CreateConfirmation(final Inventory originalinv, final Player target, final int cost, ItemStack item, final int ocost) {
		final Inventory newconf = invs.getConfirmationInventory(originalinv, cost, ocost, item);
		final int varslot = data.getConfirmationSlot(ConfirmationSlot.VARIABLE);
		// VarItem = Original Raw Item (to modify)
		// Item = Item Source (Bought item)
		final ItemStack varitem = newconf.getItem(varslot);
		if (data.getDoes(Do.var_original_material)) {
			varitem.setType(item.getType());
		}
		final ItemMeta varitemmeta = varitem.getItemMeta();
		final ItemMeta itemmeta = item.getItemMeta();
		final NBTItem nbt = new NBTItem(varitem);
		final String itemoriginalname = itemmeta.getDisplayName();
		if (data.getDoes(Do.var_original_lore)) {
			varitemmeta.setLore(itemmeta.getLore());
		}
		else {
		    double discount = DataStorage.getInstance().getDiscount();
			List<String> varlore = varitemmeta.getLore();
			ArrayList<String> newlore = new ArrayList<String>();
			if (nbt.hasKey("DCDiscount")) {
				discount = nbt.getDouble("DCDiscount");
			}
			if (discount <= 0.0) {
				varlore = InventoryStorage.getInstance().getNoDiscountLore();
			}
			for (String str : varlore) {
				if (str.contains("%s")) str = str.replaceAll("%s", "" + cost);
				if (str.contains("%o")) str = str.replaceAll("%o", "" + ocost);
				if (str.contains("%ORIGINALNAME%")) str = str.replaceAll("%ORIGINALNAME%", itemoriginalname + "&r");

				newlore.add(str);
			}
			
			varitemmeta.setLore(newlore);
		}
		String dname = varitemmeta.getDisplayName();
		if (dname.contains("%ORIGINALNAME%")) {
			dname = Utility.TransColor(dname.replaceAll("%ORIGINALNAME%", itemmeta.getDisplayName() + "&r"));
		}
		if (dname.contains("%s")) dname.replaceAll("%s", cost + "");
		if (dname.contains("%o")) dname.replaceAll("%o", ocost + "");
		varitemmeta.setDisplayName(dname);
		varitem.setItemMeta(varitemmeta);
		newconf.setItem(varslot, varitem);
		target.closeInventory();
		target.openInventory(newconf);
	}
}
