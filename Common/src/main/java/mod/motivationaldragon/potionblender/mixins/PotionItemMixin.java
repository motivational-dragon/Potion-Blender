package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mod.motivationaldragon.potionblender.Constants.*;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {




    /**
     * Mixin used to override potion vanilla "Uncraftable potion" name with combined potion name
     * @param stack the item stack
     * @param cir callback info
     */
    @Inject(method = "getDescriptionId*", at = @At("RETURN"), cancellable = true)
    private void getDescriptionId(ItemStack stack, CallbackInfoReturnable<String> cir){
        CompoundTag nbtCompound = stack.getTag();
        if(nbtCompound != null) {
            if (ModUtils.isTagValueTrue(nbtCompound, ModNBTKey.IS_COMBINED_POTION)) {
                cir.setReturnValue(COMBINED_POTION_NAME);
            } else if (ModUtils.isTagValueTrue(nbtCompound, ModNBTKey.IS_COMBINED_SPLASH_POTION)) {
                cir.setReturnValue(COMBINED_SPLASH_POTION_NAME);
            } else if (ModUtils.isTagValueTrue(nbtCompound, ModNBTKey.IS_COMBINED_LINGERING_POTION)) {
                cir.setReturnValue(COMBINED_LINGERING_POTION);
            }
        }
    }

}
