package org.golde.bukkit.corpsereborn.dump;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.bukkit.corpsereborn.Main;
import org.golde.bukkit.corpsereborn.Util;

public class ReportError {

	public ReportError(Exception e){
		this(e, Bukkit.getConsoleSender());
	}
	
	public ReportError(final Exception e, final CommandSender cs){
		e.printStackTrace();
	}
}


