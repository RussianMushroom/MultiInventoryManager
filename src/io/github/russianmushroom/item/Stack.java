package io.github.russianmushroom.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Stack extends Object {

	private int quantity = 0;
	
	private Material type = Material.AIR;
	
	private short durability = 0;
	
	private String metaTags = "";
	
	// private Map<Enchantment, Integer> entchantments = new HashMap<>();
	
	public Stack(ItemStack iStack) {
		if(iStack != null) {
			this.quantity = iStack.getAmount();
			this.type = iStack.getType();
			this.durability = iStack.getDurability();
			// this.entchantments = iStack.getEnchantments();
			/*
			if(iStack.getItemMeta() instanceof BookMeta) {
				
			} else */if (iStack.hasItemMeta())
				metaTags = MetaCompress.compressMetaData(iStack);
		}
	}
	
	@Override
	public String toString() {
		return String.format(
				"Quantity:%sType:%sDurability:%sMeta:%s",
				quantity,
				type,
				durability,
				metaTags
				);
	}
	
}
