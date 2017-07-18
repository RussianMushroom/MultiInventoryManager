package io.github.russianmushroom.item;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

public class MetaCompress {

	/**
	 * Compresses all item's enchantments to a String
	 * @param enchant
	 * @return
	 */
	public static String compressEnchantments(Map<Enchantment, Integer> enchant) {
		StringBuilder sBuilder = new StringBuilder();
		enchant.keySet()
			.forEach(enchantment -> {
				sBuilder.append(
						enchantment.getName() + "~" 
						+ enchant.get(enchantment).toString() + ";");
			});
		
		return sBuilder.toString();
	}
	
	/**
	 * Compress all active potions buffs and debuffs.
	 * Format: (Type;Duration;Amplifier:)
	 * @param potionEffects
	 * @return
	 */
	public static String compressPotionBuffs(Collection<PotionEffect> potionEffects) {
		StringBuilder sBuilder = new StringBuilder();
		potionEffects.forEach(effect -> {
			sBuilder.append(
					effect.getType().getName() + ";"
					+ effect.getDuration() + ";"
					+ effect.getAmplifier() + ":"
					);
		});
		return sBuilder.toString();
	}

	/**
	 * Read item's meta data and compress it to a string for storage.
	 * <ul>
	 * <li>> N: Name</li>
	 * <li>> L: Lore</li>
	 * <li>A: Leather Armour</li>
	 * <li>P: Potion (R: Effects)</li>
	 * <li>E: Enchantment Storage Meta</li>
	 * <li>F: Fireworks</li>
	 * <li>B: Banner</li>
	 * <li>M: MapMeta</li>
	 * <li>S: Shulkerbox</li>
	 * </ul>
	 * "=" is used to separate traits.
	 * @param iStack
	 * @return
	 */
	public static String compressMetaData(ItemStack iStack) {
		ItemMeta iMeta = iStack.getItemMeta();
		StringBuilder sBuilder = new StringBuilder();
		
		if(iMeta.hasDisplayName())
			sBuilder.append("N" + iMeta.getDisplayName() + "=");
		if(iMeta.hasLore())
			sBuilder.append("L" + iMeta.getLore()
				.stream()
				.collect(Collectors.joining("-"))
				+ "=");
		
		// Deal with leather armour: Save it's colour. (A+colour)
		if(iMeta instanceof LeatherArmorMeta)
			sBuilder.append("A" + formatColour(((LeatherArmorMeta) iMeta).getColor()) + "=");
		// Deal with potions. Save it's effects.
		else if(iMeta instanceof PotionMeta) {
			// If potion has custom effects. (R+name+amplifier+duration)
			if(((PotionMeta) iMeta).hasCustomEffects())
				((PotionMeta) iMeta).getCustomEffects()
					.parallelStream()
					.forEach(effects -> {
						sBuilder.append("R" + effects.getType().getName()
								+ "+" + effects.getAmplifier()
								+ "+" + effects.getDuration()
								+ "=");
					});
			// Get potion's base data (P+name+extended+upgraded)
			if(((PotionMeta) iMeta).getBasePotionData() != null) {
				PotionData pData = ((PotionMeta) iMeta).getBasePotionData();
				sBuilder.append("P" + pData.getType().name()
						+ "+" + pData.isExtended()
						+ "+" + pData.isUpgraded() + "=");
			}
		}
		// Deal with enchanted books. (E+enchantmentCompressedString)
		else if(iMeta instanceof EnchantmentStorageMeta) {
			sBuilder.append("E" + compressEnchantments(
							((EnchantmentStorageMeta) iMeta).getStoredEnchants()));
		}
		// Deal with fireworks.
		// As there can be a list of effects, add a separater (~) between the effects and the rest of the data.
		// (F + effects(flicker+trail+colours+fadeColours+type) + ~ + power)
		else if(iMeta instanceof FireworkMeta) {
			if(((FireworkMeta) iMeta).hasEffects())
				sBuilder.append("F");
						((FireworkMeta) iMeta).getEffects()
							.forEach(effect -> {
								sBuilder.append(effect.hasFlicker() + "+");
								sBuilder.append(effect.hasTrail() + "+");
								sBuilder.append(effect.getColors()
										.stream()
										.map(MetaCompress::formatColour)
										.collect(Collectors.joining("~"))
										+ "+");
								sBuilder.append(effect.getFadeColors()
										.stream()
										.map(MetaCompress::formatColour)
										.collect(Collectors.joining("~"))
										+ "+");
								sBuilder.append(effect.getType().name() + "=");
							});
		}
		// Deal with banners.
		else if(iMeta instanceof BannerMeta) {
			BannerMeta bMeta = (BannerMeta) iMeta;
			sBuilder.append("B");
			bMeta.getPatterns()
				.forEach(pattern -> {
					Color colour = pattern.getColor().getColor();
					sBuilder.append(formatColour(colour) + "+");
					sBuilder.append(pattern.getPattern().name() + "~");
			});
			
			sBuilder.append("=");
		}
		// Deal with maps. (M+colour+~+location+~+scaling)
		else if(iMeta instanceof MapMeta) {
			MapMeta mMeta = (MapMeta) iMeta;
			sBuilder.append("M");
			sBuilder.append(formatColour(mMeta.getColor()) + "+");
			sBuilder.append(mMeta.getLocationName() + "+");
			sBuilder.append(mMeta.isScaling() + "+");
			sBuilder.append("=");
		} 
		
		return sBuilder.toString();
		
	}
	
	private static String formatColour(Color colour) {
		return String.format("%s.%s.%s",
				colour.getRed(),
				colour.getGreen(),
				colour.getBlue());
	}

}
