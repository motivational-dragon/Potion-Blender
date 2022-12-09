package mod.motivationaldragon.potionblender.block;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.BiConsumer;

public class ModBlock {
    ModBlock(){throw new IllegalStateException("Utility class");}
    public static final Block BREWING_CAULDRON_BLOCK = new BrewingCauldron(
            BlockBehaviour.Properties.of(Material.METAL, MaterialColor.STONE).strength(2f).requiresCorrectToolForDrops()
                    .noOcclusion().lightLevel(x->15));


    public static void registerBlock(BiConsumer<Block, ResourceLocation> r){
        r.accept(BREWING_CAULDRON_BLOCK, new ResourceLocation(Constants.MOD_ID, "brewing_cauldron"));
    }

    public static void registerBlockItem(BiConsumer<Item, ResourceLocation> r){

        r.accept(new BlockItem(BREWING_CAULDRON_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_BREWING)),
                Registry.BLOCK.getKey(BREWING_CAULDRON_BLOCK));
        Constants.LOG.debug("Registered all block");
    }


}
