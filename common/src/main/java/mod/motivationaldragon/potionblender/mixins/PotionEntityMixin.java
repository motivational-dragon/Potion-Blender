package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.datatype.PotionBlender;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
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
        ItemStack itemStack = ((ThrownPotion) (Object) this).getItem();
        boolean hasCombinedPotionData = itemStack.has(PotionBlender.potionTypeData);
        if (hasCombinedPotionData) {
                cir.setReturnValue(ModUtils.isCombinedLingeringPotion(itemStack));
            }
        }
    }

