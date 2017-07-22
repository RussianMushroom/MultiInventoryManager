package io.github.russianmushroom.player;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.inventory.Inventory;

import io.github.russianmushroom.files.LoadDefaults;
import io.github.russianmushroom.item.MetaDecompress;
import io.github.russianmushroom.item.Stack;
import io.github.russianmushroom.yaml.BaseYAML;
/**
 * Load player data from file and apply it.
 * @author RussianMushroom
 *
 */
public class LoadPlayerData {
	
	private static Map<String, Object> playerInventory = Collections.synchronizedMap(new HashMap<>());

	public synchronized static void load(PlayerManager pManager, GameMode gMode) {
		loadPlayerInventory(pManager, gMode);
	}
	
	private static void loadPlayerInventory(PlayerManager pManager, GameMode gMode) {
		Optional<Map<String, Map<String, Object>>> prelimPlayerInventory = BaseYAML.getAllData(new File(
				LoadDefaults.getPlayerFolder() 
				+ File.separator 
				+ pManager.getPlayerUUID().toString() 
				+ ".yml"));
		
		// Set data to player
		if(prelimPlayerInventory.isPresent()) {
			playerInventory = prelimPlayerInventory.get().get(gMode.toString());
			
			if(!playerInventory.isEmpty()) {
				setPlayerData(pManager);
				setPlayerInventory(pManager);
				setPlayerEnderInventory(pManager, pManager.getPlayer().getEnderChest());
			}
		}
	}
	
	private static void setPlayerData(PlayerManager pManager) {
		// Set player data
		pManager.getPlayer().setHealth(
				Double.parseDouble(playerInventory.get("playerHealth").toString()));
		pManager.getPlayer().setExp(
				Float.parseFloat(playerInventory.get("playerXP").toString()));
		pManager.getPlayer().setLevel(
				Integer.parseInt(playerInventory.get("playerLvl").toString()));
		pManager.getPlayer().addPotionEffects(
				MetaDecompress.decompressPotionEffects(
						playerInventory.get("playerActivePotions").toString()));
		pManager.getPlayer().setFoodLevel(
				Integer.parseInt(playerInventory.get("playerHunger").toString()));
	}
	
	/**
	 * Split the saved inventory into it's components
	 * <ul>
	 * <li><code>.split("#")</code>: to get individual items</li>
	 * <li><code>.split(";")</code>: to get individual components, like Durability, MetaData etc.</li>
	 * <li>get MetaTags (Last in list) and decompress it</li>
	 * @param pManager
	 */
	private static void setPlayerInventory(PlayerManager pManager) {
		// clear the player's current inventory
		pManager.getPlayer().getInventory().clear();
		
		String playerInv = playerInventory.get("playerInventory").toString();
		addToInventory(playerInv, pManager.getPlayer().getInventory());

	}
	
	/**
	 * Get the saved ender inventory and assign it to the ender chest.
	 * @param pManager
	 * @param enderInventory
	 */
	private static void setPlayerEnderInventory(PlayerManager pManager, Inventory enderInventory) {
		// Clear player's ender inventory
		pManager.getPlayer().getEnderChest().clear();
		
		String playerEnderInventory = pManager.getPlayerEnderInv().orElse("");
		addToInventory(playerEnderInventory, pManager.getPlayer().getEnderChest());
		
	}
	
	private static void addToInventory(String invString, Inventory inventory) {
		List<String> individualItems = Arrays.asList(invString.split("#"));
		
		for(int i = 0; i < individualItems.size(); i++) {
			Stack stack = new Stack(individualItems.get(i), ":");
			
			inventory.setItem(i, stack.toItemStack());;
		}
	}
	
}
