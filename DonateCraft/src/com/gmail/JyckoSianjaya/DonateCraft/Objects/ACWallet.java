package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public class ACWallet {
	private int amount = 0;
	public ACWallet(int amount) {
		this.amount = amount;
	}
	public int getAmount() {
		return amount;
	}
	public void addAmount(int amount) {
		this.amount+=amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void removeAmount(int amount) {
		this.amount-=amount;
	}
}
