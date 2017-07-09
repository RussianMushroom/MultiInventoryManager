package io.github.russianmushroom.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
	
	private List<ItemStack> playerInventory = Collections.synchronizedList(new ArrayList<>());
	private List<ItemStack> playerEnderInventory = Collections.synchronizedList(new ArrayList<>());
	
	private Player player;
	
	private double playerHealth;
	
	private float playerXP;
	
	private int playerLvl;
	
	private UUID playerUUID;
	
	public PlayerManager(Player player) {
		// Set variables
		this.player = player;
		
		this.playerInventory = Arrays.asList(player.getInventory().getContents());
		this.playerHealth = player.getHealth();
		this.playerXP = player.getExp();
		this.playerLvl = player.getLevel();
		this.playerUUID = player.getUniqueId();
	}

	// Getters
	
	public Optional<List<ItemStack>> getPlayerInv() {
		return Optional.of(playerInventory);
	}

	public Optional<List<ItemStack>> getPlayerEnderInv() {
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

	public int getPlayerLvl() {
		return playerLvl;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
}
