package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.item.ModItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;


@Environment(EnvType.CLIENT)
public class ModColorProvider {

    ModColorProvider(){
        throw new IllegalStateException("Utility class");
    }
    public static void registerColorProvider(){

        //Cauldron water color
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->{
            assert view != null;
                Object data =  ((RenderAttachedBlockView)view).getBlockEntityRenderAttachment(pos);

                if(data != null){return (int) data;}
            return 3694022; // mc color code for water
            }, PotionBlenderBlock.BREWING_CAULDRON_BLOCK);

    }
}
