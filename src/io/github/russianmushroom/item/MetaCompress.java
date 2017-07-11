package io.github.russianmushroom.item;

import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

public class MetaCompress {


	/**
	 * Read item's meta data and compress it to a string for storage.
	 * <ul>
	 * <li>> N: Name</li>
	 * <li>> L: Lore</li>
	 * <li>A: Leather Armour</li>
	 * <li>P: Potion (R: Effects)</li>
	 * <li>E: Enchantment Storage Meta</li>
	 * <li>F: Fireworks</li>
	 * <li>K: Firework Effect</li>
	 * <li>B: Banner</li>
	 * <li>S: Shulkerbox</li>
	 * </ul>
	 * "=" is used to separate traits.
	 * @param iStack
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String compressMetaData(ItemStack iStack) {
		ItemMeta iMeta = iStack.getItemMeta();
		StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append("N" + iMeta.getDisplayName() + "=");
		if(iMeta.hasLore())
			sBuilder.append("L" + iMeta.getLore()
				.stream()
				.collect(Collectors.joining("-"))
				+ "=");
		
		// Deal with leather armour: Save it's colour. (A+colour)
		if(iMeta instanceof LeatherArmorMeta)
			sBuilder.append("A" + ((LeatherArmorMeta) iMeta).getColor().asRGB() + "=" );
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
		
		return sBuilder.toString();
		
	}
	
}
