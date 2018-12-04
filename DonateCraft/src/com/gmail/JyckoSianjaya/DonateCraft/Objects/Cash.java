package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public class Cash {
	private Integer amount = 0;
	public Cash(final int amount) {
		this.amount = amount;
	}
	public final Integer getCashAmount() { 
		return amount;
	}
	public final  void setCash(final int amount) {
		this.amount = amount;
	}
}
