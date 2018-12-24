package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public final class Stock extends Object {
	int stockamount = 10;
	public Stock(int amount) {
		this.stockamount = amount;
	}
	public void reduceAmount() {
		stockamount-=1;
	}
	public int getAmount() {
		return this.stockamount;
	}
	public void setAmount(int newamount) {
		this.stockamount = newamount;
	}
}
