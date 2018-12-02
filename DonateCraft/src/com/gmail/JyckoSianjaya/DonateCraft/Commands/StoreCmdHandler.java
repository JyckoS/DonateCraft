package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public class StoreCmdHandler {
	private static StoreCmdHandler instance;
	private InventoryStorage storage = InventoryStorage.getInstance();
	private StoreCmdHandler() {
	}
	public static StoreCmdHandler getInstance() {
		if (instance == null) {
			instance = new StoreCmdHandler();
		}
		return instance;
	}
	public void HandleCommand(CommandSender sender, Command cmd, String[] args) {
		// TODO Auto-generated method stub
		if (!(sender instanceof Player)) {
			Utility.sendMsg(sender, "&cOnly players can do that!");
			return;
		}
		Player p = (Player) sender;
		Inventory inv = storage.getInventory("Main");
		Utility.PlaySound(p, XSound.HORSE_BREATHE.bukkitSound(), 0.4F, 2.0F);
		Utility.PlaySound(p, XSound.NOTE_PLING.bukkitSound(), 0.7F, 2.0F);

		p.openInventory(inv);
	}
}
