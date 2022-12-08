package mod.motivationaldragon.potionblender.mixin;


import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionUtils.class)
public abstract class PotionUtilMixin {

    @Inject(method = "getColor*", at = @At("RETURN"), cancellable = true)
    private static int getColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        CompoundTag nbtCompound = stack.getTag();
        if (nbtCompound != null && nbtCompound.contains(ModNBTKey.FORCE_COLOR_RENDERING_KEY) && nbtCompound.getBoolean(ModNBTKey.FORCE_COLOR_RENDERING_KEY)) {
            cir.setReturnValue(PotionUtils.getColor(PotionUtils.getCustomEffects(stack)));
        }
        return cir.getReturnValue();
    }
}
