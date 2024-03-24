package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;


@Environment(EnvType.CLIENT)
public class ModColorProvider {

    ModColorProvider(){
        throw new IllegalStateException("Utility class");
    }
    public static void registerColorProvider(){

        //Cauldron water color
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->{
            assert view != null;
                Object data =  view.getBlockEntityRenderData(pos);

                if(data != null){return (int) data;}
            return 3694022; // mc color code for water
            }, PotionBlenderBlock.BREWING_CAULDRON_BLOCK);

    }
}
