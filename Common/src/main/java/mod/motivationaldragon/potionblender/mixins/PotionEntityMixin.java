package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin {

    /**
     * Make the is lingering method return true for combined lingering potion
     */
    @Inject(method = "isLingering", at = @At("RETURN"), cancellable = true)
    private void isLingering(CallbackInfoReturnable<Boolean> cir){
        boolean isCombinedPotion = ((ThrownPotion) (Object) this).getItem().getOrCreateTag().getBoolean(ModNBTKey.IS_COMBINED_LINGERING_POTION);
        if(isCombinedPotion) {
            cir.setReturnValue(true);
        }
    }

}
