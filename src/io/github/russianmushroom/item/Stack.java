package io.github.russianmushroom.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class Stack extends Object {

	private int quantity = 0;
	private int slot = 0;
	
	private Material type = Material.AIR;
	
	private short durability = 0;
	
	private String metaTags = null;
	private String enchantTags= null;
	
	private Map<Enchantment, Integer> entchantments = Collections.synchronizedMap(new HashMap<>());
	
	private List<String> individualComp;
	
	public Stack(ItemStack iStack) {
		if(iStack != null) {
			this.quantity = iStack.getAmount();
			this.type = iStack.getType();
			this.durability = iStack.getDurability();
			this.entchantments = iStack.getEnchantments();
			this.enchantTags = MetaCompress.compressEnchantments(entchantments);
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
		individualComp = Arrays.asList(iStack.split(":"));
		Bukkit.broadcastMessage(individualComp.size() + "");
		if(has(0))
			quantity = Integer.parseInt(individualComp.get(0));
		if(has(1))
			type = Material.getMaterial(individualComp.get(1));
		if(has(2))
			durability = Short.parseShort(individualComp.get(2));
		if(has(3))
			metaTags = individualComp.get(3);
		if(has(4))
			enchantTags = individualComp.get(4);
	}
	
	public ItemStack toItemStack() {
		ItemStack iStack = new ItemStack(type);
		
		iStack.setAmount(quantity);
		iStack.setDurability(durability);
		iStack = MetaDecompress.decompressMetaData(iStack, metaTags);
		iStack.addEnchantments(MetaDecompress.decompressEnchantments(enchantTags));
		
		return iStack;
	}
	
	@Override
	public String toString() {
		return String.format(
				"%s:%s:%s:%s:%s:",
				quantity,
				type,
				durability,
				metaTags,
				enchantTags
				);
	}
	
	private boolean has(int i) {
		return (individualComp.get(i) == null | individualComp.get(i).equals("null")) ? false : true;
	}
	
}
