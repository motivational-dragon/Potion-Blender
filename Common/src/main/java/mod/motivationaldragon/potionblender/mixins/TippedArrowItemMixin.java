package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendHoverText(ItemStack stack, Level $$1, List<Component> $$2, TooltipFlag $$3, CallbackInfo ci){
        if(isCombinedArrow(stack)){
            PotionUtils.addPotionTooltip(stack, $$2, 1);
            ci.cancel();
        }
    }

    @Inject(method = "getDescriptionId", at = @At("RETURN"), cancellable = true)
    private void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir){
        if(isCombinedArrow(stack)) {
            cir.setReturnValue("Combined Arrow");
        }
    }


    @Unique
    private boolean isCombinedArrow(ItemStack stack){
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            return stack.getTag().contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY);
        }
        return false;
    }

}
