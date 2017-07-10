package io.github.russianmushroom.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

/**
 * All base YAML methods
 * @author RussianMushroom
 */
public class BaseYAML {
	
	private static final File config = new File("plugins/MIMconfig.yml");
	
	private static File baseFile = new File("plugins/MIM/Player Data");
	
	private static Yaml ymal = new Yaml();
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getData(File targetFile) throws IOException {
		return (Map<String, Object>) ymal.load(
				new FileInputStream(targetFile));
	}
	
	/**
	 * This reads the {@link MIMconfig.yml} file in the plugins folder to get the <code>basePath</code>.
	 * If it is not found, it resorts to a default.
	 * @return basePath of the files.
	 */
	public static Path getPlayerFolder() {
			try {
				if(!config.exists())
					Logger.getLogger("Minecraft").log(Level.WARNING,
							String.format("%s was not detected, using default path: %s",
									config.toString(),
									baseFile.toString()));
				else
					baseFile = new File(getData(config).get("basePath").toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return baseFile.toPath();
	}

}
