package com.gmail.JyckoSianjaya.DonateCraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Data.Objects.PackedSound;
import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Action;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.XSound;

public final class LangStorage {
	private static LangStorage instance;
	private static String MSG_NOPERM;
	private final HashMap<Message, String> shortmsg = new HashMap<Message, String>();
	private final HashMap<LongMessage, List<String>> longmsg = new HashMap<LongMessage, List<String>>();
	private final HashMap<Titles, String[]> titles = new HashMap<Titles, String[]>();
	private final HashMap<ActionBars, String> actionbars = new HashMap<ActionBars, String>();
	private final HashMap<Sounds, List<PackedSound>> sounds = new HashMap<Sounds, List<PackedSound>>();
	private final HashMap<Does, Boolean> does = new HashMap<Does, Boolean>();
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
	
	private Action itembought_action;
	
	private Boolean use_itembought_messages = false;
	private List<String> itembought_messages;
	
	private Boolean use_itembought_title = false;
	private String[] itembought_titles = new String[] {"", ""};
	
	private Boolean use_itembought_sounds = false;
	private ArrayList<PackedSound> itembought_sounds = new ArrayList<PackedSound>();
	
	private String itembought_actionbar = "";
	private Boolean use_itembought_actionbar = false;
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
		
		ConfigurationSection itembought = msg.getConfigurationSection("item_bought");
		
		this.use_itembought_sounds = itembought.getBoolean("sounds.use");
		List<String> ggsounds = itembought.getStringList("sounds.sounds");
		for (String tr : ggsounds) {
			String[] things = tr.split("-");
			Sound sound;
			try {
				sound = XSound.requestXSound(things[0]);
			} catch (IllegalArgumentException e) {
				Utility.sendConsole("[DC] The sound: " + things[0] + " does not exist!");
				sound = XSound.BAT_DEATH.bukkitSound();
			}
			float volumes = Float.valueOf(things[1]);
			float pitch = Float.valueOf(things[2]);
			this.itembought_sounds.add(new PackedSound(sound, volumes, pitch));
		}
		this.use_itembought_title = itembought.getBoolean("titles.use");
		this.itembought_titles[0] = Utility.TransColor(itembought.getString("titles.title"));
		this.itembought_titles[1] = Utility.TransColor(itembought.getString("titles.subtitle"));
		
		this.use_itembought_messages = itembought.getBoolean("messages.use");
		this.itembought_messages = Color(itembought.getStringList("messages.message"));
		
		this.use_itembought_actionbar = itembought.getBoolean("actionbar.use");
		this.itembought_actionbar = Color(itembought.getString("actionbar.message"));
		this.itembought_action = ActionStorage.getInstance().getAction(itembought.getString("action"));
 		resetMaps();
	}
	public Action getItemBought_Action() { return this.itembought_action; }
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
		does.put(Does.BROADCASTREDEEM, this.DO_BROADCASTREDEEM);
		does.put(Does.ITEM_BOUGHT_USE_ACTIONBAR, this.use_itembought_actionbar);
		does.put(Does.ITEM_BOUGHT_USE_MESSAGE, this.use_itembought_messages);
		does.put(Does.ITEM_BOUGHT_USE_SOUNDS, this.use_itembought_sounds);
		does.put(Does.ITEM_BOUGHT_USE_TITLES, this.use_itembought_title);
		this.sounds.put(Sounds.ITEM_BOUGHT_SOUNDS, this.itembought_sounds);
		longmsg.put(LongMessage.ITEM_BOUGHT_MESSAGE, this.itembought_messages);
		actionbars.put(ActionBars.ITEM_BOUGHT_ACTIONBAR, this.itembought_actionbar);
		titles.put(Titles.ITEM_BOUGHT_TITLES, this.itembought_titles);
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
	public final String[] getTitle(final Titles titles) {
		return this.titles.get(titles);
	}
	public final String getActionBar(final ActionBars actionbar) {
		return this.actionbars.get(actionbar);
	}
	public final List<PackedSound> getSound(Sounds sound) { return this.sounds.get(sound); }
	public final List<String> getMessage(final LongMessage msg) {
		return longmsg.get(msg);
	}
	public final Boolean getDoes(final Does does) {
		return this.does.get(does);
	}
	public enum Sounds {
		ITEM_BOUGHT_SOUNDS;
	}
	public enum Titles {
		ITEM_BOUGHT_TITLES;
	}
	public enum ActionBars {
		ITEM_BOUGHT_ACTIONBAR;
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
		ITEM_BOUGHT_MESSAGE,
		REQUIRED_PERM;
	}
	public enum Does { 
		ITEM_BOUGHT_USE_TITLES,
		ITEM_BOUGHT_USE_ACTIONBAR,
		ITEM_BOUGHT_USE_SOUNDS,
		ITEM_BOUGHT_USE_MESSAGE,
		BROADCASTREDEEM,
		BROADCASTPURCHASE;
	}

}
