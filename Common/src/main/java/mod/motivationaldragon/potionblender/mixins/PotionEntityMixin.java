package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.PotionBlenderCommon;
import mod.motivationaldragon.potionblender.utils.PotionType;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin {

    /**
     * Make the is lingering method return true for potion marked as combined lingering potion
     */
    @Inject(method = "isLingering", at = @At("RETURN"), cancellable = true)
    private void isLingering(CallbackInfoReturnable<Boolean> cir){
        Integer potionTypeCode = ((ThrownPotion) (Object) this).getItem().get(PotionBlenderCommon.potionTypeData);
        if (potionTypeCode != null) {
                cir.setReturnValue(potionTypeCode == PotionType.LINGERING.code);
            }
        }
    }

