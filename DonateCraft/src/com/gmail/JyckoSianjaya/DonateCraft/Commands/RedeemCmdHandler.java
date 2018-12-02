package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DCLogger;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.RedeemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Does;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public class RedeemCmdHandler {
	private static RedeemCmdHandler instance;
	private LangStorage ls = LangStorage.getInstance();
	private RedeemStorage storage = RedeemStorage.getInstance();
	private CashBank cashbank = CashBank.getInstance();
	private DCLogger logger = DCLogger.getInstance();
	private ACCashBank accb = ACCashBank.getInstance();
	private RedeemCmdHandler() {
	}
	public static RedeemCmdHandler getInstance() {
		if (instance == null) {
			instance = new RedeemCmdHandler();
		}
		return instance;
	}
	public void ManageRedeem(CommandSender sender, Command cmd, String[] args) {
		if (!(sender instanceof Player)) {
			Utility.sendMsg(sender, "&cYou need to be a player to redeem!");
			return;
		}
		Player p = (Player) sender;
		if (!p.hasPermission("donatecraft.redeem")) {
			Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
			return;
		}
		int length = args.length;
		if (length < 1) {
			for (String str : ls.getMessage(LongMessage.REDEEM_HELP)) {
				Utility.sendMsg(p, str);
			}
			return;
		}
		String code = args[0];
		if (!storage.hasCode(code)) {
			for (String str : ls.getMessage(LongMessage.REDEEM_FAIL)) {
				Utility.sendMsg(p, str);
			}
			return;
		}
		RedeemCode rcode = storage.getRedeemCode(code);
		Cash cash = rcode.getCash();
		int amount = cash.getCashAmount();
		Cash pcash;
		if (cashbank.getCash(p) == null) {
			pcash = new Cash(0);
		}
		else {
			pcash = cashbank.getCash(p);
		}
		pcash.setCash(pcash.getCashAmount() + amount);
		if (ls.getDoes(Does.BROADCASTREDEEM)) {
		for (String str : ls.getMessage(LongMessage.BC_REDEEMSUCCESS)) {
			if (str.contains("%PLAYER%")) {
				str = str.replaceAll("%PLAYER%", p.getName());
			}
			if (str.contains("%CASH%")) {
				str = str.replaceAll("%CASH%", "" + amount);
			}
			Utility.broadcast(str);
			}
		}
		for (String str : ls.getMessage(LongMessage.REDEEM_SUCCESS)) {
			if (str.contains("%CASH%")) {
				str = str.replaceAll("%CASH%", "" + pcash.getCashAmount());
			}
			Utility.sendMsg(p, str);
		}
		logger.LogMessage("[REDEEM] " + p.getName() + " redeemed code: '" + code + "', receives " + amount + " cash, total cash: " + pcash.getCashAmount());
		cashbank.setCash(p, pcash);
		Utility.PlaySound(p, XSound.FIREWORK_TWINKLE.bukkitSound(), 1.0F, 2.0F);
		Utility.PlaySound(p, XSound.LEVEL_UP.bukkitSound(), 1.0F, 2.0F);

		ACWallet awallet = accb.getACWallet(p);
		awallet.setAmount(awallet.getAmount() + amount);
		storage.removeCode(rcode);
		rcode = null;
		return;
	}

}
