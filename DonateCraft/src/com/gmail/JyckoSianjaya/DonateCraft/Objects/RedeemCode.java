package com.gmail.JyckoSianjaya.DonateCraft.Objects;

public final class RedeemCode {
	private String passcode;
	private Cash amount;
	public RedeemCode(String code, Cash amount) {
		passcode = code;
		this.amount = amount;
	}
	public final void setCode(final String newcode) {
		this.passcode = newcode;
	}
	public final String getCode() {
		return passcode;
	}
	public final void setCash(Cash cash) {
		this.amount = cash;
	}
	public final Cash getCash() {
		return amount;
	}
}
