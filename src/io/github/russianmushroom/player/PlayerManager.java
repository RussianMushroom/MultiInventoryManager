package io.github.russianmushroom.player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import io.github.russianmushroom.item.Stack;

public class PlayerManager {
	
	private String playerInventory = "";
	private String playerArmour = "";
	private String playerEnderInventory = "";
	
	private Player player;
	
	private double playerHealth;
	
	private float playerXP;
	private float playerSaturation;
	
	private int playerLvl;
	
	private UUID playerUUID;
	
	private Collection<PotionEffect> potionEffects;
	
	
	public PlayerManager(Player player) {
		// Set variables
		this.player = player;

		for(int i = 0; i < player.getInventory().getContents().length; i++) {
			this.playerInventory += new Stack(
					player.getInventory().getContents()[i], i)
					.toString() + "#";
		}
		
		this.playerHealth = player.getHealth();
		this.playerXP = player.getExp();
		this.playerLvl = player.getLevel();
		this.playerUUID = player.getUniqueId();
		this.playerSaturation = player.getSaturation();
		this.potionEffects = player.getActivePotionEffects();
	}

	// Getters
	
	public Optional<String> getPlayerInv() {
		return Optional.of(playerInventory);
	}

	public Optional<String> getPlayerEnderInv() {
		return Optional.of(playerEnderInventory);
	}

	public Player getPlayer() {
		return player;
	}

	public double getPlayerHealth() {
		return playerHealth;
	}

	public float getPlayerXP() {
		return playerXP;
	}
	
	public float getPlayerSaturation() {
		return playerSaturation;
	}

	public int getPlayerLvl() {
		return playerLvl;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	
	
}
