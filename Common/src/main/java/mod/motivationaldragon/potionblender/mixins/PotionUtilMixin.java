package mod.motivationaldragon.potionblender.mixins;


import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionUtils.class)
public abstract class PotionUtilMixin {

    //Color a normal tipped arrow if used as a combined arrow
    @Inject(method = "getColor*", at = @At("RETURN"), cancellable = true)
    private static int getColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        CompoundTag nbtCompound = stack.getTag();
        if (nbtCompound != null && nbtCompound.contains(ModNBTKey.FORCE_COLOR_RENDERING_KEY) && nbtCompound.getBoolean(ModNBTKey.FORCE_COLOR_RENDERING_KEY)) {
            cir.setReturnValue(PotionUtils.getColor(PotionUtils.getMobEffects(stack)));
        }
        return cir.getReturnValue();
    }
}
