package io.github.russianmushroom.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.yaml.snakeyaml.Yaml;

import io.github.russianmushroom.yaml.BaseYAML;

/**
 * Append player's data to file
 * @author RussianMushroom
 *
 */
public class PlayerFileManager {

	/**
	 * Central handling. Deals with saving and loading player data
	 * @param pManager
	 * @param saving
	 * @throws IOException
	 */
	public static void handle(PlayerManager pManager, boolean saving) throws IOException {
		switch (pManager.getPlayer().getGameMode()) {
		case ADVENTURE:
			if(BaseYAML.getData(BaseYAML.CONFIG).get("adventure").equals("true")) {
				if(saving)
					SavePlayerData.save(pManager, GameMode.ADVENTURE);
			}
			break;
		case CREATIVE:
			if(saving)
				SavePlayerData.save(pManager, GameMode.CREATIVE);
			break;
		case SPECTATOR:
			if(BaseYAML.getData(BaseYAML.CONFIG).get("spectator").equals("true")) {
				if(saving)
					SavePlayerData.save(pManager, GameMode.SPECTATOR);
			}
			break;
		case SURVIVAL:
			if(saving)
				SavePlayerData.save(pManager, GameMode.SURVIVAL);
			break;
		}
	}
	
	
	
}
