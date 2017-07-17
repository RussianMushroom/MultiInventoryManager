package io.github.russianmushroom.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.google.common.collect.ImmutableList;

public class MetaDecompress {

	private static PotionMeta pMeta;
	private static FireworkMeta fMeta;
	
	/**
	 * Decompresses all String into Enchantment list.
	 * @param enchant
	 * @return
	 */
	public static Map<Enchantment, Integer> decompressEnchantments(String enchant) {
		if(enchant == null)
			return null;
		
		Map<Enchantment, Integer> enchantments = Collections.synchronizedMap(new HashMap<>());
		
		Arrays.asList(enchant.split(";")).stream()
			.forEach(e -> {
				Bukkit.broadcastMessage(e);
				enchantments.put(
						Enchantment.getByName(e.toString().split("~")[0]),
						Integer.parseInt(e.toString().split("~")[1])
						);	
				});
		
		return enchantments;
	}
	
	/**
	 * Decompress String with all active potions buffs and debuffs.
	 * Format: (Type;Duration;Amplifier:)
	 * @param potionEffects
	 * @return
	 */
	public static Collection<PotionEffect> decompressPotionEffects(String potionEffects) {
		Collection<PotionEffect> effect = new ArrayList<>();
		Arrays.asList(potionEffects.split(":"))
			.forEach(e -> {
				if(!e.equals("")) {
					String[] effectList = e.split(";");
					effect.add(new PotionEffect(
							PotionEffectType.getByName(effectList[0]),
							Integer.parseInt(effectList[1]),
							Integer.parseInt(effectList[2])
							));
				}
			});
			return effect;
	}
	
	/**
	 * Read item's meta data from String and generate an ItemMeta
	 * @param data
	 * @return ItemStack's meta
	 */
	public static ItemMeta decompressMetaData(ItemStack iStack, String meta) {
		if(meta == "" || meta == null)
			return null;
		
		ItemMeta iMeta = iStack.getItemMeta();
        
		List<String> metaComp = Arrays.asList(meta.split("="));
		
		// Split up the effects
		Map<String, String> effectList = metaComp
				.parallelStream()
				.collect(Collectors.toConcurrentMap(
						x -> x.toString().substring(0, 1),
						x -> x.toString().substring(1,  x.length()),
						(key, val) -> key + ";" + val));
		
		// Apply all effects
		effectList.keySet()
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
					String[] effectType = effectList.get(effect).split("\\+");
                    PotionEffect pEffect = new PotionEffect(
                    		PotionEffectType.getByName(effectType[0]),
                    		Integer.parseInt(effectType[1]),
                    		Integer.parseInt(effectType[2]));
                    
                    pMeta.addCustomEffect(pEffect, true);
                    break;
                // Potion data
                case "P":
                    pMeta = (PotionMeta) iMeta;
					String[] effectData = effectList.get(effect).split("\\+");
					PotionData pData = new PotionData(
							PotionType.valueOf(effectData[0]),
							Boolean.valueOf(effectData[1]),
							Boolean.valueOf(effectData[2])
							);
					
                    pMeta.setBasePotionData(pData);
					break;
				// Enchanted books
                case "E":
                	Map<Enchantment, Integer> enchant = MetaDecompress.decompressEnchantments(
                			effectList.get(effect));
                	
                	enchant.keySet().forEach(e -> {
                		iMeta.addEnchant(e, enchant.get(e), true);
                	});
                	break;
                // Fireworks
                case "F":
                	fMeta = (FireworkMeta) iMeta;
                	List<String> effects = Arrays.asList(
                			effectList.get(effect).split("="));
                	
                	effects.forEach(firework -> {
                		String[] e = firework.split("\\+");
                		Builder fEffect = FireworkEffect.builder();
                		
                		fEffect.flicker(Boolean.valueOf(e[0]));
                		fEffect.trail(Boolean.valueOf(e[1]));
                		fEffect.withColor(getUnmodifiableList(e[2].split("\\~")));
                		if(!e[3].isEmpty())
                			fEffect.withFade(getUnmodifiableList(e[3].split("\\~")));
                		fEffect.with(Type.valueOf(e[4]));
                
                		fMeta.addEffect(fEffect.build());
                 	});
				}
					
			});
		
		
		return iMeta;
		
	}
	
	/**
	 * Splits string (###.###.###) into three ints and returns a Color
	 * @param colour Expecting ###.###.###
	 * @return
	 */
	private static Color toRGB(String colour) {
		String[] rgb = colour.split("\\.");
		return Color.fromRGB(
				Integer.parseInt(rgb[0]),
				Integer.parseInt(rgb[1]),
				Integer.parseInt(rgb[2]));
	}
	
	private static List<Color> getUnmodifiableList(String[] list) {
		List<Color> colourList=  Arrays.asList(list)
				.stream()
				.map(MetaDecompress::toRGB)
				.collect(Collectors.toList());
		return Collections.unmodifiableList(colourList);
	}
	
}
