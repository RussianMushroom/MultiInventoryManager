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
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.russianmushroom.player.PlayerFileManager;
import io.github.russianmushroom.player.PlayerManager;


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
			// Remove all potion effects
			removePotionEffects(event.getPlayer());

			PlayerFileManager.handle(
					new PlayerManager(event.getPlayer()), 
					false,
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			// displayWarning(event.getPlayer(), false);
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
			PlayerFileManager.handle(
					new PlayerManager(event.getPlayer()), 
					true,
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), true);
		}
		
		// Load data for next GameMode
		try {
			// Remove all potion effects
			removePotionEffects(event.getPlayer());
			
			PlayerFileManager.handle(
					new PlayerManager(event.getPlayer()), 
					false,
					event.getNewGameMode());
		} catch (IOException e) {
			// displayWarning(event.getPlayer(), false);
		}
		
	}
	
	/**
	 * Handle player kicks: Save player data.
	 * @param event
	 */
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		// Update files
				try {
					PlayerFileManager.handle(
							new PlayerManager(event.getPlayer()), 
							true,
							event.getPlayer().getGameMode());
				} catch (IOException e) {
					displayWarning(event.getPlayer(), true);
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
			PlayerFileManager.handle(
					new PlayerManager(event.getPlayer()), 
					true,
					event.getPlayer().getGameMode());
		} catch (IOException e) {
			displayWarning(event.getPlayer(), true);
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
