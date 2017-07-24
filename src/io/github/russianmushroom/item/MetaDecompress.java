package io.github.russianmushroom.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

/**
 * Decompress serialised buffs, items and meta data to their default item type.
 * @author RussianMushroom
 *
 */
public class MetaDecompress {

	private static PotionMeta pMeta;
	private static FireworkMeta fMeta;
	private static BannerMeta bMeta;
	private static MapMeta mMeta;
	private static BlockStateMeta bStackMeta;
	private static SkullMeta sMeta;
	private static SpawnEggMeta sEggMeta;
	
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
				String[] en = e.toString().split("~");
				enchantments.put(
						Enchantment.getByName(en[0]),
						en.length != 2 ? 1 : Integer.parseInt(en[1])
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
                	if(!enchant.isEmpty() && enchant != null)
                		enchant.keySet().forEach(e -> {
                    		iMeta.addEnchant(e, (enchant.get(e) != null) ? enchant.get(e) : 1, true);
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
                	break;
                // Banners
                case "B":
                	bMeta = (BannerMeta) iMeta;
                	
                	Arrays.asList(effectList.get(effect).split("\\~"))
                		.forEach(b -> {
                			Pattern pattern = new Pattern(
                					DyeColor.getByColor(toRGB(b.split("\\+")[0])),
                					PatternType.valueOf(b.split("\\+")[1]));
                			bMeta.addPattern(pattern);
                					
                		});
                	break;
                	// Maps
                case "M":
                	mMeta = (MapMeta) iMeta;
                	String[] mapDetails = effectList.get(effect).split("\\+");
                	
                	if(!mapDetails[0].equals(""))
                		mMeta.setColor(toRGB(mapDetails[0]));
                	if(!mapDetails[1].equals(""))
                		mMeta.setLocationName(mapDetails[1]);
                	if(!mapDetails[2].equals(""))
                		mMeta.setScaling(Boolean.getBoolean(mapDetails[2]));
                	break;
                // Shulker boxes
                case "S":
        			ShulkerBox shulker = (ShulkerBox) ((BlockStateMeta) iMeta).getBlockState();
        			String[] items = effectList.get(effect).split("\\.");
        			bStackMeta = (BlockStateMeta) iMeta;

					shulker.getInventory().clear();
					
					// Set shulker inventory
					for(int i = 0; i < items.length; i++) {
    					Stack stack = new Stack(items[i], "\\&");
    					
    					shulker.getInventory().setItem(i, stack.toItemStack());
					}
					
					bStackMeta.setBlockState(shulker);
        			break;
        		// Repairables
                case "I":
                	Repairable repair = (Repairable) iMeta;
                	
                	repair.setRepairCost(Integer.parseInt(effectList.get(effect)));              
                	break;
                // SkullMeta
                case "K":
                	sMeta = (SkullMeta) iMeta;
                	
                	sMeta.setOwner(effectList.get(effect).toString());
                	break;
                // Spawn eggs
                case "G":
                	sEggMeta = (SpawnEggMeta) iMeta;
                	
                	sEggMeta.setSpawnedType(EntityType.valueOf(effectList.get(effect).toString()));
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
		colour = colour.replaceAll("\\~", "");
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
