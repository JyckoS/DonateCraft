package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.PlayerData;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.UUIDCacher;
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
		{
			if (!sender.hasPermission("accash.top")) {
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
				for (int i = 0; i < 10; i++) {
					if (i >= arrays.size()) break;
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
				if (max >= cashs.size()) max = cashs.size();
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
				for (int i = max - 10; i < max; i++) {
					if (i >= cashs.size()) break;
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
		}
		case "set":
		case "put":
		{
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash set <Player> <Amount>");
				return;
			}
			final Player prr = Bukkit.getPlayer(args[1]);
			UUID ruuid = null;
			if (prr == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				ruuid = get;
			}
			else {
				ruuid = prr.getUniqueId();
			}
			int aaamount = 0;
			try {
				aaamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet pwallet = null;
			if (acb.hasWallet(ruuid)) {
				pwallet = acb.getACWallet(ruuid);
			}			if (pwallet == null) {
				acb.setACWallet(ruuid, new ACWallet(aaamount));
				Utility.sendMsg(sender, "&a&l[!] &7Successfully changed &f" + args[1] + "&7's accumulated cash into &e" + aaamount + " &8(From 0)");
				String uud = ruuid.toString();
				DCRunnable.getInstance().addTask(new DCTask() {
					final UUID uu = UUID.fromString(uud);
					int health = 1;
					@Override
					public void runTask() {
						// TODO Auto-generated method stub
						PlayerData.getInstance().setData(uu);
					}
					@Override
					public void reduceTicks() {
						// TODO Auto-generated method stub
						health--;
					}
					@Override
					public int getLiveTicks() {
						// TODO Auto-generated method stub
						return health;
					}
				});
				return;
			}
			Utility.sendMsg(sender, "&a&l[!] &7Successfully changed &f" + args[1] + "&7's accumulated cash into &e" + aaamount + "&8(From " + pwallet.getAmount() + ")");
			pwallet.setAmount(aaamount);
			this.acb.setACWallet(ruuid, pwallet);
			String uud = ruuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					PlayerData.getInstance().setData(uu);
				}
				@Override
				public void reduceTicks() {
					// TODO Auto-generated method stub
					health--;
				}
				@Override
				public int getLiveTicks() {
					// TODO Auto-generated method stub
					return health;
				}
			});
			return;
		}
		case "add":
		case "increase":
		{
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash add <Player> <Amount>");
				return;
			}
			final Player eprr = Bukkit.getPlayer(args[1]);
			UUID ruuid = null;
			if (eprr == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				ruuid = get;
			}
			else {
				ruuid = eprr.getUniqueId();
			}
			int aamount = 0;
			try {
				aamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet awallet = null;
			if (acb.hasWallet(ruuid)) {
				awallet = acb.getACWallet(ruuid);
			}			
			if (awallet == null) {
				acb.setACWallet(ruuid, new ACWallet(aamount));
				Utility.sendMsg(sender, "&a&l[!] &7Successfully &a&nincreased&7 &f" + args[1] + "&7's accumulated cash into &e" + aamount + " &8(From 0)");
				String uud = ruuid.toString();
				DCRunnable.getInstance().addTask(new DCTask() {
					final UUID uu = UUID.fromString(uud);
					int health = 1;
					@Override
					public void runTask() {
						// TODO Auto-generated method stub
						PlayerData.getInstance().setData(uu);
					}
					@Override
					public void reduceTicks() {
						// TODO Auto-generated method stub
						health--;
					}
					@Override
					public int getLiveTicks() {
						// TODO Auto-generated method stub
						return health;
					}
				});
				return;
			}
			Utility.sendMsg(sender, "&a&l[!] &7Successfully &a&nincreased&r &f" + args[1] + "&7's accumulated cash by &e" + aamount + "&8(From " + awallet.getAmount() + ") &7now have &e" + awallet.getAmount() + aamount);
			awallet.addAmount(aamount);
			this.acb.setACWallet(ruuid, awallet);
			String uud = ruuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					PlayerData.getInstance().setData(uu);
				}
				@Override
				public void reduceTicks() {
					// TODO Auto-generated method stub
					health--;
				}
				@Override
				public int getLiveTicks() {
					// TODO Auto-generated method stub
					return health;
				}
			});
			return;
		}
		case "remove":
		case "reduce":
		case "decrease":
		{
			if (!sender.hasPermission("accash.admin")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 3) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the arguments: /accash remove <Player> <Amount>");
				return;
			}
			final Player rprr = Bukkit.getPlayer(args[1]);
			UUID ruuid = null;
			if (rprr == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				ruuid = get;
			}
			else {
				ruuid = rprr.getUniqueId();
			}
			int ramount = 0;
			try {
				ramount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7I believe, that's not a number.");
				return;
			}
			ACWallet rwallet = null;
			if (acb.hasWallet(ruuid)) {
				rwallet = acb.getACWallet(ruuid);
			}
			if (rwallet == null) {
				acb.setACWallet(ruuid, new ACWallet(ramount));
				String uud = ruuid.toString();
				DCRunnable.getInstance().addTask(new DCTask() {
					final UUID uu = UUID.fromString(uud);
					int health = 1;
					@Override
					public void runTask() {
						// TODO Auto-generated method stub
						PlayerData.getInstance().setData(uu);
					}
					@Override
					public void reduceTicks() {
						// TODO Auto-generated method stub
						health--;
					}
					@Override
					public int getLiveTicks() {
						// TODO Auto-generated method stub
						return health;
					}
				});
				Utility.sendMsg(sender, "&a&l[!] &7Successfully &c&ndecreased&7 &f" + args[1] + "&7's accumulated cash into &e" + "0" + " &8(From 0)");
				return;
			}
			rwallet.removeAmount(ramount);
			String uud = ruuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					PlayerData.getInstance().setData(uu);
				}
				@Override
				public void reduceTicks() {
					// TODO Auto-generated method stub
					health--;
				}
				@Override
				public int getLiveTicks() {
					// TODO Auto-generated method stub
					return health;
				}
			});
			this.acb.setACWallet(ruuid, rwallet);
			Utility.sendMsg(sender, "&a&l[!] &7Successfully &c&ndecreased&r &f" + args[1] + "&7's accumulated cash by &e" + ramount + "&8(From " + rwallet.getAmount() + ") &7now have &e" + rwallet.getAmount() + ramount);
			return;
		}
		default:
		{
			if (!sender.hasPermission("accash.check.others")) {
				Utility.sendMsg(sender, ls.getMessage(Message.NOPERM));
				return;
			}
			if (alength < 1) {
				Utility.sendMsg(sender, "&c&l| &7Please fill the player name.&e /accash <Player>");
				return;
			}
			final Player plre = Bukkit.getPlayer(args[0]);
			UUID target = null;
			String name = "";
			if (plre == null) {
					UUID get = UUIDCacher.getInstance().getUUID(args[0]);
					if (get == null) {
						Utility.sendMsg(sender, "&c&l| &7Hmm.. Looks like that person doesn't exist!");
						return;
					}
					target = get;
					name = UUIDCacher.getInstance().getNick(get);
			}
			else {
				target = plre.getUniqueId();
			}
			if (plre != null) {
				name = plre.getName();
			}
			ACWallet ppwallet = null;
			int money = 0;
			if (acb.hasWallet(target)) {
				ppwallet = acb.getACWallet(target);
				money = ppwallet.getAmount();
			}
			for (String cms : ls.getMessage(LongMessage.SHOW_ACASH_OTHERS)) {
				if (cms.contains("%CASH%")) cms = cms.replaceAll("%CASH%", money + "");
				if (cms.contains("%p")) cms = cms.replaceAll("%p", name);
				Utility.sendMsg(sender, cms);
			}
		}
		}
	}
}
