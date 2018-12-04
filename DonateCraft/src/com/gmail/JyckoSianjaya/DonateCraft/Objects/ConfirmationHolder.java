package com.gmail.JyckoSianjaya.DonateCraft.Objects;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public final class ConfirmationHolder implements InventoryHolder {
	private Inventory originalinventory;
	private int cost = 0;
	private int originalcost = 0;
	private ItemStack target;
	public ConfirmationHolder(final Inventory inv, final int cost, final ItemStack target, final int ocost) {
		this.originalinventory = inv;
		this.cost = cost;
		this.target = target;
		this.originalcost = ocost;
	}
	@Override
	public final Inventory getInventory() {
		// TODO Auto-generated method stub
		return originalinventory;
	}
	public final Inventory getOriginalInventory() {
		return originalinventory;
	}
	public final int getCost() {
		return cost;
	}
	public final ItemStack getTarget() {
		return target;
	}
	public final int getOriginalCost() {
		return originalcost;
	}
	
}
