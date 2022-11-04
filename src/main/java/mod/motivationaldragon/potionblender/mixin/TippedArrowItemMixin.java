package mod.motivationaldragon.potionblender.mixin;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {

    @Inject(method = "getTranslationKey", at = @At("RETURN"), cancellable = true)
    private void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir){
            if (stack.hasNbt()
                    && stack.getNbt().contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)
                    && stack.getNbt().getBoolean(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)) {
                cir.setReturnValue("Combined Arrow");
            }
    }

}
