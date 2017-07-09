package io.github.russianmushroom;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.russianmushroom.listener.PlayerListener;

public class MultiInventoryManager extends JavaPlugin {
	
	private final Logger log = getServer().getLogger();
	private final PluginDescriptionFile pdfFile = this.getDescription();
	
	@Override
	public void onEnable() {
		this.log.info(pdfFile.getName() + " has been enabled!");
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		this.log.info(pdfFile.getName() +" v" + pdfFile.getVersion() + " has been disabled!");
		
		super.onDisable();
	}

}
     