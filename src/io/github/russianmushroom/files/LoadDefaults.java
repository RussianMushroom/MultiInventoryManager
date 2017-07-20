package io.github.russianmushroom.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.russianmushroom.yaml.BaseYAML;

/**
 * Create the default directories.
 * @author RussianMushroom
 *
 */
public class LoadDefaults {

	public static final File CONFIG_BASE = new File("plugins/MultiInventoryManager/");
	public static final File CONFIG_FILE = new File(CONFIG_BASE + File.separator + "MIMconfig.yml");
	public static File playerSaveBase = new File(LoadDefaults.CONFIG_BASE + File.separator + "playerdata");
	
	public static void load() {
		createPlayerSaveBase();
		createConfigFile();
	}
	
	/**
	 * This reads the {@link MIMconfig.yml} file in the plugins folder to get the <code>basePath</code>.
	 * If it is not found, it resorts to a default.
	 * @return basePath of the files.
	 */
	public static Path getPlayerFolder() {
			try {
				if(!CONFIG_FILE.exists())
					Logger.getLogger("Minecraft").log(Level.WARNING,
							String.format("%s was not detected, using default path: %s",
									CONFIG_FILE.toString(),
									playerSaveBase.toString()));
				else {
					if(BaseYAML.getData(CONFIG_FILE).isPresent())
						playerSaveBase = new File(BaseYAML.getData(CONFIG_FILE).get().get("basePath").toString());
				}
					
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			createPlayerSaveBase();
			
			return playerSaveBase.toPath();
	}
	
	private static void createPlayerSaveBase() {
		// Create directory to save player's data
		if(!playerSaveBase.exists())
			playerSaveBase.mkdirs();
	}
	
	private static void createConfigFile() {
		// Create config file
		if(!CONFIG_FILE.exists()) {
			try {
				FileWriter fWriter = new FileWriter(CONFIG_FILE);
				fWriter.write("basePath: \"plugins/MultiInventoryManager/playerdata\"\nadventure: false\nspectator: false");
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				Logger.getLogger("Minecraft").log(Level.WARNING,
						String.format("%s could not be created, please add the MIMconfig.yml manually.",
								CONFIG_FILE.toString()));
			}
		}
	}
	
}
