package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.utils.PotionType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static mod.motivationaldragon.potionblender.PotionBlenderCommon.potionTypeData;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
   @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag, CallbackInfo ci){
        if(potion_Blender$isCombinedArrow(stack)){
            if(stack.has(DataComponents.POTION_CONTENTS)){
                PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
                potionContents.addPotionTooltip(components::add, 1F, context.tickRate());
                ci.cancel();
            }
        }
    }

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
