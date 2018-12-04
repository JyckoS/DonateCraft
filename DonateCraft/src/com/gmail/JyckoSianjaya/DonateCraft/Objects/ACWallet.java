package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public final class ACWallet {
	private int amount = 0;
	public ACWallet(int amount) {
		this.amount = amount;
	}
	public final int getAmount() {
		return amount;
	}
	public final void addAmount(final int amount) {
		this.amount+=amount;
	}
	public final void setAmount(final int amount) {
		this.amount = amount;
	}
	public final void removeAmount(final int amount) {
		this.amount-=amount;
	}
}
