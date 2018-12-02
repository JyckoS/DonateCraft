package com.gmail.JyckoSianjaya.DonateCraft.Objects;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ConfirmationHolder implements InventoryHolder {
	private Inventory originalinventory;
	private int cost = 0;
	private int originalcost = 0;
	private ItemStack target;
	public ConfirmationHolder(Inventory inv, int cost, ItemStack target, int ocost) {
		this.originalinventory = inv;
		this.cost = cost;
		this.target = target;
		this.originalcost = ocost;
	}
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return originalinventory;
	}
	public Inventory getOriginalInventory() {
		return originalinventory;
	}
	public int getCost() {
		return cost;
	}
	public ItemStack getTarget() {
		return target;
	}
	public int getOriginalCost() {
		return originalcost;
	}
	
}
