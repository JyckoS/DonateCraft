package com.gmail.JyckoSianjaya.DonateCraft.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.JyckoSianjaya.DonateCraft.Data.DataStorage;
import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;
import com.gmail.JyckoSianjaya.DonateCraft.Utils.Utility;

public class SimpleSQL {
	private static SimpleSQL instance;
	private Connection connection;
	private Statement statement;
	private String hostname = ""; // The host name
	private int port = 0; // The host port
	private String databasename = ""; // The name of the database
	private String username = ""; // MySQL username
	private String password = ""; // MySQL password
	public Connection getConnection() {
		return this.connection;
	}
	public void onDisable() {
		try {
		this.connection.close();
		statement.close();
		} catch (SQLException e) {
		}
	}
	public boolean createTable() {
		if (connection == null) return false;
		try {
			statement.executeQuery("CREATE TABLE DCPlayerData IF NOT EXISTS ("
					+ "uuid UUID NOT NULL,"
					+ "data TEXT NOT NULL,"
					+ "PRIMARY KEY(uuid))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public ResultSet getResult(String query) {
		ResultSet res = null;
		try {
			res = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public boolean hasRecord(String uuid) {
		String sql = "SELECT data FROM DCPlayerData WHERE uuid='" + uuid + "';";
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = this.connection.prepareStatement(sql);
			result = statement.executeQuery();
			return result.next();
		} catch (SQLException e) {
		}
		return false;

		
		
	}
	public Statement getStatement() {
		Statement s = null;
		try {
			s = this.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	public static SimpleSQL getInstance() {
		return instance;
	}
	private void changeSQLState() {
		DataStorage.getInstance().setSQL(false);
	}
	private SimpleSQL(String hostname, int port , String databasename, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.databasename = databasename;
		this.username = username;
		this.password = password;
		new BukkitRunnable() {
			Boolean loaded = false;
			@Override
			public void run() {
				if (!loaded) {
					try {
						openConnection();
						Statement state = connection.createStatement();
						statement = state;
					} catch (SQLException e) {
						Utility.sendConsole("[DC] &7Can't open a MySQL connection, is it correct?");
						changeSQLState();
						return;
					} catch (ClassNotFoundException e) {
						Utility.sendConsole("[DC] &7Can't find SQL class. Please contact the author via spigot conversation.");
						changeSQLState();

						return;
					}
					loaded = true;
				}
				Boolean closed = false;
				try {
					closed = connection.isClosed();
				} catch (SQLException e) {
					Utility.sendConsole("[DC] &7Can't check if Connection closed. Is it okay?");
				}
				if (closed) {
					try {
						openConnection();
						Statement state = connection.createStatement();
						statement = state;
					} catch (SQLException e) {
						Utility.sendConsole("[DC] &7Can't open a MySQL connection, is it correct?");
						changeSQLState();

					} catch (ClassNotFoundException e) {
						changeSQLState();

						Utility.sendConsole("[DC] &7Can't find SQL class. Please contact the author via spigot conversation.");
					}
				}
			}
		}.runTaskAsynchronously(DonateCraft.getInstance());
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					openConnection();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.cancel();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.cancel();
				}
				
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 6000L, 6000L);
		this.createTable();
	}
	public static SimpleSQL setup(String host, int port, String database, String user, String pass) {
		if (instance != null) return null;
		instance = new SimpleSQL(host, port, database, user, pass);
		return instance;
	}
	public void openConnection() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        } 
	        Properties properties = new Properties();
	        properties.setProperty("user", this.username);
	        properties.setProperty("password", this.password);
	        properties.setProperty("useSSL", "false");
	        properties.setProperty("autoReconnect", "true");

	        try {
	        	connection = DriverManager.getConnection(this.hostname + ":" + this.port, properties);
	        	return;
	        } catch (SQLException e) {
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname+ ":" + this.port + "/" + this.databasename, this.username, this.password);
	    }
	}
}
