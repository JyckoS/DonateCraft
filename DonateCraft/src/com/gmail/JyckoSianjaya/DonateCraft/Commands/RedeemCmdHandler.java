package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DCLogger;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.PlayerData;
import com.gmail.JyckoSianjaya.DonateCraft.Data.RedeemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Does;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public final class RedeemCmdHandler {
	private static RedeemCmdHandler instance;
	private final LangStorage ls = LangStorage.getInstance();
	private final RedeemStorage storage = RedeemStorage.getInstance();
	private final CashBank cashbank = CashBank.getInstance();
	private final DCLogger logger = DCLogger.getInstance();
	private final ACCashBank accb = ACCashBank.getInstance();
	private RedeemCmdHandler() {
	}
	public static final RedeemCmdHandler getInstance() {
		if (instance == null) {
			instance = new RedeemCmdHandler();
		}
		return instance;
	}
	public final void ManageRedeem(final CommandSender sender, final Command cmd, final String[] args) {
		if (!(sender instanceof Player)) {
			Utility.sendMsg(sender, "&cYou need to be a player to redeem!");
			return;
		}
		final Player p = (Player) sender;
		if (!p.hasPermission("donatecraft.redeem")) {
			Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
			return;
		}
		final int length = args.length;
		if (length < 1) {
			for (String str : ls.getMessage(LongMessage.REDEEM_HELP)) {
				Utility.sendMsg(p, str);
			}
			return;
		}
		final String code = args[0];
		if (!storage.hasCode(code)) {
			for (String str : ls.getMessage(LongMessage.REDEEM_FAIL)) {
				Utility.sendMsg(p, str);
			}
			return;
		}
		final RedeemCode rcode = storage.getRedeemCode(code);
		if (rcode.isUsed()) {
			for (String str : ls.getMessage(LongMessage.REDEEM_FAIL)) {
				Utility.sendMsg(p, str);
			}
			return;
		}
		rcode.used();
		final Cash cash = rcode.getCash();
		final int amount = cash.getCashAmount();
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

		final ACWallet awallet = accb.getACWallet(p);
		awallet.setAmount(awallet.getAmount() + amount);
		storage.removeCode(rcode);
		PlayerData.getInstance().setData(p, pcash);
		return;
	}

}
