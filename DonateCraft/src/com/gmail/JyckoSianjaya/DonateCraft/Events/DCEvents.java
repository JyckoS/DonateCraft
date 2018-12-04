package com.gmail.JyckoSianjaya.DonateCraft.Events;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.JyckoSianjaya.DonateCraft.Objects.ConfirmationHolder;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.DCHolder;

public final  class DCEvents implements Listener {
	private final DCEventHandler eventhandler = DCEventHandler.getInstance();
	@EventHandler
	public final void onInventoryClick(final InventoryClickEvent e) {
		// Manage inventory click
		final InventoryHolder ie = e.getInventory().getHolder();
		if (ie instanceof DCHolder) {
		eventhandler.ManageInventoryClickEvent(e);
		}
		if (ie instanceof ConfirmationHolder) {
			eventhandler.ManageConfirmationClick(e);
		}
		
	}
	@EventHandler
	public final void onQuit(final PlayerQuitEvent e) {
		eventhandler.ManagePlayerQuit(e);
	}
	@EventHandler
	public final void onJoin(final PlayerJoinEvent e) {
		eventhandler.ManagePlayerJoin(e);
	}

}
