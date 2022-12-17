package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.item.ModItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlocksColors {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
        event.register((blockState, blockAndTintGetter, pos, tintIndex) ->{
            assert blockAndTintGetter != null;
            assert pos != null;

            if(blockAndTintGetter.getBlockEntity(pos) instanceof BrewingCauldronBlockEntity brewingCauldron){
                return brewingCauldron.getWaterColor();
            }
            return 3694022;
        }, ModBlock.BREWING_CAULDRON_BLOCK);

    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtils.getColor(PotionUtils.getCustomEffects(stack)), ModItem.COMBINED_POTION);
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtils.getColor(PotionUtils.getCustomEffects(stack)), ModItem.SPLASH_COMBINED_POTION);
        event.register((stack, tintIndex) -> tintIndex == 1 ? 0xFFFFFFFF : PotionUtils.getColor(PotionUtils.getCustomEffects(stack)), ModItem.COMBINED_LINGERING_POTION);
    }
}
