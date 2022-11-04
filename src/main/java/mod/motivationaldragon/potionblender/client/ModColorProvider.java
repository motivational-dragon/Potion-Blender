package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.item.ModItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.potion.PotionUtil;


@Environment(EnvType.CLIENT)
public class ModColorProvider {

    ModColorProvider(){
        throw new IllegalStateException("Utility class");
    }
    public static void registerColorProvider(){

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->{
            assert view != null;
            if(view instanceof RenderAttachedBlockView renderAttachedBlockView){
                Object data =  renderAttachedBlockView.getBlockEntityRenderAttachment(pos);

                if(data != null){
                    return (int) data;
                }
            }

            return 3694022;

            }, ModBlock.BREWING_CAULDRON_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtil.getColor(PotionUtil.getCustomPotionEffects(stack)), ModItem.COMBINED_POTION);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtil.getColor(PotionUtil.getCustomPotionEffects(stack)), ModItem.SPLASH_COMBINED_POTION);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtil.getColor(PotionUtil.getCustomPotionEffects(stack)), ModItem.COMBINED_LINGERING_POTION);

    }
}
