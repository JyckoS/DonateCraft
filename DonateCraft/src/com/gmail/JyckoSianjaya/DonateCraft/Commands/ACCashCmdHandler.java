package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class ACCashCmdHandler {
	private static ACCashCmdHandler instance;
	private final LangStorage ls = LangStorage.getInstance();
	private final ACCashBank acb = ACCashBank.getInstance();
	private ACCashCmdHandler() {
	}
	public final static ACCashCmdHandler getInstance() {
		if (instance == null) {
			instance = new ACCashCmdHandler();
		}
		return instance;
	}
	public final void ManageCmd(final CommandSender sender, final Command cmd, final String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		if (!sender.hasPermission("accash.check")) {
			Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
			return;
		}
		final int alength = args.length;
		if (alength < 1) {
			ACWallet acw = null;
			int money = 0;
			if (acb.hasWallet(p)) {
				acw = acb.getACWallet(p);
				money = acw.getAmount();
			}
			for (String str : ls.getMessage(LongMessage.SHOW_ACASH)) {
				if (str.contains("%CASH%"))
					str = str.replaceAll("%CASH%", "" + money);
				Utility.sendMsg(sender, str);
			}
			return;
		}
		switch (args[0].toString().toLowerCase()) {
		case "top":
		case "leaderboard":
		case "tops":
			if (!sender.hasPermission("cash.top")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 2) {
				final ArrayList<String> arrays = acb.getTopBalance();
				for (String str : ls.getMessage(LongMessage.ACTOP_OPENING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "1");
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "10");
					}
					Utility.sendMsg(sender, str);
				}
				for (int i = 0; i < 9; i++) {
					try {
						arrays.get(i);
					} catch (IndexOutOfBoundsException e) {
						break;
					}
					String thebal = arrays.get(i);
					String[] info = thebal.split("~");
					String msg = ls.getMessage(Message.ACTOP_FORMAT).replaceAll("%RANK%", "" + (i + 1)).replaceAll("%p", info[0]).replaceAll("%cash%", info[1]);
					Utility.sendMsg(sender, msg);
				}
				for (String str : ls.getMessage(LongMessage.ACTOP_CLOSING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "1");
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "10");
					}
					Utility.sendMsg(sender, str);
				}
				return;
			}
			if (alength < 3) {
				int max = 0;
				try { 
					max = Integer.parseInt(args[1]);
				} catch (NumberFormatException lakad) {
					for (String str : ls.getMessage(LongMessage.NOT_NUMBER)) Utility.sendMsg(sender, str);
					return;
				}
				final ArrayList<String> cashs = acb.getTopBalance();
				if (max > cashs.size()) max = cashs.size();
				if (max < 10) max = 10;
				for (String str : ls.getMessage(LongMessage.ACTOP_OPENING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "" + (max - 9));
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "" + max);
					}
					Utility.sendMsg(sender, str);
				}
				for (int i = max - 10; i < max - 1; i++) {
					try {
						cashs.get(i);
					} catch (IndexOutOfBoundsException e) {
						break;
					}
					String thebal = cashs.get(i);
					String[] info = thebal.split("~");
					String msg = ls.getMessage(Message.ACTOP_FORMAT).replaceAll("%RANK%", "" + (i + 1)).replaceAll("%p", info[0]).replaceAll("%cash%", info[1]);
					Utility.sendMsg(sender, msg);
				}
				for (String str : ls.getMessage(LongMessage.ACTOP_CLOSING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "" + (max - 9));
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "" + max);
					}
					Utility.sendMsg(sender, str);
				}
				return;
			}
		case "set":
		case "put":
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash set <Player> <Amount>");
				return;
			}
			Player prr = Bukkit.getPlayer(args[1]);
			if (prr == null) {
				Utility.sendMsg(sender, "&c&l| &7Hmm.. Looks like that person doesn't exist!");
				return;
			}
			int aaamount = 0;
			try {
				aaamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet pwallet = null;
			if (acb.hasWallet(prr)) {
				pwallet = acb.getACWallet(prr);
			}			if (pwallet == null) {
				acb.setACWallet(prr, new ACWallet(aaamount));
				Utility.sendMsg(sender, "&a&l[!] &7Successfully changed &f" + prr.getName() + "&7's accumulated cash into &e" + aaamount + " &8(From 0)");
				return;
			}
			Utility.sendMsg(sender, "&a&l[!] &7Successfully changed &f" + prr.getName() + "&7's accumulated cash into &e" + aaamount + "&8(From " + pwallet.getAmount() + ")");
			pwallet.setAmount(aaamount);
			return;
		case "add":
		case "increase":
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash add <Player> <Amount>");
				return;
			}
			final Player eprr = Bukkit.getPlayer(args[1]);
			if (eprr == null) {
				Utility.sendMsg(sender, "&c&l| &7Hmm.. Looks like that person doesn't exist!");
				return;
			}
			int aamount = 0;
			try {
				aamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet awallet = null;
			if (acb.hasWallet(eprr)) {
				awallet = acb.getACWallet(eprr);
			}			
			if (awallet == null) {
				acb.setACWallet(eprr, new ACWallet(aamount));
				Utility.sendMsg(sender, "&a&l[!] &7Successfully &a&nincreased&7 &f" + eprr.getName() + "&7's accumulated cash into &e" + aamount + " &8(From 0)");
				return;
			}
			Utility.sendMsg(sender, "&a&l[!] &7Successfully &a&nincreased&r &f" + eprr.getName() + "&7's accumulated cash by &e" + aamount + "&8(From " + awallet.getAmount() + ") &7now have &e" + awallet.getAmount() + aamount);
			awallet.addAmount(aamount);
			return;
		case "remove":
		case "reduce":
		case "decrease":
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash remove <Player> <Amount>");
				return;
			}
			final Player rprr = Bukkit.getPlayer(args[1]);
			if (rprr == null) {
				Utility.sendMsg(sender, "&c&l| &7Hmm.. Looks like that person doesn't exist!");
				return;
			}
			int ramount = 0;
			try {
				aamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet rwallet = null;
			if (acb.hasWallet(rprr)) {
				rwallet = acb.getACWallet(rprr);
			}
			if (rwallet == null) {
				acb.setACWallet(rprr, new ACWallet(aamount));
				Utility.sendMsg(sender, "&a&l[!] &7Successfully &c&ndecreased&7 &f" + rprr.getName() + "&7's accumulated cash into &e" + "0" + " &8(From 0)");
				return;
			}
			rwallet.removeAmount(ramount);
			Utility.sendMsg(sender, "&a&l[!] &7Successfully &c&ndecreased&r &f" + rprr.getName() + "&7's accumulated cash by &e" + ramount + "&8(From " + rwallet.getAmount() + ") &7now have &e" + rwallet.getAmount() + ramount);
			return;
		default:
			if (!sender.hasPermission("accash.check.others")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 1) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the player name.&e /accash <Player>");
				return;
			}
			final Player plre = Bukkit.getPlayer(args[0]);
			if (plre == null) {
				Utility.sendMsg(sender, "&c&l| &7Hmm.. Looks like that person doesn't exist!");
				return;
			}
			ACWallet ppwallet = null;
			int money = 0;
			if (acb.hasWallet(plre)) {
				ppwallet = acb.getACWallet(plre);
				money = ppwallet.getAmount();
			}
			for (String cms : ls.getMessage(LongMessage.SHOW_ACASH_OTHERS)) {
				if (cms.contains("%CASH%")) cms = cms.replaceAll("%CASH%", money + "");
				if (cms.contains("%p")) cms = cms.replaceAll("%p", plre.getName());
				Utility.sendMsg(sender, cms);
			}
		}
	}
}
