package com.gmail.JyckoSianjaya.DonateCraft.Objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ActionStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DCLogger;
import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XMaterial;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public final class Action {
	private ArrayList<String> commands = new ArrayList<String>();
	private final ConsoleCommandSender console = Bukkit.getConsoleSender();
	public Action(final ArrayList<String> commands) {
		this.commands = commands;
	}
	public final void addCommand(String cmd) {
		commands.add(cmd);
	}
	public final boolean hasCommand(String cmd) {
		return commands.contains(cmd);
	}
	public final void removeCommand(String cmd) {
		commands.remove(cmd);
	}
	public final ArrayList<String> getCommands() {
		return commands;
	}
	public final void applyCommand(final Player p) {
		for (String str : commands) {
			if (str.contains("$p")) {
				str = str.replace("$p", p.getName());
			}
			if (str.contains("$cash")) {
				final CashBank cbi = CashBank.getInstance();
				int money = 0;
				if (cbi.hasCash(p)) {
					money = cbi.getCash(p).getCashAmount();
				}
				str = str.replaceAll("$cash", "" + money);
			}
			if (str.contains("$acash")) {
				final ACCashBank acb = ACCashBank.getInstance();
				int money = 0;
				if (acb.hasWallet(p)) {
					money = acb.getACWallet(p).getAmount();
				}
				str = str.replaceAll("$acash", "" + money);
			}
			if (str.charAt(0) == '%') {
				switch (str.substring(0, 5)) {
				case "%CGUI":
					p.closeInventory();
					break;
				case "%CANC":
					break;
				case "%OGUI":
					final String guiname = str.replaceAll("%OGUI", "").replaceAll(" ", "");
					// get a gui with that name
					// if gui = null cancels
					final Inventory gui = InventoryStorage.getInstance().getInventory(guiname, p);
					p.closeInventory();
					p.openInventory(gui);
					break;
				case "%BCST":
					final String broadcasts = Utility.TransColor(str.replaceAll("%BCST", ""));
					Utility.broadcast(broadcasts);
					break;
				case "%MSGS":
					final String message = Utility.TransColor(str.replaceAll("%MSGS", ""));
					Utility.sendMsg(p, message);
					break;
				case "%SNDS":
					final String astr = str.replaceAll("%SNDS", "");

					final String[] sound = astr.split("-");
					final Sound snd = XSound.requestXSound(sound[0]);
					final Float volume = Float.valueOf(sound[1]);
					final Float pitch = Float.valueOf(sound[2]);
					Utility.PlaySound(p, snd, volume, pitch);
					break;
				case "%TITL":
					final String ta = str.replaceAll("%TITL", "");
					final String[] titles = ta.split("-");
					final String title = titles[0];
					final int length = titles.length;
					String subtitle = "";
					if (length > 1) {
						subtitle = titles[1];
					}
					Utility.sendTitle(p, 10, 50, 10, title, subtitle);
					break;
				case "%ACBR":
					final String rr = str.replaceAll("%ACBR", "");
					Utility.sendActionBar(p, rr);
					break;
				case "%PCMD":
					final String cmd = str.replaceAll("%PCMD", "");
					p.performCommand(cmd);
					break;
				case "%ITEM":
					final String[] items = str.replaceAll("%ITEM", "").split("-");
					final String mat = items[0];
					final int iamount = Integer.valueOf(items[2]);
					final Material dmat = XMaterial.requestMaterial(mat, Byte.valueOf(" "));
					final Short durability = Short.valueOf(items[1]);
					final ItemStack citem = new ItemStack(dmat, iamount, durability);
					p.getInventory().addItem(citem);
				default:
					break;
				}
				continue;
			}
			executeConsole(str);
			
		}
	}
	private final void executeConsole(final String cmd) {
		Bukkit.dispatchCommand(console, cmd);
	}
}
