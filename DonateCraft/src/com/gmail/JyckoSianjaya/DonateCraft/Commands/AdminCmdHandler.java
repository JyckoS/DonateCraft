package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ActionStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ItemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.NumberStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.RandomResult;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage.Message;
import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.ConfirmationManager;
import com.gmail.JyckoSianjaya.DonateCraft.Data.RedeemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Action;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Stock;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.UUIDCacher;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public final class AdminCmdHandler {
	private final LangStorage lstorage = LangStorage.getInstance();
	private static AdminCmdHandler instance;
	private final InventoryStorage istorage = InventoryStorage.getInstance();
	private final ActionStorage astorage = ActionStorage.getInstance();
	private final ItemStorage Istorage = ItemStorage.getInstance();
	private final DonateCraft minstance = DonateCraft.getInstance();
	private AdminCmdHandler() {
	}
	public static final AdminCmdHandler getInstance() {
		if (instance == null) {
			instance = new AdminCmdHandler();
		}
		return instance;
	}
	public final void ManageAdminCmd(final CommandSender sender, final Command cmd, final String[] args) {
		if (!sender.hasPermission("donatecraft.admin")) {
			Utility.sendMsg(sender, " ");
			Utility.sendMsg(sender, " &7Running  &6&lDonate&e&lCraft &8&lV.&8" + minstance.getDescription().getVersion());
			Utility.sendMsg(sender, "    &7Made by &f&oJicko Sianjaya &7(Gober)");
			Utility.sendMsg(sender, " ");
			return;
		}
		final int length = args.length;
		if (length < 1) {
			Utility.sendMsg(sender, " ");
			Utility.sendMsg(sender, "        &6&lDonate&e&lCraft &8&lV.&8" + minstance.getDescription().getVersion());
			Utility.sendMsg(sender, "    &7Type '&f/dc help&7' for list of commands.");
			Utility.sendMsg(sender, " ");
			return;
		}
		switch (args[0].toLowerCase()) {
		case "list":
		case "lists":
			if (length < 2) {
				Utility.sendMsg(sender, "&c&l█ &7Please use /dc list ACTIONS, CODES, GUI, ITEMS");
				return;
			}
			else {
			final String arg = args[1].toUpperCase();
			switch (arg) {
			case "ACTION":
			case "ACTIONS":
			case "ACTS":
			case "ACT":
				final Collection<String> actions = astorage.getKeys();
				Utility.sendMsg(sender, "&e&l&nActions");
				String rractions = "&7There is: ";
				for (final String str : actions) {
					rractions = rractions + "&e" + str + "&7, ";
				}
				Utility.sendMsg(sender, rractions);
				return;
			case "CODES":
			case "REDEEMS":
			case "REDEEMCODES":
			case "RCODE":
			case "CODE":
				final List<RedeemCode> codes = RedeemStorage.getInstance().getRedeemCodes();
				Utility.sendMsg(sender, "&b&l&nCODES");
				String fcode = "&7Codes: ";
				for (final RedeemCode code : codes) {
					final String ic = code.getCode();
					final int amount = code.getCash().getCashAmount();
					fcode = fcode + "&a" + ic + "&9(&b" + amount + "&9)" + "&7, ";
				}
				Utility.sendMsg(sender, fcode);
				return;
			case "GUI":
				final Collection<String> guiname = istorage.getKeys();
				Utility.sendMsg(sender, "&6&l&m--&6&l> &eGUI(s)");
				String cguis = "&7There are: &f";
				for (final String str : guiname) {
					cguis = cguis + "&e" + str + "&7, ";
				}
				Utility.sendMsg(sender, cguis);
				return;
			case "ITEMS":
			case "ITEM":
				final ItemStorage itemstorage = ItemStorage.getInstance();
				final Collection<String> items = itemstorage.getKeys();
				String item = "&7The available items are: &a";
				for (final String str : items) {
					item = item + str + "&7, &a";
				}
				Utility.sendMsg(sender, item);
				return;
			}
			}
		case "help":
			Utility.sendMsg(sender, "             &6&l&nDonate&e&l&nCraft&r        ");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7open &f<GUINAME>");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7createcode &f<CODE> &7or &dRANDOM");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7actions &F<Action>");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7removecode <CODE>");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7getItem &f<KEY>");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7list &fACTION&7, &fCODE&7, &fGUI&7, &fITEMS");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7discount <PERCENTAGE>");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &4reload ");
			Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &aupdates");
			Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &atop <POINT>");
			Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &aADD&f/&aREMOVE&f/&aSET <PLAYER> <AMOUNT>");
			Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &a<PLAYER>");
			Utility.sendMsg(sender, "          ");
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.BAT_TAKEOFF.bukkitSound(), 1.0F, 0.5F);
			}
			return;
			default:
				Utility.sendMsg(sender, "             &6&l&nDonate&e&l&nCraft&r        ");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7open &f<GUINAME>");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7createcode &f<CODE> &7or &dRANDOM");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7actions &f<Action>");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7removecode <CODE>");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7getItem &f<KEY>");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7list &fACTION&7, &fCODE&7, &fGUI&7, &fITEMS");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &7discount <PERCENTAGE>");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &4reload");
				Utility.sendMsg(sender, "    &e&l&m&e&l>  &6/dc &aupdates");

				Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &atop <POINT>");
				Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &aADD&f/&aREMOVE&f/&aSET <PLAYER> <AMOUNT>");
				Utility.sendMsg(sender, "    &a&l&m&a&l>  &2/acash &7or &e/cash &a<PLAYER>");
				Utility.sendMsg(sender, "          ");
				if (isPlayer(sender)) {
					Utility.PlaySound((Player) sender, XSound.BAT_TAKEOFF.bukkitSound(), 1.0F, 0.5F);
				}
				return;
			case "setstock":
			{
			if (length < 3) {
				Utility.sendMsg(sender, "&c| &7/dc setstock <ITEM> <AMOUNT>");
				return;
			}
			String item = args[1];
			if (ItemStorage.getInstance().getItem(item) == null) {
				Utility.sendMsg(sender, "&c| &7OOPS! &fThat item doesn't exist!");
				return;
			}
			int amount = 0;
			try {
				amount = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c| &7OOPS! &fThat's not a number!");
				return;
			}
			DataStorage.getInstance().setStock(item, new Stock(amount));
			Utility.sendMsg(sender, "&a&l ! &7Successfully changed the stock for &f" + item + "&7 into &e&l" + amount);
			return;
			}
			case "checkuuid":
			case "getuuid":
			case "uuid":
			{
				if (length < 2) return;
				final Player p = Bukkit.getPlayer(args[1]);
				if (p == null) {
					if (UUIDCacher.getInstance().getUUID(args[1].toLowerCase()) == null) {
						return;
					}
					Utility.sendMsg(sender, UUIDCacher.getInstance().getUUID(args[1].toLowerCase()).toString());
					return;
				}
				Utility.sendMsg(sender, p.getUniqueId().toString());
				return;
			}
			case "getitem":
				if (!isPlayer(sender)) {
					Utility.sendMsg(sender, "&eYou're not player!");
					return;
				}
				final Player player = (Player) sender;
				if (length < 2) {
					Utility.sendMsg(sender, "&7█ &6/dc &cgetItem <ITEMNAME>");
					return;
				}
			final	String itemkey = args[1];
				if (Istorage.getItem(itemkey) == null) {
					Utility.sendMsg(player, "&c&l█ &7That item doesn't exist!");
					return;
				}
				final ItemStack item = Istorage.getItem(itemkey);
				player.getInventory().addItem(item);
				Utility.sendMsg(player, "&a&l█ &7Item successfully added to your inventory!");
				if (isPlayer(sender)) {
					Utility.PlaySound((Player) sender, XSound.CHICKEN_EGG_POP.bukkitSound(), 1.0F, 1.5F);
				}
				return;
			case "update":
			case "updates":
				// Url for Plugin Version is: https://pastebin.com/hfq3rsZx
				URL url = null;
				Scanner scanner = null;
				try {
				url = new URL("https://pastebin.com/raw/hfq3rsZx");
				scanner = new Scanner(url.openStream());
				final String str = scanner.nextLine();
				final PluginDescriptionFile dcpf = DonateCraft.getInstance().getDescription();
				final String dc = dcpf.getVersion();
				scanner.close();
				if (isPlayer(sender)) {
					Utility.PlaySound((Player) sender, XSound.VILLAGER_YES.bukkitSound(), 1.0F, 1.5F);
				}
				if (!str.equals(dc)) {
					Utility.sendMsg(sender, "&b<< UPDATE >> &7New update for &eDonateCraft &7is available (Version: " + str + "), check them out in '&fLink coming soon, waiting for the resource staff to accept.&7'");
					return;
				}
				else {
					Utility.sendMsg(sender, "&6[DC] &eYou are using the latest version: &6V" + dc);
				}
				} catch (Exception e) {
					Utility.sendMsg(sender, "&6[DC] &cCouldn't get the latest version for DonateCraft.");
					return;
				}
				return;
		case "open":
		case "see":
			if (!isPlayer(sender)) {
				Utility.sendMsg(sender, "&c&l█ &eYou're not player!");
				return;
			}
			if (length < 2) {
				Utility.sendMsg(sender, "&7█ &6/dc &copen <GUINAME> &7<PlAYER>");
				return;
			}
			final String guiname = args[1];
			if (!istorage.hasKey(guiname)) {
				Utility.sendMsg(sender, "&c&lOops! &7That GUI doesn't exist!");
				return;
			}
			Inventory inventory = null;
			inventory = istorage.getInventory(guiname, (Player) sender);
			Player p = null;
			if (length < 3) {
				p = (Player) sender;
			}
			else {
				p = Bukkit.getPlayer(args[2]);
				if (p == null) {
					Utility.sendMsg(sender, "&c&lOops! &7That person doesn't exist!");
					return;
				}
			}
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.NOTE_PLING.bukkitSound(), 1.0F, 1.5F);
			}
			p.openInventory(inventory);
			return;
		case "removecode":
			if (length < 2) {
				Utility.sendMsg(sender, "&c&l█ &7Please use &e/dc removecode &9<KEY>");
				return;
			}
			final String removecode = args[1];
			final RedeemStorage rs = RedeemStorage.getInstance();
			if (removecode.equals("all")) {
				rs.clearCodes();
				Utility.sendMsg(sender, "&a&l█ &7Successfully cleaned up all redeem codes, no redeem codes left.");
				return;
			}
			if (!rs.hasCode(removecode)) {
				Utility.sendMsg(sender, "&c&l█ &7That code doesn't exist!");
				return;
			}
			final RedeemCode code = rs.getRedeemCode(removecode);
			rs.removeCode(code);
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.ANVIL_LAND.bukkitSound(), 1.0F, 0.5F);
			}
			Utility.sendMsg(sender, "&c&l█ &7Removed code &f" + removecode + "&7, it's no longer available.");
			return;
		case "actions":
		case "action":
			if (length < 2) {
			String actions = "&a&lActions: &6";
			final Collection<String> aa = astorage.getKeys();
			for (final String act : aa) {
				actions = actions + act + "&7, &6";
			}
			Utility.sendMsg(sender, "actions");
			return;
			}
			final String actkey = args[1];
			final Action act = astorage.getAction(actkey);
			if (act == null) {
				Utility.sendMsg(sender, "&c&l█ &7That action does not exist!");
				return;
			}
			Utility.sendMsg(sender, "&e&l&m          &r &6&lCommands &e&l&m          ");
			Utility.sendMsg(sender, "   ");
			for (final String ac : act.getCommands()) {
				Utility.sendMsg(sender, ac);
			}
			Utility.sendMsg(sender, "   ");
			Utility.sendMsg(sender, "&e&l&m                                          ");
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.BAT_TAKEOFF.bukkitSound(), 1.0F, 0.5F);
			}
			return;
		case "discount":
		case "diskon":
		case "disc":
			if (length < 2) {
				final Double cd = DataStorage.getInstance().getRawDiscount();
				Utility.sendMsg(sender, "&a&l█ &aYou mean current discount? It's &f" + cd + "&f&o%");
				Utility.sendMsg(sender, "&7█ &6/dc &cdiscount <PERCENTAGE> &7- e.g: 30, 40, etc. Means discount 30 percent.");
				return;
			}
			Double discount = 0.0;
			try {
			      discount = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&eThat's not a double!");
				return;
			}
			final DataStorage dstorage = DataStorage.getInstance();
			dstorage.setDiscount(discount);
			final File fconfig = new File(DonateCraft.getInstance().getDataFolder(), "config.yml");
			final YamlConfiguration config = YamlConfiguration.loadConfiguration(fconfig);
			config.set("current_discount", discount);
			try {
				config.save(fconfig);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Utility.sendMsg(sender, "&4&l█ &7Oops! Failed to save Config.yml for discount!");
			}
			Utility.sendMsg(sender, "&a&l█ &7Successfully set the discount to &a" + discount + "% &7");
			Utility.sendMsg(sender, " ");
			Utility.sendMsg(sender, "&7Result Example: &a&o$1000");
			final Double discounts = 1000 * discount / 100;
			final Double discounted = 1000 - discounts;
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.BAT_TAKEOFF.bukkitSound(), 1.0F, 0.5F);
			}
			Utility.sendMsg(sender, "&7After Discount Result: &a$1000 &f- &a$" + discounts + " &f= " + discounted);
			return;
		case "createcode":
		case "newcode":
			if (length < 2) {
				Utility.sendMsg(sender, "&c&l█ &7Use /dc createcode <CODE> or use RANDOM to let me do the Magic for you ;)");
				return;
			}
			if (length < 3) {
				Utility.sendMsg(sender, "&c&l█ &7Please specify how many amount of Cash!");
				return;
			}
			try {
				Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				Utility.sendMsg(sender, "&c&l█ &7That's not a number");
				return;
			}
			final Cash result = new Cash(Integer.parseInt(args[2]));
			final String newcode = args[1];
			for (RedeemCode a : RedeemStorage.getInstance().getRedeemCodes()) {
				if (a.getCode().equals(newcode)) {
					Utility.sendMsg(sender, "&c&l█ &7Oops, there is also a code named: '&f&o" + newcode + "&e'");
					return;
				}
			}
			if (newcode.toLowerCase().equals("random")) {
				String base = "";
				final RandomResult res = NumberStorage.getInstance().getResults();
				String val1 = "";
				String val2 = "";
				String val3 = "";
					for (int si = 0; si < 5; si++) {
						val1+=res.getRandom();
					}
					for (int si = 0; si < 5; si++) {
						val2+=res.getRandom();
					}
					for (int si = 0; si < 5; si++) {
						val3+=res.getRandom();
					}
				base = val1 + "-" + val2 + "-" + val3;
				for (final RedeemCode a : RedeemStorage.getInstance().getRedeemCodes()) {
					if (a.getCode() == base) {
						Utility.sendMsg(sender, "&c&l█ &7Oops, there is also a code named: '&f&o" + newcode + "&e'");
						return;
					}
				}
				Utility.sendMsg(sender, "&a&l█ &eYou created new random redeem code: '&f&o" + base + "&e'");
				RedeemStorage.getInstance().addCode(new RedeemCode(base, result));
				return;
			}
			for (final RedeemCode a : RedeemStorage.getInstance().getRedeemCodes()) {
				if (a.getCode().equals(newcode)) {
					Utility.sendMsg(sender, "&c&l█ &7Oops, there is also a code named: '&f&o" + newcode + "&e'");
					return;
				}
			}
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.HORSE_BREATHE.bukkitSound(), 0.8F, 1.8F);
			}
			Utility.sendMsg(sender, "&a&l█ &eYou created new redeem code: '&f&o" + newcode + "&e' &6(&e" + result.getCashAmount() + "&6)");
			RedeemStorage.getInstance().addCode(new RedeemCode(newcode, result));
			if (isPlayer(sender)) {
				Utility.PlaySound((Player) sender, XSound.FIREWORK_BLAST.bukkitSound(), 1.0F, 0.5F);
			}
			return;
		case "reload":
			ActionStorage.getInstance().LoadActions();
			DataStorage.getInstance().LoadConfig();
			InventoryStorage.getInstance().LoadFiles();
			ItemStorage.getInstance().LoadFiles();
			LangStorage.getInstance().LoadMessage();
			Utility.sendMsg(sender, "&a&l█ &7Successfully &fRELOADED &7the &oActions.yml, config.yml, GUI.yml, Items.yml&7 and lang.yml&7!");
			return;
		}
	}
	private boolean isPlayer(final CommandSender sender) {
		return sender instanceof Player;
	}
	private String Zerofy(final int amount) {
		String finale = "";
		if (amount < 10) {
			finale = "000" + amount;
			return finale;
		}
		if (amount < 100) {
			finale = "00" + amount;
			return finale;
		}
		if (amount < 1000) {
			finale = "0" + amount;
			return finale;
		}
		if (amount < 10000) {
			finale = "" + amount;
			return finale;
		}
		return finale;
	}
}
