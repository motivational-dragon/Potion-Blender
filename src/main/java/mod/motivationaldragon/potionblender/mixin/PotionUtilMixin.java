package mod.motivationaldragon.potionblender.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mod.motivationaldragon.potionblender.utils.ModNBTKey.FORCE_COLOR_RENDERING_KEY;

@Mixin(PotionUtil.class)
public abstract class PotionUtilMixin {


    @Inject(method = "getColor*", at = @At("RETURN"), cancellable = true)
    private static int getColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains(FORCE_COLOR_RENDERING_KEY) && nbtCompound.getBoolean(FORCE_COLOR_RENDERING_KEY)) {
            cir.setReturnValue(PotionUtil.getColor(PotionUtil.getPotionEffects(stack)));
        }
        return cir.getReturnValue();
    }
}
