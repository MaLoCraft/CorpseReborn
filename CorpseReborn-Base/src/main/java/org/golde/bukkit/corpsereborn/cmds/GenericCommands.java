package org.golde.bukkit.corpsereborn.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.golde.bukkit.corpsereborn.ConfigData;
import org.golde.bukkit.corpsereborn.Main;
import org.golde.bukkit.corpsereborn.dump.DumpException;
import org.golde.bukkit.corpsereborn.dump.ReportError;

public class GenericCommands implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args){
		try{
			String v = Main.getPlugin().getDescription().getVersion();
			if(args.length == 0){
				sender.sendMessage("This server is running CorpseReborn version " + v);
				if(sender.hasPermission("corpses.reload") || sender.hasPermission("corpses.dump")){
					sender.sendMessage("/corpsereborn reload:dump");
				}
				return true;
			}

			if(args[0].equalsIgnoreCase("reload")){
				if (!sender.hasPermission("corpses.reload")) {
					sender.sendMessage(ChatColor.RED
							+ "You do not have enough permissions!");
					return true;
				}else{
					
					Main.getPlugin().reloadConfig();
					
					ConfigData.checkConfigForMissingOptions();
					ConfigData.load();
					
					sender.sendMessage(ChatColor.GREEN + "Config reloaded.");
				}
			}

			if(args[0].equalsIgnoreCase("dump")){
				if (!sender.hasPermission("corpses.dump")) {
					sender.sendMessage(ChatColor.RED
							+ "You do not have enough permissions!");
					return true;
				}else{
					sender.sendMessage(ChatColor.GREEN + "Creating dump...");
					new ReportError(new DumpException(), sender);
					return true;
				}
			}


		}catch(Exception ex){
			new ReportError(ex, sender);
		}
		return true;
	}

}
