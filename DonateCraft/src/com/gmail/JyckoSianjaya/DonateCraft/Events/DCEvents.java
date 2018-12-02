package com.gmail.JyckoSianjaya.DonateCraft.Events;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.JyckoSianjaya.DonateCraft.Objects.ConfirmationHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.DCHolder;

public class DCEvents implements Listener {
	private DCEventHandler eventhandler = DCEventHandler.getInstance();
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		// Manage inventory click
		InventoryHolder ie = e.getInventory().getHolder();
		if (ie instanceof DCHolder) {
		eventhandler.ManageInventoryClickEvent(e);
		}
		if (ie instanceof ConfirmationHolder) {
			eventhandler.ManageConfirmationClick(e);
		}
		
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		eventhandler.ManagePlayerQuit(e);
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		eventhandler.ManagePlayerJoin(e);
	}

}
