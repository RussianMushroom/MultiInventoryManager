package io.github.russianmushroom.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.yaml.snakeyaml.Yaml;

import io.github.russianmushroom.files.LoadDefaults;

/**
 * All base YAML methods
 * @author RussianMushroom
 */
public class BaseYAML {
	
	private static boolean saveAdventure = false;
	private static boolean saveSpectator = false;
	private static Yaml ymal = new Yaml();
	
	@SuppressWarnings("unchecked")
	public static Optional<Map<String, Object>> getData(File targetFile) {
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(targetFile);
			return Optional.of((Map<String, Object>) ymal.load(iStream));
		} catch (IOException e) {
			// Print to console that the file does not exist
			return Optional.empty();
		} finally {
			 try {
				 if(iStream != null)
					 iStream.close();
			} catch (IOException e) {
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Optional<Map<String, Map<String, Object>>> getAllData(File targetFile) {
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(targetFile);
			return Optional.of((Map<String, Map<String, Object>>) ymal.load(iStream));
		} catch (IOException e) {
			// Print to console that the file does not exist
			// Gets called when file is missing.
			return Optional.empty();
		} finally {
			 try {
				 if(iStream != null)
					 iStream.close();
			} catch (IOException e) {
			}
		}
	}
	
	
	public static boolean getSaveSpectator() {
		if(!getData(LoadDefaults.CONFIG_FILE).isPresent())
			return saveSpectator;
		return Boolean.valueOf(getData(LoadDefaults.CONFIG_FILE).get().get("spectator").toString());
	}
	
	public static boolean getSaveAdventure() {
		if(!getData(LoadDefaults.CONFIG_FILE).isPresent())
			return saveAdventure;
		return Boolean.valueOf(getData(LoadDefaults.CONFIG_FILE).get().get("adventure").toString());
	}
	
}
