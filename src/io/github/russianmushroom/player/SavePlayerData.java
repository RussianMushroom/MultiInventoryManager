package io.github.russianmushroom.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.yaml.snakeyaml.Yaml;

import io.github.russianmushroom.files.LoadDefaults;

/**
 * Load all player's data and save them to player-specific yml file.
 * @author RussianMushroom
 *
 */
public class SavePlayerData {

	private static Map<String, Map<String, Object>> obj = Collections.synchronizedMap(new HashMap<>());
	private static Map<String, Object> data = Collections.synchronizedMap(new HashMap<>());
	private static PlayerManager pManager;
	private static File playerData;
	private static final Yaml yaml = new Yaml();
	
	/**
	 * Get player's gamemode and save player data accordingly
	 * @param pM
	 * @param gMode
	 * @throws IOException
	 */
	public synchronized static void save(PlayerManager pM, GameMode gMode) throws IOException {
		
		pManager = pM;
		
		// Create user dataFile
		playerData = new File(LoadDefaults.getPlayerFolder() 
				+ File.separator 
				+ pManager.getPlayerUUID().toString() 
				+ ".yml");
		
		if(!playerData.exists())
			createDataFile(playerData);
		
		addEntry(gMode);
	}
	
	/**
	 * Create player file with UUID as name. Populate with base template.
	 * @param basePath
	 * @param playerUUID
	 * @throws IOException
	 */
	private static void createDataFile(File file) throws IOException {
		file.createNewFile();
		populateDataFile(file);
	}
	
	/**
	 * Saves player data to yml file
	 * @param gMode
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void addEntry(GameMode gMode) throws IOException {
		
		data.put("playerInventory", pManager.getPlayerInv().orElse("empty"));
		data.put("playerActivePotions", pManager.getPotionEffects().orElse("empty"));
		data.put("playerHealth", pManager.getPlayerHealth());
		data.put("playerXP", pManager.getPlayerXP());
		data.put("playerSaturation", pManager.getPlayerSaturation());
		data.put("playerLvl", pManager.getPlayerLvl());
		data.put("playerHunger", pManager.getPlayerHunger());
		data.put("playerEnderInventory", pManager.getPlayerEnderInv().orElse("empty"));
		
		
		obj = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(playerData));
		
		obj.replace(gMode.toString(), data);
		
		FileWriter writer = new FileWriter(playerData);
		
		yaml.dump(obj, writer);
		writer.flush();
		writer.close();
		
	}
	
	/**
	 * Add base template to {@link dataFile}
	 * @param dataFile
	 */
	private static void populateDataFile(File dataFile) throws IOException {
		
		obj.put(GameMode.SURVIVAL.toString(), new HashMap<>());
		obj.put(GameMode.CREATIVE.toString(), new HashMap<>());
		obj.put(GameMode.SPECTATOR.toString(), new HashMap<>());
		obj.put(GameMode.ADVENTURE.toString(), new HashMap<>());
		
		FileWriter writer = new FileWriter(dataFile);
		
		yaml.dump(obj, writer);
		writer.flush();
		writer.close();
		
		Bukkit.getServer().getLogger().info(String.format(
				"[MultiInventoryManager] %s's player file has been created!", pManager.getPlayer().getName()));
		
	}
	
}
