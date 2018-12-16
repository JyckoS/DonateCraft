package com.gmail.JyckoSianjaya.DonateCraft.Events;

import java.io.IOException;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ActionStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DCLogger;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ItemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.PlayerData;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.ConfirmationManager;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Action;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ConfirmationHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.DCHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.UUIDCacher;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.NBT.NBTItem;


public final class DCEventHandler {
	private static DCEventHandler instance;
	private final ActionStorage actions = ActionStorage.getInstance();
	private final CashBank bank = CashBank.getInstance();
	private final PlayerData pd = PlayerData.getInstance();
	private final LangStorage ls = LangStorage.getInstance();
	private final DataStorage ds = DataStorage.getInstance();
	private final ItemStorage is = ItemStorage.getInstance();
	private final DCLogger loger = DCLogger.getInstance();
	private final ConfirmationManager confirm = ConfirmationManager.getInstance();
	private DCEventHandler() {
	}
	public final static DCEventHandler getInstance() {
		if (instance == null) {
			instance = new DCEventHandler();
		}
		return instance;
	}
	public final void ManageConfirmationClick(final InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.OUTSIDE) {
			return;
		}
		e.setCancelled(true);
		final ItemStack item = e.getCurrentItem();
		 if (item == null) {
			 return;
		 }
		 if (item.getType() == Material.AIR) {
			 return;
		 }
		 if (item.getItemMeta().getDisplayName() == null) {
			 return;
		 }
		 final  Player p = (Player) e.getWhoClicked();
		 Cash cash = null;
		 if (!bank.hasCash(p)) {
			 bank.setCash(p, new Cash(0));
		 }
		 if (cash == null) {
			 cash = new Cash(0);
		 }
		 cash = bank.getCash(p);
		 final ConfirmationHolder ch = (ConfirmationHolder) e.getInventory().getHolder();
		 final NBTItem rritem = new NBTItem(item);
		 if (!rritem.hasKey("custom_actiontype")) {
			 return;
		 }
		 final String cckey = rritem.getString("custom_actiontype");
		 switch (cckey.toLowerCase()) {
		 case "yes":
			 final  ItemStack target = ch.getTarget();
			 final  NBTItem titem = new NBTItem(target);
			 final  String akey = titem.getString("DCAction");
			 final  NBTItem citem = new NBTItem(item);
			 final  String ckey = citem.getString("CCAction");
			 if (akey == null) {
				 return;
			 }
			 int cost = 0;
			 cost = titem.getInteger("DCCost");
			 Double disc = ds.getDiscount();
			 if (titem.hasKey("DCDiscount")) {
				 disc = titem.getDouble("DCDiscount") / 100;
			 }
			 cost = (int) (cost - cost * disc);
			 final  int amount = cash.getCashAmount();
			 if (amount < cost) {
				 final  int less = cost - amount;
				 for (String str : ls.getMessage(LongMessage.NO_CASH)) {
					 if (str.contains("%MORE%")) {
						 str = str.replaceAll("%MORE%", "" + less); 
					 }
					 if (str.contains("%CASH%")) {
						 str = str.replaceAll("%CASH%", "" + amount);
					 }
					 Utility.sendMsg(p, str);
				 }
				 return;
			 }
			 cash.setCash(amount - cost);
			 PlayerData.getInstance().setData(p, cash);
			 if (cost > 0) {
				 loger.LogMessage("[PURCHASE] " + p.getName() + " bought action '" + akey + "' for " + cost + ", cash left: " + cash.getCashAmount() + "(DISCOUNT: " + ds.getRawDiscount() + "%)");
			 }
			 p.closeInventory();
			 if (actions.getAction(ckey) != null) {
			 final  Action caction = actions.getAction(ckey);
			 caction.applyCommand(p);
			 }
			 final Action action = actions.getAction(akey);
			 action.applyCommand(p);
			 e.setCancelled(true); 
			 return;
		 case "no":
			 final String dkey = rritem.getString("CCAction");
			 final Action taction = actions.getAction(dkey);
			 final Inventory oginv = ch.getOriginalInventory();
			 p.openInventory(oginv);
			 taction.applyCommand(p);
			 return;
		 case "var":
			 final  String rcaction =  rritem.getString("CCAction");
			 final Action ddaction = actions.getAction(rcaction);
			 ddaction.applyCommand(p);
			 return;
			 default:
				 return;
		 }
		
	}
	public void ManageInventoryClickEvent(final InventoryClickEvent e) {
		final InventoryHolder ihold = e.getInventory().getHolder();
		if (!(ihold instanceof DCHolder)) {
			return;
		}
		if (e.getSlotType() == SlotType.OUTSIDE) {
			return;
		}
		 e.setCancelled(true);
		 final ItemStack item = e.getCurrentItem();
		 if (item == null) {
			 return;
		 }
		 if (item.getType() == Material.AIR) {
			 return;
		 }
		 if (item.getItemMeta().getDisplayName() == null) {
			 return;
		 }
		 final  Player p = (Player) e.getWhoClicked();
		 Cash cash = null;
		 if (!bank.hasCash(p)) {
			 bank.setCash(p, new Cash(0));
		 }
		 if (cash == null) {
			 cash = new Cash(0);
		 }
		 final  NBTItem rritem = new NBTItem(item);
		 if (!rritem.hasKey("DCAction")) {
			 return;
		 }
		 final  String akey = rritem.getString("DCAction");
		 if (akey == null) {
			 return;
		 }
		 final String rperm = rritem.getString("DCPerm");
		 if (!rperm.toUpperCase().equals("NONE")) {
		 if (!p.hasPermission(rperm)) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.75F);
			 final int clicked = e.getSlot();
			 final  Inventory inv = e.getClickedInventory();
			 inv.setItem(clicked, ItemStorage.getInstance().getDenyItem());
			 DCRunnable.getInstance().addTask(new DCTask() {
				 int liveticks = 40;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (liveticks > 1) return;
					inv.setItem(clicked, item);
				}

				@Override
				public void reduceTicks() {
					// TODO Auto-generated method stub
					liveticks--;
				}

				@Override
				public int getLiveTicks() {
					// TODO Auto-generated method stub
					return liveticks;
				}
				 
			 });
			 return;
		 }
		 }
		 final String bperm = rritem.getString("BLISTPERM");
		 if (!bperm.toUpperCase().equals("NONE")) {
		 if (!p.hasPermission("donatecraft.staff") && p.hasPermission(bperm)) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.65F);
			 final int clicked = e.getSlot();
			 final Inventory inv = e.getClickedInventory();
			 inv.setItem(clicked, ItemStorage.getInstance().getDenyItem2());
			 DCRunnable.getInstance().addTask(new DCTask() {
				 int liveticks = 40;
				@Override
				public final void runTask() {
					// TODO Auto-generated method stub
					if (liveticks > 1) return;
					inv.setItem(clicked, item);
				}

				@Override
				public final void reduceTicks() {
					// TODO Auto-generated method stub
					liveticks--;
				}

				@Override
				public final int getLiveTicks() {
					// TODO Auto-generated method stub
					return liveticks;
				}
			 });
			 return;
		 }
		 }
		 cash = bank.getCash(p);
		 int cost = 0;
		 cost = rritem.getInteger("DCCost");
		 final int ocost = cost;
		 if (rritem.hasKey("DCDiscount")) {
			 cost = (int) (cost - cost * rritem.getDouble("DCDiscount") / 100);
		 }
		 else {
			 cost = (int) (cost - cost * ds.getDiscount());
		 }
		 final int amount = cash.getCashAmount();
		 if (amount < cost) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.85F);
			 final int clicked = e.getSlot();
			 final  Inventory inv = e.getClickedInventory();
			 inv.setItem(clicked, ItemStorage.getInstance().getDenyItem3());
			 DCRunnable.getInstance().addTask(new DCTask() {
				 int liveticks = 40;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (liveticks > 1) return;
					inv.setItem(clicked, item);
				}

				@Override
				public void reduceTicks() {
					// TODO Auto-generated method stub
					liveticks--;
				}

				@Override
				public int getLiveTicks() {
					// TODO Auto-generated method stub
					return liveticks;
				}
			 });
			 final  int less = cost - amount;
			 for (String str : ls.getMessage(LongMessage.NO_CASH)) {
				 if (str.contains("%MORE%")) {
					 str = str.replaceAll("%MORE%", "" + less); 
				 }
				 if (str.contains("%CASH%")) {
					 str = str.replaceAll("%CASH%", "" + amount);
				 }
				 Utility.sendMsg(p, str);
			 }
			 return;
		 }
		 if (cost > 0) {
			 confirm.CreateConfirmation(e.getInventory(), p, cost, item, ocost);
			 return;
		 }
		 else {
			 final  Action action = actions.getAction(akey);
		 if (action != null) {
		 action.applyCommand(p);
		 }
		 e.setCancelled(true);
		 }
	}
	public final void ManagePlayerJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		final Cash cash = pd.getCash(p);
		UUIDCacher.getInstance().setUUID(e.getPlayer().getName(), e.getPlayer().getUniqueId());
	}
	public final void ManagePlayerQuit(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		final Cash cash = bank.getCash(p);
		if (cash == null) {
			return;
		}
		pd.setData(p, cash);

	}
}
