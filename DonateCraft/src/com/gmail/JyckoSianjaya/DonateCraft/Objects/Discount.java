package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public class Discount {
	private Double amount = 0.0;
	public Discount(Double percentage) {
		this.amount = percentage;
	}
	public void setDiscount(double newamount) {
		this.amount = newamount;
	}
	public double getDiscount() {
		return this.amount;
	}
	public double getFinalPrice(double price) {
		return price * amount / 100;
	}
}
