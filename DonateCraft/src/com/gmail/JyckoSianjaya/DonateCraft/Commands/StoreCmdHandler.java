package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public final class StoreCmdHandler {
	private static StoreCmdHandler instance;
	private final InventoryStorage storage = InventoryStorage.getInstance();
	private StoreCmdHandler() {
	}
	public final static StoreCmdHandler getInstance() {
		if (instance == null) {
			instance = new StoreCmdHandler();
		}
		return instance;
	}
	public final void HandleCommand(final CommandSender sender, final Command cmd, final String[] args) {
		// TODO Auto-generated method stub
		if (!(sender instanceof Player)) {
			Utility.sendMsg(sender, "&cOnly players can do that!");
			return;
		}
		final Player p = (Player) sender;
		final Inventory inv = storage.getInventory("Main", p);
		Utility.PlaySound(p, XSound.NOTE_PLING.bukkitSound(), 0.7F, 2.0F);
		p.openInventory(inv);
	}
}
