package io.github.russianmushroom.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.russianmushroom.player.LoadPlayerData;
import io.github.russianmushroom.player.PlayerManager;
import io.github.russianmushroom.player.SavePlayerData;


/**
 * Handle all events connected to the player. Update database accordlingly.
 * @author RussianMushroom
 *
 */
public class PlayerListener implements Listener{

	Logger logger = Bukkit.getServer().getLogger();
	
	/**
	 * Check the world type and load player's information accordingly
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Update files
		try {
			// Clear the player's current inventory
			event.getPlayer().getInventory().clear();
			
			// Remove all potion effects
			removePotionEffects(event.getPlayer());
			
			LoadPlayerData.load(
					new PlayerManager(event.getPlayer()), 
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), false);
			e.printStackTrace();
		}
	}
	
	/**
	 * Check the previous GameMode and save and load new GameMode
	 * @param event
	 */
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		// Save previous GameMode
		try {
			SavePlayerData.save(
					new PlayerManager(event.getPlayer()), 
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), true);
			e.printStackTrace();
		}
		
		// Load data for next GameMode
		try {
			// clear the player's current inventory
			event.getPlayer().getInventory().clear();
			
			// Remove all potion effects
			removePotionEffects(event.getPlayer());
			
			LoadPlayerData.load(
					new PlayerManager(event.getPlayer()),
					event.getNewGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), false);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Check the world type and save player's information accordingly.
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Update files
		try {
			SavePlayerData.save(
					new PlayerManager(event.getPlayer()), 
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), true);
			e.printStackTrace();
		}
	}
	
	private void displayWarning(Player player, boolean saving) {
		logger.log(Level.WARNING, String.format(
				"Could not %s %s's inventory %s the file!",
				saving ? "save" : "load",
				player.getName(),
				saving ? "to" : "from")
				.toString());
	}
	
	private void removePotionEffects(Player player) {
		player.getActivePotionEffects().forEach(e -> {
			player.removePotionEffect(e.getType());
		});
		
	}
	
}
