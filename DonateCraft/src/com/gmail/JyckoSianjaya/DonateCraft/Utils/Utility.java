package com.gmail.JyckoSianjaya.DonateCraft.Utils;

import java.io.File;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public final class Utility {
	int time;
	int ctime;
	private Utility() {}
	public static Class<?> getClass(String classname) {
		String servversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + servversion + "." + classname);
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
		} 
		return null;
	  }
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } else if (file.getName().endsWith(".class")) {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
	public static Class[] getClasses(String packageName)
	        throws ClassNotFoundException, IOException {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    ArrayList<Class> classes = new ArrayList<Class>();
	    for (File directory : dirs) {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes.toArray(new Class[classes.size()]);
	}
	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getClass("Packet") }).invoke(playerConnection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
	   }
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {


	    try
	    {
	      if (title != null) {
	        title = TransColor(title);
	        Object e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
	        Object chatTitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
	        Constructor titleConstructor = getClass("PacketPlayOutTitle").getConstructor(new Class[] { getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	        Object titlePacket = titleConstructor.newInstance(new Object[] { e, chatTitle, fadeIn, stay, fadeOut });
	        sendPacket(player, titlePacket);

	        e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
	        chatTitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
	        titleConstructor = getClass("PacketPlayOutTitle").getConstructor(new Class[] { getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent") });
	        titlePacket = titleConstructor.newInstance(new Object[] { e, chatTitle });
	        sendPacket(player, titlePacket);
	      }

	      if (subtitle != null) {
	        subtitle = TransColor(subtitle);
	        Object e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
	        Object chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
	        Constructor subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(new Class[] { getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	        Object subtitlePacket = subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
	        sendPacket(player, subtitlePacket);

	        e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
	        chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
	        subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(new Class[] { getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
	        subtitlePacket = subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
	        sendPacket(player, subtitlePacket);
	      }
	    } catch (Exception var11) {
	      var11.printStackTrace();
	    }
	}
	public static void executeConsole(String cmd) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}
	public static void sendMsg(Player b, String msg) {
		if (msg.contains("%center%")) { sendCenteredMessage(b, msg.replaceAll("%center%", "")); return; }
		b.sendMessage(TransColor(msg));
	}
	public static Inventory copy(Inventory inventory, InventoryHolder h) {
	    Inventory inv = Bukkit.createInventory(h, inventory.getSize(), inventory.getTitle());
	    inv.setContents(inventory.getContents());
	    return inv;
	}
	public static void sendMsg(CommandSender b, String msg) {
		if (msg.contains("%center%")) {
			if (b instanceof Player) {
			sendCenteredMessage((Player) b, msg.replaceAll("%center%", "")); 
			return;
			}
			b.sendMessage(TransColor(msg.replaceAll("%center%", "")));
		return; }
		b.sendMessage(TransColor(msg));
	}
	public static void broadcast(String msg) {
		if (msg.contains("%center%")) {
			broadCastCenteredMessage(msg.replaceAll("%center%", ""));
			return;
		}
		Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	public static void sendActionBar(Player b, String ActionBar) {
		ActionBarAPI.sendActionBar(b, ChatColor.translateAlternateColorCodes('&', ActionBar));
	}
	public static void sendConsole(String msg) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));;
	}
	public static String TransColor(String c) {
		return ChatColor.translateAlternateColorCodes('&', c);
	}
	private final static int CENTER_PX = 154;

	public static void sendCenteredMessage(Player player, String message){
	        if(message == null || message.equals("")) { sendMsg(player, message); return; }
	                int messagePxSize = 0;
	                boolean previousCode = false;
	                boolean isBold = false;
	               
	                for(char c : message.toCharArray()){
	                        if(c == '§'){
	                                previousCode = true;
	                                continue;
	                        }else if(previousCode == true){
	                                previousCode = false;
	                                if(c == 'l' || c == 'L'){
	                                        isBold = true;
	                                        continue;
	                                }else isBold = false;
	                        }else{
	                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
	                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
	                                messagePxSize++;
	                        }
	                }
	               
	                int halvedMessageSize = messagePxSize / 2;
	                int toCompensate = CENTER_PX - halvedMessageSize;
	                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
	                int compensated = 0;
	                StringBuilder sb = new StringBuilder();
	                while(compensated < toCompensate){
	                        sb.append(" ");
	                        compensated += spaceLength;
	                }
	                player.sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString() + message));
	        }
	public static void broadCastCenteredMessage(String message){
        if(message == null || message.equals("")) { Bukkit.getServer().broadcastMessage(""); return; }               
                int messagePxSize = 0;
                boolean previousCode = false;
                boolean isBold = false;
               
                for(char c : message.toCharArray()){
                        if(c == '§'){
                                previousCode = true;
                                continue;
                        }else if(previousCode == true){
                                previousCode = false;
                                if(c == 'l' || c == 'L'){
                                        isBold = true;
                                        continue;
                                }else isBold = false;
                        }else{
                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                                messagePxSize++;
                        }
                }
               
                int halvedMessageSize = messagePxSize / 2;
                int toCompensate = CENTER_PX - halvedMessageSize;
                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
                int compensated = 0;
                StringBuilder sb = new StringBuilder();
                while(compensated < toCompensate){
                        sb.append(" ");
                        compensated += spaceLength;
                }
                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', sb.toString() + message));
        }
	public enum DefaultFontInfo{
		 
        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);
       
        private char character;
        private int length;
       
        DefaultFontInfo(char character, int length) {
                this.character = character;
                this.length = length;
        }
       
        public char getCharacter(){
                return this.character;
        }
       
        public int getLength(){
                return this.length;
        }
       
        public int getBoldLength(){
                if(this == DefaultFontInfo.SPACE) return this.getLength();
                return this.length + 1;
        }
       
        public static DefaultFontInfo getDefaultFontInfo(char c){
                for(DefaultFontInfo dFI : DefaultFontInfo.values()){
                        if(dFI.getCharacter() == c) return dFI;
                }
                return DefaultFontInfo.DEFAULT;
        }
	}
	public static String[] TransColor(String[] c) {
		String strf = "";
		int length = c.length;
		int cr = 0;
		for (String str : c) {
			cr++;
			if (cr != length) {
				strf = strf + str + ";";
			}
			else {
				strf = strf + str;
			}
		}
		strf = TransColor(strf);
		return strf.split(";");
	}
	public static List<String> TransColor(List<String> strlist) {
		for (int x = 0; x < strlist.size(); x++) {
			strlist.set(x, TransColor(strlist.get(x)));
		}
		return strlist;
	}
	public static void PlaySoundAt(World w, Location p, Sound s, Float vol, Float pit) {
		w.playSound(p, s, vol, pit);;
	}
	public static void PlaySound(Player p, Sound s, Float vol, Float pit) {
		p.playSound(p.getLocation(), s, vol, pit);
	}
	public static ArrayList<Player> near(Entity loc, int radius) {
		ArrayList<Player> nearby = new ArrayList<>();
		for (Entity e : loc.getNearbyEntities(radius, radius, radius)) {
			if (e instanceof Player) {
				nearby.add((Player) e);
			}
		}
		return nearby;
	}
	public static void PlayParticle(World world, Location loc, Effect particle, int count) {
		world.playEffect(loc, particle, count);
	}
	public static void spawnParticle(World world, Particle particle, Location loc, Double Xoff, Double Yoff, Double Zoff, int count) {
		world.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), count, Xoff, Yoff, Zoff);
	}
	public static String normalizeTime(int seconds) {
		int sec = seconds;
		int min = 0;
		int hour = 0;
		int day = 0;
		while (sec >= 60) {
			min+=1;
			sec-=60;
		}
		while (min >= 60) {
			hour+=1;
			min-=60;
		}
		while (hour >= 24) {
			day+=1;
			hour-=24;
		}
		if (sec == 0 && min == 0 && hour == 0 && day == 0) {
			return "&a&lZERO!";
		}
		if (min == 0 && hour == 0 && day == 0) {
			return sec + " Seconds";
		}
		if (hour == 0 && day == 0 && min > 0) {
			return min + " Minutes " + sec + " Seconds"; 
		}
		if (day == 0 && hour > 0) {
			return hour + " Hours " + min + " Minutes " + sec + " Seconds";
		}
		if (day > 0) {
			return day + " Days " + hour + " Hours " + min + " Minutes " + sec + " Seconds";
		}
		return "&a&lZERO!";
	}
	public static String normalizeTime2(int seconds) {
		int sec = seconds;
		int min = 0;
		int hour = 0;
		int day = 0;
		while (sec >= 60) {
			min+=1;
			sec-=60;
		}
		while (min >= 60) {
			hour+=1;
			min-=60;
		}
		while (hour >= 24) {
			day+=1;
			hour-=24;
		}
		if (sec == 0 && min == 0 && hour == 0 && day == 0) {
			return "&a&lZERO!";
		}
		if (min == 0 && hour == 0 && day == 0) {
			return sec + " sec";
		}
		if (hour == 0 && day == 0 && min > 0) {
			return min + " min " + sec + " sec"; 
		}
		if (day == 0 && hour > 0) {
			return hour + " h " + min + " min " + sec + " sec";
		}
		if (day > 0) {
			return day + " day " + hour + " h " + min + " min " + sec + " sec";
		}
		return "&a&lZERO!";
	}
	public static boolean isEmpty(Inventory inv) {
		int size = inv.getSize();
		for (int i = 0; i < size; i++) {
			if (inv.getItem(i) == null) return true;
		}
		return false;
	}
	public static boolean isEmpty(PlayerInventory inv) {
		return isEmpty(inv);
	}
}
