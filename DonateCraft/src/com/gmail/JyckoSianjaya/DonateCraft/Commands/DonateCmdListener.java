package com.gmail.JyckoSianjaya.DonateCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class DonateCmdListener implements CommandExecutor {
	private final AdminCmdHandler admincmd = AdminCmdHandler.getInstance();
	private final RedeemCmdHandler redeemcmd = RedeemCmdHandler.getInstance();
	private final StoreCmdHandler storecmd = StoreCmdHandler.getInstance();
	private final CashCmdHandler cashcmd = CashCmdHandler.getInstance();
	private final ACCashCmdHandler accmd = ACCashCmdHandler.getInstance();
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String cmdlabel, final String[] args) {
		final String cmdname = cmd.getName().toLowerCase();
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
