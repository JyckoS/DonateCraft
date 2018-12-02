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
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.NBT.NBTItem;


public class DCEventHandler {
	private static DCEventHandler instance;
	private ActionStorage actions = ActionStorage.getInstance();
	private CashBank bank = CashBank.getInstance();
	private PlayerData pd = PlayerData.getInstance();
	private LangStorage ls = LangStorage.getInstance();
	private DataStorage ds = DataStorage.getInstance();
	private ItemStorage is = ItemStorage.getInstance();
	private DCLogger loger = DCLogger.getInstance();
	private ConfirmationManager confirm = ConfirmationManager.getInstance();
	private DCEventHandler() {
	}
	public static DCEventHandler getInstance() {
		if (instance == null) {
			instance = new DCEventHandler();
		}
		return instance;
	}
	public void ManageConfirmationClick(InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.OUTSIDE) {
			return;
		}
		e.setCancelled(true);
		 ItemStack item = e.getCurrentItem();
		 if (item == null) {
			 return;
		 }
		 if (item.getType() == Material.AIR) {
			 return;
		 }
		 if (item.getItemMeta().getDisplayName() == null) {
			 return;
		 }
		 Player p = (Player) e.getWhoClicked();
		 Cash cash = null;
		 if (!bank.hasCash(p)) {
			 bank.setCash(p, new Cash(0));
		 }
		 if (cash == null) {
			 cash = new Cash(0);
		 }
		 cash = bank.getCash(p);
			ConfirmationHolder ch = (ConfirmationHolder) e.getInventory().getHolder();
		 NBTItem rritem = new NBTItem(item);
		 if (!rritem.hasKey("custom_actiontype")) {
			 return;
		 }
		 String cckey = rritem.getString("custom_actiontype");
		 switch (cckey.toLowerCase()) {
		 case "yes":
			 ItemStack target = ch.getTarget();
			 NBTItem titem = new NBTItem(target);
			 String akey = titem.getString("DCAction");
			 NBTItem citem = new NBTItem(item);
			 String ckey = citem.getString("CCAction");
			 if (akey == null) {
				 return;
			 }
			 int cost = 0;
			 cost = titem.getInteger("DCCost");
			 cost = (int) (cost - cost * ds.getDiscount());
			 int amount = cash.getCashAmount();
			 if (amount < cost) {
				 int less = cost - amount;
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
			 if (cost > 0) {
				 loger.LogMessage("[PURCHASE] " + p.getName() + " bought action '" + akey + "' for " + cost + ", cash left: " + cash.getCashAmount() + "(DISCOUNT: " + ds.getRawDiscount() + "%)");
			 }
			 p.closeInventory();
			 if (actions.getAction(ckey) != null) {
			 Action caction = actions.getAction(ckey);
			 caction.applyCommand(p);
			 }
			 Action action = actions.getAction(akey);
			 action.applyCommand(p);
			 e.setCancelled(true); 
			 return;
		 case "no":
			 String dkey = rritem.getString("CCAction");
			 Action taction = actions.getAction(dkey);
			 Inventory oginv = ch.getOriginalInventory();
			 p.openInventory(oginv);
			 taction.applyCommand(p);
			 return;
		 case "var":
			 String rcaction =  rritem.getString("CCAction");
			 Action ddaction = actions.getAction(rcaction);
			 ddaction.applyCommand(p);
			 return;
			 default:
				 return;
		 }
		
	}
	public void ManageInventoryClickEvent(InventoryClickEvent e) {
		InventoryHolder ihold = e.getInventory().getHolder();
		if (!(ihold instanceof DCHolder)) {
			return;
		}
		if (e.getSlotType() == SlotType.OUTSIDE) {
			return;
		}
		 e.setCancelled(true);
		 ItemStack item = e.getCurrentItem();
		 if (item == null) {
			 return;
		 }
		 if (item.getType() == Material.AIR) {
			 return;
		 }
		 if (item.getItemMeta().getDisplayName() == null) {
			 return;
		 }
		 Player p = (Player) e.getWhoClicked();
		 Cash cash = null;
		 if (!bank.hasCash(p)) {
			 bank.setCash(p, new Cash(0));
		 }
		 if (cash == null) {
			 cash = new Cash(0);
		 }
		 NBTItem rritem = new NBTItem(item);
		 if (!rritem.hasKey("DCAction")) {
			 return;
		 }
		 String akey = rritem.getString("DCAction");
		 if (akey == null) {
			 return;
		 }
		 String rperm = rritem.getString("DCPerm");
		 if (!rperm.toUpperCase().equals("NONE")) {
		 if (!p.hasPermission(rperm)) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.75F);
			 int clicked = e.getSlot();
			 Inventory inv = e.getClickedInventory();
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
		 String bperm = rritem.getString("BLISTPERM");
		 if (!bperm.toUpperCase().equals("NONE")) {
		 if (!p.hasPermission("donatecraft.staff") && p.hasPermission(bperm)) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.65F);
			 int clicked = e.getSlot();
			 Inventory inv = e.getClickedInventory();
			 inv.setItem(clicked, ItemStorage.getInstance().getDenyItem2());
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
		 cash = bank.getCash(p);
		 int cost = 0;
		 cost = rritem.getInteger("DCCost");
		 int ocost = cost;
		 cost = (int) (cost - cost * ds.getDiscount());
		 int amount = cash.getCashAmount();
		 if (amount < cost) {
			 Utility.PlaySound(p, XSound.VILLAGER_NO.bukkitSound(), 0.4F, 0.85F);
			 int clicked = e.getSlot();
			 Inventory inv = e.getClickedInventory();
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
			 int less = cost - amount;
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
		 Action action = actions.getAction(akey);
		 if (action != null) {
		 action.applyCommand(p);
		 }
		 e.setCancelled(true);
		 }
	}
	public void ManagePlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Cash cash = pd.getCash(p);
	}
	public void ManagePlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Cash cash = bank.getCash(p);
		if (cash == null) {
			return;
		}
		try {
			pd.setData(p, cash);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Utility.sendConsole("[DC] Can't save data for Player &e" + p.getName() + "&r!");
		}
	}
}
