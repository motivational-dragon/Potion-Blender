package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.item.ModItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlocksColorsProvider {
    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event){
        event.getBlockColors().register((blockState, blockAndTintGetter, pos, tintIndex) ->{
            assert blockAndTintGetter != null;
            assert pos != null;

            if(blockAndTintGetter.getBlockEntity(pos) instanceof BrewingCauldronBlockEntity brewingCauldron){
                return brewingCauldron.getWaterColor();
            }
            return 3694022;
        }, ModBlock.BREWING_CAULDRON_BLOCK);

    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event){
        event.getItemColors().register(CommonItemColors::handlePotionColor, ModItem.COMBINED_POTION);
        event.getItemColors().register(CommonItemColors::handlePotionColor, ModItem.SPLASH_COMBINED_POTION);
        event.getItemColors().register(CommonItemColors::handlePotionColor, ModItem.COMBINED_LINGERING_POTION);
    }
}
