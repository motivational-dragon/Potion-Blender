package mod.motivationaldragon.potionblender.mixin;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {

    @Inject(method = "getDescriptionId", at = @At("RETURN"), cancellable = true)
    private void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir){
            if (stack.hasTag()
                    && stack.getTag().contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)
                    && stack.getTag().getBoolean(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)) {
                cir.setReturnValue("Combined Arrow");
            }
    }

}
