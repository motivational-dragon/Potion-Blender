package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.PotionType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mod.motivationaldragon.potionblender.Constants.*;
import static mod.motivationaldragon.potionblender.PotionBlenderCommon.potionTypeData;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {




    /**
     * Mixin used to override potion vanilla "Uncraftable potion" name with combined potion name
     * @param stack the item stack
     * @param cir callback info
     */
    @Inject(method = "getDescriptionId*", at = @At("RETURN"), cancellable = true)
    private void getDescriptionId(ItemStack stack, CallbackInfoReturnable<String> cir){
        PotionType type = PotionType.codeToPotionType.get(stack.get(potionTypeData));
        if(type == null){return;}
        switch (type){
            case NORMAL -> cir.setReturnValue(COMBINED_POTION_NAME);
            case SPLASH -> cir.setReturnValue(COMBINED_SPLASH_POTION_NAME);
            case LINGERING -> cir.setReturnValue(COMBINED_LINGERING_POTION);
        }
    }

}
