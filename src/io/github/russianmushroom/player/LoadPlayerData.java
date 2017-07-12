package io.github.russianmushroom.player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import io.github.russianmushroom.yaml.BaseYAML;
/**
 * Load player data from file and apply it.
 * @author RussianMushroom
 *
 */
public class LoadPlayerData {
	
	private static Map<String, Object> playerInventory = Collections.synchronizedMap(new HashMap<>());

	public static void load(PlayerManager pManager, GameMode gMode) throws IOException {
		loadPlayerInventory(pManager, gMode);
		
	}
	
	private static void loadPlayerInventory(PlayerManager pManager, GameMode gMode) throws IOException {
		playerInventory = BaseYAML.getAllData(new File(
				BaseYAML.getPlayerFolder() 
				+ File.separator 
				+ pManager.getPlayerUUID().toString() 
				+ ".yml"))
				.get(gMode.toString());
		
		Bukkit.broadcastMessage(gMode.toString());
		
		setPlayerData(pManager);
	}
	
	private static void setPlayerData(PlayerManager pManager) {
		// Set player data
		pManager.getPlayer().setHealth(
				Double.parseDouble(playerInventory.get("playerHealth").toString()));
		pManager.getPlayer().setExp(
				Float.parseFloat(playerInventory.get("playerXP").toString()));
		pManager.getPlayer().setLevel(
				Integer.parseInt(playerInventory.get("playerLvl").toString()));
		
	}
	
}
