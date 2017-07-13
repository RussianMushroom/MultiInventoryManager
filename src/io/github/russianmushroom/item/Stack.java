package io.github.russianmushroom.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Stack extends Object {

	private int quantity = 0;
	private int slot = 0;
	
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
	
	/**
	 * Receives raw input from save file and converts to Stack
	 * @param iStack
	 */
	public Stack(String iStack) {
		List<String> individualComp = Arrays.asList(iStack.split("::"));
		
		quantity = Integer.parseInt(individualComp.get(0));
		type = Material.getMaterial(individualComp.get(1));
		durability = Short.parseShort(individualComp.get(2));
		metaTags = individualComp.get(3);
	}
	
	public ItemStack toItemStack() {
		ItemStack iStack = new ItemStack(type);
		
		iStack.setAmount(quantity);
		iStack.setDurability(durability);
		iStack = MetaDecompress.decompressMetaData(iStack, metaTags);
		
		return iStack;
	}
	
	@Override
	public String toString() {
		return String.format(
				"::%s::%s::%s::%s",
				quantity,
				type,
				durability,
				metaTags
				);
	}
	
}
