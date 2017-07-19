package io.github.russianmushroom.command;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.russianmushroom.yaml.BaseYAML;

/**
 * Handle command to delete user data
 * @author RussianMushroom
 *
 */
public class DeleteData {
	
	private static UUID playerUUID;
	private static String playerDataPath;

	/**
	 * Execute /mim delete [player name]
	 * Check if the sender has the permission to execute the command.
	 * Check if the command has been properly executed.
	 * @param sender
	 * @param playerName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean remove(CommandSender sender, String playerName) {
		// Check if the playerName is valid
		Player player = Bukkit.getServer().getPlayer(playerName);
		
		// Check if the sender has the necessary permissions.
		if(!sender.hasPermission("mim.delete")) {
			sender.sendMessage("You do not have the necessary permissions to use this command!");
			return false;
		}
		
		if(player != null) {
			playerUUID = player.getUniqueId();
			playerDataPath = BaseYAML.getPlayerFolder().toString() + File.separator + playerUUID + ".yml";
			
			if(!new File(playerDataPath).exists())
				sender.sendMessage(String.format("%s does not have a data file", player.getName()));
			else {
				if(!new File(playerDataPath).delete()) 
					sender.sendMessage(String.format("%s's file could not be deleted!", player.getName()));
				else
					return true;
			}
		}
		else
			sender.sendMessage(String.format("%s does not exist!", playerName));
		
		return false;
	}
	
}
