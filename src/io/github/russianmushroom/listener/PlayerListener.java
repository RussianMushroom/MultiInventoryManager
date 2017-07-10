package io.github.russianmushroom.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.russianmushroom.yaml.BaseYAML;

/**
 * Handle all events connected to the player. Update database accordlingly.
 * @author RussianMushroom
 *
 */
public class PlayerListener implements Listener{

	/**
	 * Check the world type and load player's information accordingly
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		BaseYAML.getPlayerFolder();
		// Update files
	}
	
	/**
	 * Check the world type and save player's information accordingly.
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		BaseYAML.getPlayerFolder();
		// Update files
	}
	
	
	
}
