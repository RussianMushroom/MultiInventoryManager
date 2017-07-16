package io.github.russianmushroom.player;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.russianmushroom.item.MetaCompress;
import io.github.russianmushroom.item.Stack;

public class PlayerManager {
	
	private String playerInventory = "";
	// private String playerArmour = "";
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

		for(int i = 0; i < player.getInventory().getContents().length; i++) {
			this.playerInventory += new Stack(
					player.getInventory().getContents()[i])
					.toString() + "#";
		}
	
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
