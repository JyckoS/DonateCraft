package com.gmail.JyckoSianjaya.DonateCraft.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
					} catch (ClassNotFoundException e) {
						Utility.sendConsole("[DC] &7Can't find SQL class. Please contact the author via spigot conversation.");
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
					} catch (ClassNotFoundException e) {
						Utility.sendConsole("[DC] &7Can't find SQL class. Please contact the author via spigot conversation.");
					}
				}
			}
		}.runTaskTimerAsynchronously(DonateCraft.getInstance(), 100L, 100L);
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
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname+ ":" + this.port + "/" + this.databasename, this.username, this.password);
	    }
	}
}
