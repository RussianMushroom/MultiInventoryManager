package io.github.russianmushroom.item;

import java.util.Arrays;

import org.bukkit.entity.Player;

/**
 * Deal with the player's ender chest inventory
 * @author RussianMushroom
 *
 */
public class EnderInventory {
	
	private String enderInventory = "";

	public EnderInventory(Player player) {
		StringBuilder sBuilder = new StringBuilder();
		
		Arrays.asList(player.getEnderChest().getContents())
			.parallelStream()
			.forEach(iStack -> {
				sBuilder.append(
						new Stack(iStack).toString()
						+ "#");
			});
		
		enderInventory = sBuilder.toString();
	}
	
	public String getEnderInventory() {
		return enderInventory;
	}
	
	
}
