package mod.motivationaldragon.potionblender.mixin;

import mod.motivationaldragon.potionblender.item.ModItem;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin {

    /**
     * Make the is lingering method return true for combined lingering potion
     */
    @Inject(method = "isLingering", at = @At("RETURN"), cancellable = true)
    private void isLingering(CallbackInfoReturnable<Boolean> cir){
        boolean res = ((PotionEntity) (Object) this).getStack().isOf(ModItem.COMBINED_LINGERING_POTION);
        cir.setReturnValue(res);
    }

}
