package io.github.russianmushroom.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Replacement class for vanilla ItemStack.
 * @author RussianMushroom
 *
 */
public class Stack extends Object {

	private int quantity = 0;
	private Material type = Material.AIR;
	private short durability = 0;
	private String metaTags = "";
	private String enchantTags= "";
	private Map<Enchantment, Integer> enchantments = Collections.synchronizedMap(new HashMap<>());
	private List<String> individualComp;
	
	public Stack(ItemStack iStack) {
		if(iStack != null) {
			this.quantity = iStack.getAmount();
			this.type = iStack.getType();
			this.durability = iStack.getDurability();
			this.enchantments = iStack.getEnchantments();
			this.enchantTags = MetaCompress.compressEnchantments(enchantments);
			
			if(iStack.getItemMeta() instanceof BookMeta) {
				BookMeta bMeta = (BookMeta) iStack.getItemMeta();
				if(!bMeta.getPages().isEmpty()) {
					// Book book = new Book(bMeta.getTitle(), bMeta.getAuthor(), bMeta.getPages());
					// metaTags = book.get
				}
			} else if (iStack.hasItemMeta())
				metaTags = MetaCompress.compressMetaData(iStack);
		}
	}
	
	/**
	 * Receives raw input from save file and converts to Stack
	 * @param iStack
	 */
	public Stack(String iStack, String splitChar) {
		individualComp = Arrays.asList(iStack.split(splitChar));
		
		// Check all elements
		quantity = has(0) ? Integer.parseInt(individualComp.get(0)) : 0;
		type = has(1) ? Material.getMaterial(individualComp.get(1)) : Material.AIR;
		durability = has(2) ? Short.parseShort(individualComp.get(2)) : 0;
		metaTags = has(3) ? individualComp.get(3) : null;
		enchantTags = has(4) ? individualComp.get(4) : null;
	}
	
	public ItemStack toItemStack() {
		ItemStack iStack = new ItemStack(type);
		
		iStack.setAmount(quantity);
		iStack.setDurability(durability);
		
		ItemMeta iMeta = MetaDecompress.decompressMetaData(iStack, metaTags);
		if(iMeta != null)
			iStack.setItemMeta(iMeta);
		
		enchantments = MetaDecompress.decompressEnchantments(enchantTags);
		if(enchantTags != null)
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
	
	public String toShulkerString() {
		return String.format(
				"%s&%s&%s&%s&%s&",
				quantity,
				type,
				durability,
				metaTags,
				enchantTags
				);
	}
	
	private boolean has(int i) {
		try {
			return !individualComp.get(i).equals("");
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
}
