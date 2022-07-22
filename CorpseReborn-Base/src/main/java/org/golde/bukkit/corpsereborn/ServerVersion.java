package org.golde.bukkit.corpsereborn;

import org.bukkit.Bukkit;

public enum ServerVersion {
	v1_19_R1,
	UNKNOWN;
	
	public static String getParenthesesContent(String str){
        return str.substring(str.indexOf('(')+1,str.indexOf(')'));
    }
	
	public static String rawMcVersion = getParenthesesContent(Bukkit.getVersion()).replaceAll("MC: ", "");
	
	public static ServerVersion fromClass(String clazz){
		if ("v1_19_R1".equals(clazz)) {
			return v1_19_R1;
		}
		return UNKNOWN;
	}
}
