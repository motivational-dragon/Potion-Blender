package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(LingeringPotionItem.class)
public abstract class LingeringPotionMixin {
	@Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag, CallbackInfo ci) {
		boolean isCombinedPotion = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(ModNBTKey.IS_COMBINED_LINGERING_POTION);
		if(isCombinedPotion){
			//Since the potion duration is precalculated, there is to reduce it here
			PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			Objects.requireNonNull(components);
			potionContents.addPotionTooltip(components::add, 1F, tooltipContext.tickRate());
			ci.cancel();
		}
	}
}
