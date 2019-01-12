package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.PlayerData;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.LongMessage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCTask;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.UUIDCacher;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class CashCmdHandler {
	private static CashCmdHandler instance;
	private final LangStorage lang = LangStorage.getInstance();
	private final CashBank cashb = CashBank.getInstance();
	private CashCmdHandler() {
	}
	public static final CashCmdHandler getInstance() {
		if (instance == null) {
			instance = new CashCmdHandler();
		}
		return instance;
	}
	public final void ManageCashCommand(CommandSender sender, Command cmd, String[] args) {
		final int length = args.length;
		if (length < 1) {
			if (!(sender instanceof Player)) {
				Utility.sendMsg(sender, "&cOnly players!");
				return;
			}
			final Player p = (Player) sender;
			for (String str : lang.getMessage(LongMessage.SHOW_CASH)) {
				if (str.contains("%p")) {
					str = str.replaceAll("%p", p.getName());
				}
				if (str.contains("%CASH%")) {
					Cash cash = null;
					if (cashb.hasCash(p)) {
					cash = cashb.getCash(p.getUniqueId());
					}
					int money = 0;
					if (cash != null) {
						money = cash.getCashAmount();
					}
					str = str.replaceAll("%CASH%", "" + money);

				}
				Utility.sendMsg(sender, str);
			}
			if (p.hasPermission("cash.admin")) {
				Utility.sendMsg(sender, "&c&lAdmin Notes: &7You can use /cash &fset&7/&fadd&7/&fremove &7<Player> <Amount>");
			}
			return;
		}
		switch (args[0].toString().toLowerCase()) {
		case "top":
		case "tops":
		case "leaderboard":
			if (!sender.hasPermission("cash.top")) {
				Utility.sendMsg(sender, lang.getMessage(Message.NOPERM));
				return;
			}
			if (length < 2) {
				final ArrayList<String> arrays = cashb.getTopBalance();
				for (String str : lang.getMessage(LongMessage.CTOP_OPENING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "1");
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "10");
					}
					Utility.sendMsg(sender, str);
				}
				final int aalength = arrays.size();
				if (aalength < 10) {
					for (int i = 0; i < aalength; i++) {
						if (i >= aalength) break;
						final String thebal = arrays.get(i);
						final String[] info = thebal.split("~");
						final String msg = lang.getMessage(Message.CTOP_FORMAT).replaceAll("%RANK%", "" + (i + 1)).replaceAll("%p", info[0]).replaceAll("%cash%", info[1]);
						Utility.sendMsg(sender, msg);
					}
				}
				else {
					for (int i = 0; i < 10; i++) {
					if (i >= aalength) break;
					final String thebal = arrays.get(i);
					final String[] info = thebal.split("~");
					final String msg = lang.getMessage(Message.CTOP_FORMAT).replaceAll("%RANK%", "" + (i + 1)).replaceAll("%p", info[0]).replaceAll("%cash%", info[1]);
					Utility.sendMsg(sender, msg);
					}
				}
				for (String str : lang.getMessage(LongMessage.CTOP_CLOSING)) {
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
			if (length < 3) {
				int max = 0;
				try { 
					max = Integer.parseInt(args[1]);
				} catch (NumberFormatException lakad) {
					for (String str : lang.getMessage(LongMessage.NOT_NUMBER)) Utility.sendMsg(sender, str);
					return;
				}
				final ArrayList<String> cashs = cashb.getTopBalance();
				if (max > cashs.size()) max = cashs.size();
				if (max < 10) max = 10;
				for (String str : lang.getMessage(LongMessage.CTOP_OPENING)) {
					if (str.contains("%r")) {
						str = str.replaceAll("%r", "" + (max - 9));
					}
					if (str.contains("%m")) {
						str = str.replaceAll("%m", "" + max);
					}
					Utility.sendMsg(sender, str);
				}

				for (int i = max - 10 ; i < max - 1; i++) {
					if (i >= cashs.size()) break;
					final String thebal = cashs.get(i);
					final String[] info = thebal.split("~");
					final String msg = lang.getMessage(Message.CTOP_FORMAT).replaceAll("%RANK%", "" + (i + 1)).replaceAll("%p", info[0]).replaceAll("%cash%", info[1]);
					Utility.sendMsg(sender, msg);
				}
				for (String str : lang.getMessage(LongMessage.CTOP_CLOSING)) {
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
		{
			if (!sender.hasPermission("cash.admin")) {
				return;
			}
			if (length < 3) {
				Utility.sendMsg(sender, "&a&l| &7/cash set <Player> <Amount>");
				return;
			}
			final Player p = Bukkit.getPlayer(args[1]);
			UUID uuid = null;
			if (p == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				uuid = get;
			}
			else {
				uuid = p.getUniqueId();
			}
			int cashamount = 0;
			try {
			cashamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7That's not a number!");
				return;
			}
			final Cash cash = cashb.getCash(uuid);
			if (cash == null) {
			cashb.setCash(uuid, new Cash(cashamount));
			String uud = uuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (p != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;
					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
			Utility.sendMsg(sender, "&2&l| &7Successfuly changed the Cash amount for &f" + args[1] + "&7 into &f" + cashamount);
			return;
			}
			cash.setCash(cashamount);
			String uud = uuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (p != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;

					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
			Utility.sendMsg(sender, "&a&l| &7Successfuly changed the Cash amount for &f" + args[1] + "&7 into &f" + cashamount);
			return;
		}
		case "add":
		case "increase":
		{
			if (!sender.hasPermission("cash.admin")) {
				return;
			}
			if (length < 3) {
				Utility.sendMsg(sender, "&a&l| &7/cash add <Player> <Amount>");
				return;
			}
			final Player ap = Bukkit.getPlayer(args[1]);
			UUID auuid = null;
			if (ap == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				auuid = get;
			}
			else {
				auuid = ap.getUniqueId();
			}
			int addcashamount = 0;
			try {
			addcashamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7That's not a number!");
				return;
			}
			final ACCashBank acb = ACCashBank.getInstance();
			ACWallet wallet = acb.getACWallet(auuid);
			if (wallet == null) {
				acb.setACWallet(auuid, new ACWallet(0));
				wallet = acb.getACWallet(auuid);
			}
			wallet.addAmount(addcashamount);
			final Cash acash = cashb.getCash(auuid);
			if (acash == null) {
			cashb.setCash(auuid, new Cash(addcashamount));
			
			Utility.sendMsg(sender, "&2&l| &7Successfuly &aincreased&7 the Cash amount for &f" + args[1] + "&7 into &f" + addcashamount);
			String uud = auuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (ap != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;
					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
			final int currcash = acash.getCashAmount();
			acash.setCash(addcashamount+currcash);
			Utility.sendMsg(sender, "&a&l| &7Successfuly &aincreased&7 the Cash amount for &f" + args[1] + "&7 into &f" + (addcashamount+currcash));
			String uud = auuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (ap != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;
					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
		case "decrease":
		case "take":
		{
			if (!sender.hasPermission("cash.admin")) {
				return;
			}
			if (length < 3) {
				Utility.sendMsg(sender, "&a&l| &7/cash take <Player> <Amount>");
				return;
			}
			final Player rp = Bukkit.getPlayer(args[1]);
			UUID ruuid = null;
			if (rp == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[1]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				ruuid = get;
			}
			else {
				ruuid = rp.getUniqueId();
			}
			int rcashamount = 0;
			try {
			rcashamount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l| &7That's not a number!");
				return;
			}
			final Cash rcash = cashb.getCash(ruuid);
			if (rcash == null) {
			cashb.setCash(ruuid, new Cash(0));
			String uud = ruuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (rp != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;
					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
			Utility.sendMsg(sender, "&2&l| &7Successfuly &cdecreased&7 the Cash amount for &f" + args[1] + "&7 into &f" + 0);
			return;
			}
			final int currdcash = rcash.getCashAmount();
			int fina = currdcash - rcashamount;
			if (fina < 0) {
				fina = 0;
			}
			rcash.setCash(fina);
			String uud = ruuid.toString();
			DCRunnable.getInstance().addTask(new DCTask() {
				final UUID uu = UUID.fromString(uud);
				int health = 1;
				Boolean run = false;
				@Override
				public void runTask() {
					// TODO Auto-generated method stub
					if (rp != null || !DataStorage.getInstance().useSQL()) {
					PlayerData.getInstance().setData(uu);
					return;
					}
					if (run) return;
					run = true;
					PlayerData.getInstance().saveData(PlayerData.getInstance().getSQLData(uu));
					return;
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
			Utility.sendMsg(sender, "&a&l| &7Successfuly &cdecreased&7 the Cash amount for &f" + args[1] + "&7 into &f" + fina);
			return;
		}
		default:
		{
			final Player play = Bukkit.getPlayer(args[0]);
			UUID ruuid = null;
			if (play == null) {
				UUID get = UUIDCacher.getInstance().getUUID(args[0]);
				if (get == null) {
					Utility.sendMsg(sender, "&7That player NEVER existed.");
					return;
				}
				ruuid = get;
			}
			else {
				ruuid = play.getUniqueId();
			}
			Cash cashe = null;
			if (cashb.hasCash(ruuid)) {
			cashe = cashb.getCash(ruuid);
			}
			if (cashe == null && play == null && DataStorage.getInstance().useSQL()) {
				cashe = PlayerData.getInstance().getSQLCash(ruuid);
			}
			String name = args[0];
			if (play != null) name = play.getName();
			int money = 0;
			if (cashe != null) {
				money = cashe.getCashAmount();
			}
			for (String str : lang.getMessage(LongMessage.SHOW_CASH_OTHERS)) {
				if (str.contains("%p")) {
					str = str.replaceAll("%p", name);
				}
				if (str.contains("%CASH%")) {
					str = str.replaceAll("%CASH%", "" + money);
				}
				Utility.sendMsg(sender, str);
			}
			return;
		}
		}
	}
}
