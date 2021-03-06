package com.gmail.JyckoSianjaya.DonateCraft.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.JyckoSianjaya.DonateCraft.Commands.DonateCmdListener;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ACCashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ActionStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.CashBank;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DCLogger;
import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.InventoryStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.ItemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.LangStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.NumberStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Data.PlayerData;
import com.gmail.JyckoSianjaya.DonateCraft.Data.RedeemStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Database.SimpleSQL;
import com.gmail.JyckoSianjaya.DonateCraft.Events.DCEventHandler;
import com.gmail.JyckoSianjaya.DonateCraft.Events.DCEvents;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.ConfirmationManager;
import com.gmail.JyckoSianjaya.DonateCraft.Manager.DCRunnable;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.ACWallet;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.Cash;
import com.gmail.JyckoSianjaya.DonateCraft.Objects.RedeemCode;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.ActionBarAPI;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.UUIDCacher;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public final class DonateCraft extends JavaPlugin {
	private static DonateCraft instance;
	private ActionStorage as;
	private CashBank cb;
	private DataStorage ds;
	private InventoryStorage is;
	private ItemStorage iss;
	private LangStorage ls;
	private PlayerData pd;
	private RedeemStorage rs;
	private ACCashBank acb;
	private DCLogger logger;
	private ConfirmationManager cmm;
	private DCRunnable dcr;
	private ActionBarAPI aba;
	private NumberStorage str = NumberStorage.getInstance();
	private UUIDCacher cach;
	@Override
	public final void onEnable() {
		Utility.sendConsole("&6&l<<--&e&l DonateCraft &6&l&m-->>&r");
		Utility.sendConsole(" ");
		Utility.sendConsole("&eProfessionally made with &dlove &eby Jycko Sianjaya a.k.a Gober xD");
		Utility.sendConsole(" &eEnjoy the plugin :)");
		Utility.sendConsole("  ");
		Utility.sendConsole("&6&l&m<<------------------->>");

		registerInstance();
		Utility.sendConsole("DC -->>");
		LoadConfig();
		Utility.sendConsole("->> Loaded Config");
		CopyFiles();
		Utility.sendConsole("->> Copied Files");
		CreateFolders();
		Utility.sendConsole("->> Created Folders");
		CreateStorageObjects();
		LoadManagers();
		Utility.sendConsole("->> Loaded Files & Logger");
		RegisterCommands();
		Utility.sendConsole("->> Commands loaded");
		RegisterEvents();
		Utility.sendConsole("->> Events loaded");
		checkNBTAPI();
		registerMetrics();
		checkUpdates();
	}
	@Override
	public final void onDisable() {
		saveAllCash();
		saveAllcode();
		cach.saveData();
		SimpleSQL.getInstance().onDisable();
	}
	private final void registerMetrics() {
		Metrics metrics = new Metrics(this);
	}
	private final void checkNBTAPI() {
		}
	private final void CreateStorageObjects() {
		logger = DCLogger.getInstance();
		final DataStorage ds = DataStorage.getInstance();
		ds.LoadConfig();
		this.ds = ds;
		final LangStorage ls = LangStorage.getInstance();
		ls.LoadMessage();
		this.ls = ls;
		final PlayerData pd = PlayerData.getInstance();
		this.pd = pd;
		final ActionStorage as = ActionStorage.getInstance();
		as.LoadActions();
		this.as = as;
		final ItemStorage is = ItemStorage.getInstance();
		is.LoadFiles();
		this.iss = is;
		final CashBank bank = CashBank.getInstance();
		bank.LoadFiles();
		this.cb = bank;
		final InventoryStorage IS = InventoryStorage.getInstance();
		this.is = IS;
		IS.LoadFiles();
		final RedeemStorage rs = RedeemStorage.getInstance();
		rs.LoadRedeemFile();
		this.rs = rs;
		final ACCashBank acb = ACCashBank.getInstance();
		this.acb = acb;
		this.dcr = DCRunnable.getInstance();
		aba = ActionBarAPI.getInstance();
		this.cach = UUIDCacher.getInstance();
	}
	private final void LoadManagers() {
		cmm = ConfirmationManager.getInstance();
	}
	private final void saveAllcode() {
		final RedeemStorage storage = RedeemStorage.getInstance();
		File file = new File(this.getDataFolder(), "Redeems.yml");
		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		file = new File(this.getDataFolder(), "Redeems.yml");
		final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		yaml.createSection("redeems");
		final ConfigurationSection redeems = yaml.getConfigurationSection("redeems");
		for (final RedeemCode code : storage.getRedeemCodes()) {
			final String cc = code.getCode();
			final int amount = code.getCash().getCashAmount();
			redeems.set(cc, amount);
		}
		try {
			yaml.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utility.sendConsole("[DC] Can't save Redeems.yml");
		}
	}
	private final void saveAllCash() {
		if (DataStorage.getInstance().useSQL()) return;
		final CashBank bank = CashBank.getInstance();
		final PlayerData pd = PlayerData.getInstance();
		for (final UUID p : this.cb.getKeys()) {
			if (bank.getCash(p) == null && acb.getACWallet(p) == null) {
				continue;
			}
			Cash cash = bank.getCash(p);
			if (cash == null) {
				cash = new Cash(0);
			}
			ACWallet wallet = acb.getACWallet(p);
			if (wallet == null) {
				wallet = new ACWallet(0);
			}
			pd.setData(p);
		}
	}
	private final void RegisterCommands() {
		final CommandExecutor executor = new DonateCmdListener();
		this.getCommand("redeems").setExecutor(executor);
		this.getCommand("store").setExecutor(executor);
		this.getCommand("donatecraft").setExecutor(executor);
		this.getCommand("cash").setExecutor(executor);
		this.getCommand("acash").setExecutor(executor);
	}
	private final void registerInstance() {
		instance = this;
	}
	public final static DonateCraft getInstance() {
		return instance;
	}
	private final void RegisterEvents() {
		DCEventHandler.getInstance();
		Bukkit.getServer().getPluginManager().registerEvents(new DCEvents(), this);
	}
	private final void CreateFolders() {
		final File folder = new File(this.getDataFolder(), "playerdata");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		final File logs = new File(this.getDataFolder(), "logs");
		if (!logs.exists()) {
			logs.mkdir();
		}
	}
	@Override
	public final void onLoad() {
		showCoolLogo();
	}
	private final void showCoolLogo() {
		final PluginDescriptionFile dcpf = this.getDescription();
		final String dc = dcpf.getVersion();
		Utility.sendConsole("&6   __    __        __  ___  __ &e   __  __   __   __  ___             ");
		Utility.sendConsole("&6  |  \\  |  | |\\ | |__|  |  |__ &e  |   |__| |__| |__   |                   ");
		Utility.sendConsole("&6  |__/  |__| | \\| |  |  |  |__ &e  |__ |  \\ |  | |     |               ");
		Utility.sendConsole("&6                                              ");
		Utility.sendConsole("&7              Running DonateCraft V.&b" + dc);


	}
	private final void checkUpdates() {
		URL url = null;
		Scanner scanner = null;
		try {
			// Url for Plugin Version is: https://pastebin.com/hfq3rsZx
		url = new URL("https://pastebin.com/raw/hfq3rsZx");
		scanner = new Scanner(url.openStream());
		final String str = scanner.nextLine();
		final PluginDescriptionFile dcpf = this.getDescription();
		final String dc = dcpf.getVersion();
		scanner.close();
		if (!str.equals(dc)) {
			
			Utility.sendConsole("&b[UPDATE] &7New update for &eDonateCraft &7is available (Version " + str + "), check them out in '&fLink Coming soon&7'");
			return;
		}
		else {
			Utility.sendConsole("&6[DC] &eYou are using the latest version: &6V" + dc);
		}
		} catch (Exception e) {
			Utility.sendConsole("&6[DC] &cCouldn't get the latest version for DonateCraft.");
			return;
		}
	}
	private void CopyFiles() {
		final File langfile = new File(this.getDataFolder(), "lang.yml");
		if (!langfile.exists()) {
			this.saveResource("lang.yml", true);
		}
		final File itemfile = new File(this.getDataFolder(), "Items.yml");
		if (!itemfile.exists()) {
			this.saveResource("Items.yml", true);
		}
		final File actionfile = new File(this.getDataFolder(), "Actions.yml");
		if (!actionfile.exists()) {
			this.saveResource("Actions.yml", true);
		}
		final File GUIFile = new File(this.getDataFolder(), "GUI.yml");
		if (!GUIFile.exists()) {
			this.saveResource("GUI.yml", true);
		}
		final File RedeemFile = new File(this.getDataFolder(), "Redeems.yml");
		if (!RedeemFile.exists()) {
			this.saveResource("Redeems.yml", true);
		}
		final File instructions = new File(this.getDataFolder(), "Instructions.txt");
		this.saveResource("Instructions.txt", true);
		this.saveResource("Changelogs.txt", true);
		this.saveResource("DummyData.yml", true);
	}
	private void LoadConfig() {
		final FileConfiguration config = this.getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}
}
