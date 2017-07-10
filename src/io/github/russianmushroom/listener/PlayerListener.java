package io.github.russianmushroom.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import io.github.russianmushroom.player.PlayerFileManager;
import io.github.russianmushroom.player.PlayerManager;
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
		// Update files
	}
	
	/**
	 * Check the world type and save player's information accordingly.
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Update files
	}
	
	@EventHandler
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		try {
			PlayerFileManager.save(new PlayerManager(event.getPlayer()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
