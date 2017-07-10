package io.github.russianmushroom.item;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Stack extends Object {

	private int quantity = 0;
	
	private Material type = Material.AIR;
	
	private short durability = 0;
	
	private ItemMeta meta;
	
	private Map<Enchantment, Integer> entchantments = new HashMap<>();
	
	
	public Stack(ItemStack iStack) {
		if(iStack != null) {
			this.quantity = iStack.getAmount();
			this.type = iStack.getType();
			this.durability = iStack.getDurability();
			this.entchantments = iStack.getEnchantments();
		}
	}
	
}
