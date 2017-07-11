package io.github.russianmushroom.item;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;


public class Stack extends Object {

	private int quantity = 0;
	
	private Material type = Material.AIR;
	
	private short durability = 0;
	
	private String metaTags = "";
	
	private Map<Enchantment, Integer> entchantments = new HashMap<>();
	
	public Stack(ItemStack iStack) {
		if(iStack != null) {
			this.quantity = iStack.getAmount();
			this.type = iStack.getType();
			this.durability = iStack.getDurability();
			this.entchantments = iStack.getEnchantments();
			/*
			if(iStack.getItemMeta() instanceof BookMeta) {
				
			} else */if (iStack.hasItemMeta())
				metaTags = MetaCompress.compressMetaData(iStack);
		}
		
		
		Bukkit.broadcastMessage(metaTags);
	}
	
}
