package io.github.russianmushroom.player;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.GameMode;

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
	public synchronized static void handle(PlayerManager pManager, boolean saving) 
			throws IOException, FileNotFoundException {
		switch (pManager.getPlayer().getGameMode()) {
		case ADVENTURE:
			if(BaseYAML.getSaveAdventure()) {
				if(saving)
					SavePlayerData.save(pManager, GameMode.ADVENTURE);
				else
					LoadPlayerData.load(pManager, GameMode.ADVENTURE);
			}
			break;
		case CREATIVE:
			if(saving)
				SavePlayerData.save(pManager, GameMode.CREATIVE);
			else
				LoadPlayerData.load(pManager, GameMode.CREATIVE);
			break;
		case SPECTATOR:
			if(BaseYAML.getSaveSpectator()) {
				if(saving)
					SavePlayerData.save(pManager, GameMode.SPECTATOR);
				else
					LoadPlayerData.load(pManager, GameMode.SPECTATOR);
			}
			break;
		case SURVIVAL:
			if(saving)
				SavePlayerData.save(pManager, GameMode.SURVIVAL);
			else
				LoadPlayerData.load(pManager, GameMode.SURVIVAL);
			break;
		}
	}
	
	
	
}
