package com.gmail.JyckoSianjaya.DonateCraft.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.gmail.JyckoSianjaya.DonateCraft.Main.DonateCraft;

public class ReflectionUtil {
	private static String MINECRAFT_SERVER = "";
	static {
		MINECRAFT_SERVER = "net.minecraft.server." + DonateCraft.getInstance().getServer().getClass().getPackage().getName().substring(23);
	}
	   public static Object getIChatBaseComponentFromJSONString(String json) {
	      try {
			return getMethod(getChatSerializer(), "a", String.class).invoke(null, json);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return json;
	    }
	 
	    private static void sendPacket(Player player, Object packet) {
	        Object nmsPlayer = null;
	        try {
	            nmsPlayer = getMethod(player.getClass(), "getHandle").invoke(player);
	        } catch (IllegalAccessException e1) {
	            e1.printStackTrace();
	        } catch (InvocationTargetException e2) {
	        	e2.printStackTrace();
	        }
	    
	        Object playerConnection = getFieldValue(nmsPlayer.getClass(), nmsPlayer, "playerConnection");

	        try {
	            getMethod(playerConnection.getClass(), "sendPacket", getClass("Packet")).invoke(playerConnection, packet);
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e1) {
	        	e1.printStackTrace();
	        }
	    }
	 
	    private static Class<?> getClass(String className) {
	        try {
				return Class.forName(MINECRAFT_SERVER + "." + className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return null;
	    }
	 
	    private static Class<?> getChatSerializer() {
	        Class<?> serializer = getClass("IChatBaseComponent$ChatSerializer");

	        try {
	            return serializer.newInstance().getClass();
	        } catch (InstantiationException | IllegalAccessException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
	 
	    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameters) {
	        try {
	            return clazz.getMethod(methodName, parameters);
	        } catch (NoSuchMethodException | SecurityException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
	 
	    private static Object getFieldValue(Class<?> clazz, Object object, String fieldName) {
	        Field field;

	        try {
	            field = clazz.getField(fieldName);
	        
	            return field.get(object);
	        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
}
