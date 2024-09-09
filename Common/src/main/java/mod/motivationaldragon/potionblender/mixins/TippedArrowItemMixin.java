package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.PotionType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mod.motivationaldragon.potionblender.PotionBlenderCommon.potionTypeData;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
    //TODO: update this
/*    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendHoverText(ItemStack stack, Item.TooltipContext $$1, List<Component> $$2, TooltipFlag $$3, CallbackInfo ci){
        if(potion_Blender$isCombinedArrow(stack)){
            PotionUtils.addPotionTooltip(stack, componentList, 1f, level == null ? 20.0F : level.tickRateManager().tickrate());
            ci.cancel();
        }
    }*/

    @Inject(method = "getDescriptionId", at = @At("RETURN"), cancellable = true)
    private void getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir){
        if(potion_Blender$isCombinedArrow(stack)) {
            cir.setReturnValue("Combined Arrow");
        }
    }


    @Unique
    private boolean potion_Blender$isCombinedArrow(ItemStack stack){
        PotionType type = PotionType.codeToPotionType.get(stack.get(potionTypeData));
        return type == PotionType.TIPPEDARROW;
    }

}
