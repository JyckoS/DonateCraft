package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DonateCmdListener implements CommandExecutor {
	private AdminCmdHandler admincmd = AdminCmdHandler.getInstance();
	private RedeemCmdHandler redeemcmd = RedeemCmdHandler.getInstance();
	private StoreCmdHandler storecmd = StoreCmdHandler.getInstance();
	private CashCmdHandler cashcmd = CashCmdHandler.getInstance();
	private ACCashCmdHandler accmd = ACCashCmdHandler.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		String cmdname = cmd.getName().toLowerCase();
		switch (cmdname) {
		case "recoup":
		case "retrieve":
		case "redeem":
			redeemcmd.ManageRedeem(sender, cmd, args);
			return true;
		case "store":
			storecmd.HandleCommand(sender, cmd, args);
			return true;
		case "dc":
		case "dcr":
		case "donatecraft": 
			admincmd.ManageAdminCmd(sender, cmd, args);
			return true;
		case "cash":
			cashcmd.ManageCashCommand(sender, cmd, args);
			return true;
		case "acash":
		case "accumulatedcash":
		case "accash":
			accmd.ManageCmd(sender, cmd, args);
			return true;
		}
		// TODO Auto-generated method stub
		return true;
	}

}
