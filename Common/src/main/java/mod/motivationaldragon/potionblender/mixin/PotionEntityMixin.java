package mod.motivationaldragon.potionblender.mixin;

import mod.motivationaldragon.potionblender.item.ModItem;
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
        boolean res = ((ThrownPotion) (Object) this).getItem().is(ModItem.COMBINED_LINGERING_POTION);
        cir.setReturnValue(res);
    }

}
