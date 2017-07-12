package io.github.russianmushroom.listener;

import java.io.IOException;

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

	/**
	 * Check the world type and load player's information accordingly
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Update files
		try {
			LoadPlayerData.load(
					new PlayerManager(event.getPlayer()), 
					event.getPlayer().getGameMode());
		} catch (IOException e) {
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
			e.printStackTrace();
		}
		
		// Load data for next GameMode
		try {
			LoadPlayerData.load(
					new PlayerManager(event.getPlayer()),
					event.getNewGameMode());
		} catch (IOException e) {
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
			e.printStackTrace();
		}
	}
	
	
}
