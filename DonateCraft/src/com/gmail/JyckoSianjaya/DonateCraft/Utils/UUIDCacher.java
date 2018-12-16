package com.gmail.JyckoSianjaya.DonateCraft.Utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;

public class UUIDCacher {
	private static UUIDCacher instance;
	private YamlConfiguration yml;
	private ConfigurationSection uuids;
	private ConfigurationSection nicks;
	private UUIDCacher() {
		File cachef = new File(DonateCraft.getInstance().getDataFolder(), "cacheuuid.yml");
		if (!cachef.exists()) {
		DonateCraft.getInstance().saveResource("cacheuuid.yml", false);
		}
		cachef = new File(DonateCraft.getInstance().getDataFolder(), "cacheuuid.yml");
		yml = YamlConfiguration.loadConfiguration(cachef);
		uuids = yml.getConfigurationSection("UUIDS");
		nicks = yml.getConfigurationSection("NICKNAMES");
	}
	public void saveData() {
		try {
			yml.save(new File(DonateCraft.getInstance().getDataFolder(), "cacheuuid.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public UUID getUUID(String name) {
		if (uuids.getString(name.toLowerCase()) == null) return null;
		return UUID.fromString(uuids.getString(name.toLowerCase()));
	}
	public String getNick(UUID uuid) {
		if (nicks.getString(uuid.toString()) == null) return null;

		return nicks.getString(uuid.toString());
	}
	public static UUIDCacher getInstance() {
		if (instance == null) instance = new UUIDCacher();
		return instance;
	}
	public void setUUID(String name, UUID uuid) {
		uuids.set(name.toLowerCase(), uuid.toString());
		nicks.set(uuid.toString(), name);
	}
}
