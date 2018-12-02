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

public class Action {
	public ArrayList<String> commands = new ArrayList<String>();
	private ConsoleCommandSender console = Bukkit.getConsoleSender();
	public Action(ArrayList<String> commands) {
		this.commands = commands;
	}
	public void addCommand(String cmd) {
		commands.add(cmd);
	}
	public boolean hasCommand(String cmd) {
		return commands.contains(cmd);
	}
	public void removeCommand(String cmd) {
		commands.remove(cmd);
	}
	public ArrayList<String> getCommands() {
		return commands;
	}
	public void applyCommand(Player p) {
		for (String str : commands) {
			if (str.contains("$p")) {
				str = str.replace("$p", p.getName());
			}
			if (str.contains("$cash")) {
				CashBank cbi = CashBank.getInstance();
				int money = 0;
				if (cbi.hasCash(p)) {
					money = cbi.getCash(p).getCashAmount();
				}
				str = str.replaceAll("$cash", "" + money);
			}
			if (str.contains("$acash")) {
				ACCashBank acb = ACCashBank.getInstance();
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
					String guiname = str.replaceAll("%OGUI", "").replaceAll(" ", "");
					// get a gui with that name
					// if gui = null cancels
					Inventory gui = InventoryStorage.getInstance().getInventory(guiname);
					p.closeInventory();
					p.openInventory(gui);
					break;
				case "%BCST":
					String broadcasts = Utility.TransColor(str.replaceAll("%BCST", ""));
					Utility.broadcast(broadcasts);
					break;
				case "%MSGS":
					String message = Utility.TransColor(str.replaceAll("%MSGS", ""));
					Utility.sendMsg(p, message);
					break;
				case "%SNDS":
					String astr = str.replaceAll("%SNDS", "");

					String[] sound = astr.split("-");
					XSound snd = XSound.requestXSound(sound[0]);
					Sound dsound = snd.bukkitSound();
					Float volume = Float.valueOf(sound[1]);
					Float pitch = Float.valueOf(sound[2]);
					Utility.PlaySound(p, dsound, volume, pitch);
					break;
				case "%TITL":
					String ta = str.replaceAll("%TITL", "");
					String[] titles = ta.split("-");
					String title = titles[0];
					int length = titles.length;
					String subtitle = "";
					if (length > 1) {
						subtitle = titles[1];
					}
					Utility.sendTitle(p, 10, 50, 10, title, subtitle);
					break;
				case "%ACBR":
					String rr = str.replaceAll("%ACBR", "");
					Utility.sendActionBar(p, rr);
					break;
				case "%PCMD":
					String cmd = str.replaceAll("%PCMD", "");
					p.performCommand(cmd);
					break;
				case "%ITEM":
					String[] items = str.replaceAll("%ITEM", "").split("-");
					String mat = items[0];
					int iamount = Integer.valueOf(items[2]);
					Material dmat = XMaterial.requestMaterial(mat, Byte.valueOf(" "));
					Short durability = Short.valueOf(items[1]);
					ItemStack citem = new ItemStack(dmat, iamount, durability);
					p.getInventory().addItem(citem);
				default:
					break;
				}
				continue;
			}
			executeConsole(str);
			
		}
	}
	private void executeConsole(String cmd) {
		Bukkit.dispatchCommand(console, cmd);
	}
}
