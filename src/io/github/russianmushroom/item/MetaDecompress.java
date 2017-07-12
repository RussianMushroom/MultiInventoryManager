package io.github.russianmushroom.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class MetaDecompress {

	private static PotionMeta pMeta;
	
	/**
	 * Read item's meta data from String and generate an ItemStack
	 * @param data
	 * @return ItemStack's meta
	 */
	public static ItemStack decompressMetaData(ItemStack iStack, String meta) {
		ItemMeta iMeta = iStack.getItemMeta();
        
		List<String> metaComp = Arrays.asList(meta.split("="));
		
		// Split up the effects
		Map<String, String> effectList = metaComp
				.parallelStream()
				.collect(Collectors.toConcurrentMap(
						x -> x.toString().substring(0, 1),
						x -> x.toString().substring(1,  x.length()),
						(val, key) -> val + ";" + key));
		
		// Apply all effects
		effectList.keySet()
			.parallelStream()
			.forEach(effect -> {
				switch (effect) {
				// Set name
				case "N":
					iMeta.setDisplayName(effectList.get(effect));
					break;
				// Set lore
				case "L": 
					iMeta.setLore(Arrays.asList(
							effectList.get(effect).split("-")));
					break;
				// Leather armour
				case "A": 
					((LeatherArmorMeta) iMeta).setColor(
							toRGB(effectList.get(effect)));
					break;
				// Potion effects
				case "R":
					pMeta = (PotionMeta) iMeta;
					String[] effectType = effectList.get(effect).split("+");
                    PotionEffect pEffect = new PotionEffect(
                    		PotionEffectType.getByName(effectType[0]),
                    		Integer.parseInt(effectType[1]),
                    		Integer.parseInt(effectType[2]));
                    
                    pMeta.addCustomEffect(pEffect, true);
                    break;
                // Potion data
                case "P":
                    pMeta = (PotionMeta) iMeta;
					String[] effectData = effectList.get(effect).split("+");
					PotionData pData = new PotionData(
							PotionType.valueOf(effectData[0]),
							Boolean.valueOf(effectData[1]),
							Boolean.valueOf(effectData[2])
							);
					
                    pMeta.setBasePotionData(pData);
					break;
					
				}
					
			});
		
		iStack.setItemMeta(iMeta);
		
		return iStack;
		
	}
	
	/**
	 * Splits string (###.###.###) into three ints and returns a Color
	 * @param colour Expecting ###.###.###
	 * @return
	 */
	private static Color toRGB(String colour) {
		String[] rgb = colour.split(".");
		return Color.fromRGB(
				Integer.parseInt(rgb[0]),
				Integer.parseInt(rgb[1]),
				Integer.parseInt(rgb[2]));
	}
	
}
