package io.github.russianmushroom.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import io.github.russianmushroom.item.MetaCompress;
import io.github.russianmushroom.item.MetaDecompress;
import io.github.russianmushroom.item.Stack;
import io.github.russianmushroom.player.PlayerFileManager;
import io.github.russianmushroom.player.PlayerManager;
import io.github.russianmushroom.player.SavePlayerData;
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
	
	// Used for testing purposes
	@EventHandler
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		for(ItemStack s : event.getPlayer().getInventory().getContents()) {
			if(s != null) {
				Stack stack = new Stack(s);
				Bukkit.broadcastMessage(stack.toString());
			}
		}
		PlayerManager pM = new PlayerManager(event.getPlayer());
		try {
			PlayerFileManager.handle(pM, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
