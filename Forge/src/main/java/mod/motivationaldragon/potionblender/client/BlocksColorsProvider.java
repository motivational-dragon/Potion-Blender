package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlocksColorsProvider {
    @SubscribeEvent
    public static void registerCauldronColor(RegisterColorHandlersEvent.Block event){
        event.register((blockState, blockAndTintGetter, pos, tintIndex) ->{
            if(blockAndTintGetter != null && pos !=null) {
                var cauldron = (BrewingCauldronBlockEntity)blockAndTintGetter.getBlockEntity(pos);
                if(cauldron != null) {
                    return cauldron.getWaterColor();
                }
            }
            return 3694022;
        }, PotionBlenderBlock.BREWING_CAULDRON_BLOCK);

    }

}
