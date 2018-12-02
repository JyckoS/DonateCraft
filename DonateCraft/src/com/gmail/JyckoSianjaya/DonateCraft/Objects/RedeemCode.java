package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public class RedeemCode {
	private String passcode;
	private Cash amount;
	public RedeemCode(String code, Cash amount) {
		passcode = code;
		this.amount = amount;
	}
	public void setCode(String newcode) {
		this.passcode = newcode;
	}
	public String getCode() {
		return passcode;
	}
	public void setCash(Cash cash) {
		this.amount = cash;
	}
	public Cash getCash() {
		return amount;
	}
}
