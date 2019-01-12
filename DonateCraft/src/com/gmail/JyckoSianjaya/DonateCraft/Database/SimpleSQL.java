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
		} catch (SQLException e) {
		}
	}
	public boolean createTable() {
		if (connection == null) return false;
		try {
			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS DCPlayerData ("
					+ "uuid VARCHAR(100), "
					+ "data TEXT, "
					+ "PRIMARY KEY (uuid))");
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean execute(String query) {
		try {
			Statement statement = connection.createStatement(); 
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			
			return false;
		}
		return true;
	}
	public int getUpdate(String update) {
		int i = 0;
		try {
			Statement statement = connection.createStatement();
			i = statement.executeUpdate(update);;
			statement.close();
		} catch (SQLException e) {}
		return i;
	}
	public ResultSet getResult(String query) {
		ResultSet res = null;
		try {
			Statement statement = connection.createStatement();
			res = statement.executeQuery(query);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public boolean hasRecord(String uuid) {
		String sql = "SELECT EXISTS(SELECT * from DCPlayerData WHERE uuid='" + uuid + "')";
		PreparedStatement statement = null;
		ResultSet result = null;
		Boolean returns = false;
		try {
			statement = this.connection.prepareStatement(sql);
			result = statement.executeQuery();
			statement.close();
			while (result.next()) {
				int resuls = result.getInt(1);
				returns = (resuls > 0 ? true : false);
			}

		} catch (SQLException e) {
			return false;
		}
		return returns;

		
		
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
						state.close();
						createTable();
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
					Utility.sendConsole("[DC] &7Successfully &aCONNECTED &7to MySQL.");
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
						state.close();
					} catch (SQLException e) {
						Utility.sendConsole("[DC] &7Can't open a MySQL connection, is it correct?");
						changeSQLState();
						return;
					} catch (ClassNotFoundException e) {
						changeSQLState();
						Utility.sendConsole("[DC] &7Can't find SQL class. Please contact the author via spigot conversation.");
						return;
					}
					Utility.sendConsole("[DC] &7Successfully &aRECONNECTED &7to MySQL.");
				}
				
			}
		}.runTaskAsynchronously(DonateCraft.getInstance());
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
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname+ ":" + this.port + "/" + this.databasename + "?autoReconnect=true&useSSL=false", this.username, this.password);
	    }
	}
}
