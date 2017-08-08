package io.github.russianmushroom;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.russianmushroom.command.DeleteData;
import io.github.russianmushroom.files.LoadDefaults;
import io.github.russianmushroom.listener.PlayerListener;
import io.github.russianmushroom.player.PlayerFileManager;
import io.github.russianmushroom.player.PlayerManager;

/**
 * Separates the inventories as well as data of players from one another through worlds 
 * (Survival, Creative, Adventure and Spectator)
 * @author RussianMushroom
 *
 */
public class MultiInventoryManager extends JavaPlugin {
	
	private final PluginDescriptionFile pdfFile = this.getDescription();
	
	@Override
	public void onEnable() {
		// Add necessary files
		LoadDefaults.load();
		// Attach listeners
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		getLogger().info(String.format("[%s] Saving everyone's data.", pdfFile.getName()));
		
		// Save everyone's data
		if(!getServer().getOnlinePlayers().isEmpty()) {
			getServer().getOnlinePlayers()
			.parallelStream()
			.forEach(player -> {
				try {
					PlayerFileManager.handle(
							new PlayerManager(player.getPlayer()), 
							true,
							player.getGameMode());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
		super.onDisable();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("mim")) {
			if(args.length == 0)
				sender.sendMessage("Invalid syntax: /mim delete [player name]");
			else if(args.length > 2)
				sender.sendMessage("Invalid syntax: /mim delete [player name]");
			else {
				if(args[0].equalsIgnoreCase("delete"))
					DeleteData.remove(sender, args[1]);
				else
					sender.sendMessage("Invalid command!");
			}
		}
		return super.onCommand(sender, command, label, args);
	}

}
     