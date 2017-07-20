package io.github.russianmushroom.player;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.russianmushroom.item.MetaCompress;
import io.github.russianmushroom.item.Stack;

/**
 * Applies player data to PlayerManager.
 * @author RussianMushroom
 *
 */
public class PlayerManager {
	
	private String playerInventory = "";
	private String playerEnderInventory = "";
	private String playerActivePotions = "";
	private Player player;
	private double playerHealth;
	private float playerXP;
	private float playerSaturation;
	private int playerLvl;
	private int playerHunger;
	private UUID playerUUID;
	
	
	public PlayerManager(Player player) {
		// Set variables
		this.player = player;

		// Bind player inventory
		Arrays.asList(player.getInventory().getContents())
			.forEach(i -> {
				playerInventory += new Stack(i).toString() + "#";
			});
		
		// Bind player ender inventory
		Arrays.asList(player.getEnderChest().getContents())
			.forEach(i -> {
				playerEnderInventory += new Stack(i).toString() + "#";
			});
	
		this.playerActivePotions = MetaCompress.compressPotionBuffs(
				player.getActivePotionEffects());
		this.playerHealth = player.getHealth();
		this.playerXP = player.getExp();
		this.playerLvl = player.getLevel();
		this.playerHunger = player.getFoodLevel();
		this.playerUUID = player.getUniqueId();
		this.playerSaturation = player.getSaturation();
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
	
	public int getPlayerHunger() {
		return playerHunger;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Optional<String> getPotionEffects() {
		return Optional.of(playerActivePotions);
	}
	
	
}
