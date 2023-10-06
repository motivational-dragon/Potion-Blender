package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LingeringPotionItem.class)


public abstract class LingeringPotionMixin {
	@Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
	public void appendHoverText(ItemStack itemStack, Level $$1, List<Component> $$2, TooltipFlag $$3, CallbackInfo ci) {
		boolean isCombinedPotion = itemStack.getOrCreateTag().getBoolean(ModNBTKey.IS_COMBINED_LINGERING_POTION);
		if(isCombinedPotion){
			PotionUtils.addPotionTooltip(itemStack, $$2, 1F);
			ci.cancel();
			return;
		}
	}
}
