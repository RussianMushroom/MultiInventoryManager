package io.github.russianmushroom.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.yaml.snakeyaml.Yaml;

import io.github.russianmushroom.files.LoadDefaults;

/**
 * All base YAML methods
 * @author RussianMushroom
 */
public class BaseYAML {
	
	static boolean saveAdventure = false;
	static boolean saveSpectator = false;
	private static Yaml ymal = new Yaml();
	
	@SuppressWarnings("unchecked")
	public static Optional<Map<String, Object>> getData(File targetFile)
			throws IOException, FileNotFoundException {
		return Optional.of((Map<String, Object>) ymal.load(
				new FileInputStream(targetFile)));
	}
	
	@SuppressWarnings("unchecked")
	public static Optional<Map<String, Map<String, Object>>> getAllData(File targetFile) 
			throws IOException, FileNotFoundException {
		return Optional.of((Map<String, Map<String, Object>>) ymal.load(
				new FileInputStream(targetFile)));
	}
	
	
	public static boolean getSaveSpectator() {
		try {
			if(!getData(LoadDefaults.CONFIG_FILE).isPresent())
				return saveSpectator;
			return Boolean.valueOf(getData(LoadDefaults.CONFIG_FILE).get().get("spectator").toString());
		} catch (IOException e) {
			return saveSpectator;
		}
	}
	
	public static boolean getSaveAdventure() {
		try {
			if(!getData(LoadDefaults.CONFIG_FILE).isPresent())
				return saveAdventure;
			return Boolean.valueOf(getData(LoadDefaults.CONFIG_FILE).get().get("adventure").toString());
		} catch (IOException e) {
			return saveAdventure;
		}
	}

}
