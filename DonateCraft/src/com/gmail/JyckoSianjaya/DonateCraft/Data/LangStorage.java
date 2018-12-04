package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public class LangStorage {
	private static LangStorage instance;
	private static String MSG_NOPERM;
	private final HashMap<Message, String> shortmsg = new HashMap<Message, String>();
	private final HashMap<LongMessage, List<String>> longmsg = new HashMap<LongMessage, List<String>>();
	private List<String> MSG_SUCCESSREDEEM;
	private List<String> MSG_FAILREDEEM;
	
	private List<String> SHOW_CASH;
	private List<String> SHOW_CASH_OTHERS;

	private List<String> REDEEM_HELP;
	
	private List<String> NOT_ENOUGH_CASH;
	private List<String> REQUIRED_PERM;
	private List<String> BLACKLISTED_PERM;
	private List<String> BROADCAST_SUCCESSREDEEM;
	
	private List<String> SHOW_AC_CASH;
	private List<String> SHOW_AC_CASH_OTHERS;
	
	private List<String> CASH_TOP_OPENING;
	private List<String> CASH_TOP_CLOSING;
	private String CASH_TOP_FORMAT;
	
	private List<String> ACCASH_TOP_OPENING;
	private List<String> ACCASH_TOP_CLOSING;
	private String ACCASH_TOP_FORMAT;
	private Boolean DO_BROADCASTREDEEM;
	
	private List<String> NOT_NUMBER;
	
	
	private LangStorage() {
	}
	public final static LangStorage getInstance() {
		if (instance == null) {
			instance = new LangStorage();
		}
		return instance;
	}
	public final void LoadMessage() {
		final File file = new File(DonateCraft.getInstance().getDataFolder(), "lang.yml");
		final YamlConfiguration msg = YamlConfiguration.loadConfiguration(file);
		
		MSG_NOPERM = Color(msg.getString("no_permission"));
		MSG_SUCCESSREDEEM = Color(msg.getStringList("redeem_success"));
		MSG_FAILREDEEM = Color(msg.getStringList("redeem_failure"));
		SHOW_CASH = Color(msg.getStringList("show_cash"));
		REDEEM_HELP = Color(msg.getStringList("redeem_help"));
		NOT_ENOUGH_CASH = Color(msg.getStringList("not_enough_cash"));
		BLACKLISTED_PERM = Color(msg.getStringList("blacklisted_perm"));
		SHOW_CASH_OTHERS = Color(msg.getStringList("show_cash_others"));
		SHOW_AC_CASH = Color(msg.getStringList("show_accumulated_cash"));
		SHOW_AC_CASH_OTHERS = Color(msg.getStringList("show_accumulated_cash_others"));
		ConfigurationSection accashtop = msg.getConfigurationSection("ac_cash_top");
		ACCASH_TOP_OPENING = Color(accashtop.getStringList("opening"));
		ACCASH_TOP_CLOSING = Color(accashtop.getStringList("closing"));
		ACCASH_TOP_FORMAT = Color(accashtop.getString("format"));
		ConfigurationSection cashtop = msg.getConfigurationSection("cash_top");
		CASH_TOP_OPENING = Color(cashtop.getStringList("opening"));
		CASH_TOP_CLOSING = Color(cashtop.getStringList("closing"));
		CASH_TOP_FORMAT = Color(cashtop.getString("format"));
		NOT_NUMBER = Color(msg.getStringList("not_a_number"));
		REQUIRED_PERM = Color(msg.getStringList("required_perm_deny"));
		//
		ConfigurationSection bc = msg.getConfigurationSection("broadcast");
		DO_BROADCASTREDEEM = bc.getBoolean("broadcast_redeems");
		BROADCAST_SUCCESSREDEEM = Color(bc.getStringList("message_red"));
		resetMaps();
	}
	private final void resetMaps() {
		shortmsg.clear();
		longmsg.clear();
		longmsg.put(LongMessage.ACTOP_CLOSING, ACCASH_TOP_CLOSING);
		longmsg.put(LongMessage.ACTOP_OPENING, ACCASH_TOP_OPENING);
		longmsg.put(LongMessage.BC_REDEEMSUCCESS, BROADCAST_SUCCESSREDEEM);
		longmsg.put(LongMessage.BLACKLISTED_PERM, BLACKLISTED_PERM);
		longmsg.put(LongMessage.CTOP_CLOSING, CASH_TOP_CLOSING);
		longmsg.put(LongMessage.CTOP_OPENING, CASH_TOP_OPENING);
		longmsg.put(LongMessage.NO_CASH, NOT_ENOUGH_CASH);
		longmsg.put(LongMessage.REDEEM_FAIL, MSG_FAILREDEEM);
		longmsg.put(LongMessage.REDEEM_HELP, REDEEM_HELP);
		longmsg.put(LongMessage.REDEEM_SUCCESS, MSG_SUCCESSREDEEM);
		longmsg.put(LongMessage.SHOW_CASH, SHOW_CASH);
		longmsg.put(LongMessage.SHOW_CASH_OTHERS, SHOW_CASH_OTHERS);
		longmsg.put(LongMessage.SHOW_ACASH, SHOW_AC_CASH);
		longmsg.put(LongMessage.SHOW_ACASH_OTHERS, SHOW_AC_CASH_OTHERS);
		longmsg.put(LongMessage.NOT_NUMBER, NOT_NUMBER);
		longmsg.put(LongMessage.REQUIRED_PERM, REQUIRED_PERM);
		// Load Short Msgs
		shortmsg.put(Message.NOPERM, MSG_NOPERM);
		shortmsg.put(Message.CTOP_FORMAT, CASH_TOP_FORMAT);
		shortmsg.put(Message.ACTOP_FORMAT, ACCASH_TOP_FORMAT);
	}
	private final  String Color(final String str) {
		return Utility.TransColor(str);
	}
	private final List<String> Color(final List<String> str) {
		return Utility.TransColor(str);
	}
	public final String getMessage(final Message msg) {
		return shortmsg.get(msg);
	}
	public final List<String> getMessage(final LongMessage msg) {
		return longmsg.get(msg);
	}
	public final Boolean getDoes(final Does does) {
		switch (does.toString()) {
		case "BROADCASTREDEEM":
			return DO_BROADCASTREDEEM;
		default:
			return false;
		}
	}
	public enum Message {
		NOPERM,
		CTOP_FORMAT,
		ACTOP_FORMAT;
	}
	public enum LongMessage {
		BC_REDEEMSUCCESS,
		SHOW_CASH,
		REDEEM_HELP,
		REDEEM_SUCCESS,
		REDEEM_FAIL,
		NO_CASH,
		BLACKLISTED_PERM,
		SHOW_CASH_OTHERS,
		ACTOP_OPENING,
		ACTOP_CLOSING,
		CTOP_OPENING,
		CTOP_CLOSING,
		SHOW_ACASH,
		SHOW_ACASH_OTHERS,
		NOT_NUMBER,
		REQUIRED_PERM;
	}
	public enum Does { 
		BROADCASTREDEEM,
		BROADCASTPURCHASE;
	}

}
