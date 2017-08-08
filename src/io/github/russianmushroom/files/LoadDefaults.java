package io.github.russianmushroom.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.bukkit.Bukkit;

import io.github.russianmushroom.yaml.BaseYAML;

/**
 * Create the default directories.
 * @author RussianMushroom
 */
public class LoadDefaults {

	public static final File CONFIG_BASE = new File("plugins/MultiInventoryManager/");
	public static final File CONFIG_FILE = new File(CONFIG_BASE + File.separator + "MIMconfig.yml");
	public static File playerSaveBase = new File(LoadDefaults.CONFIG_BASE + File.separator + "playerdata");
	public static File playerSaveBooks = new File(LoadDefaults.CONFIG_BASE + File.separator + "books");
	
	public static void load() {
		createBase(playerSaveBase);
		createBase(playerSaveBooks);
		createConfigFile();
	}
	
	/**
	 * This reads the {@link MIMconfig.yml} file in the plugins folder to get the <code>basePath</code>.
	 * If it is not found, it resorts to a default.
	 * @return basePath of the files.
	 */
	public static Path getPlayerFolder() {
	playerSaveBase = getPath("basePath", playerSaveBase);
	createBase(playerSaveBase);
	
	return playerSaveBase.toPath();
	}
	
	public static Path getBookFolder() {
		playerSaveBooks = getPath("bookPath", playerSaveBooks);
		createBase(playerSaveBooks);
		
		return playerSaveBooks.toPath();
	}
	
	private static File getPath(String pathName, File defaultPath) {
		if(!CONFIG_FILE.exists())
			Bukkit.getServer().getLogger().warning(
					String.format("[MultiInventoryManager] %s was not detected, using default path: %s",
							CONFIG_FILE.toString(),
							defaultPath.toString()));
		else {
			if(BaseYAML.getData(CONFIG_FILE).isPresent())
				defaultPath = new File(BaseYAML.getData(CONFIG_FILE).get().get(pathName).toString());
		}
		
		return defaultPath;
	}
	
	private static void createBase(File base) {
		// Create directory to save player's data
		if(!base.exists())
			base.mkdirs();
	}
	
	private static void createConfigFile() {
		// Create config file
		if(!CONFIG_FILE.exists()) {
			try {
				FileWriter fWriter = new FileWriter(CONFIG_FILE);
				fWriter.write(
						"basePath: \"plugins/MultiInventoryManager/playerdata\""
						+ "\nbookPath: \"plugins/MultiInventoryManager/books\""
						+ "\n\nadventure: false"
						+ "\nspectator: false");
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().warning(
						String.format("[MultiInventoryManager] %s could not be created, please add the MIMconfig.yml manually.",
								CONFIG_FILE.toString()));
			}
		}
	}
	
}
