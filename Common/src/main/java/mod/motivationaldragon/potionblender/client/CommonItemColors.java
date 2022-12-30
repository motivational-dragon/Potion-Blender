package mod.motivationaldragon.potionblender.client;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

public class CommonItemColors {
	public static Integer handlePotionColor(ItemStack stack, Integer tintIndex){
		return tintIndex == 1 ? 0xFFFFFFFF : PotionUtils.getColor(PotionUtils.getCustomEffects(stack));
	}
}
