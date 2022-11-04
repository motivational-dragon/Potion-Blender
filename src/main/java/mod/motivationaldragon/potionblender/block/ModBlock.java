package mod.motivationaldragon.potionblender.block;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlock {
    ModBlock(){throw new IllegalStateException("Utility class");}
    public static final Block BREWING_CAULDRON_BLOCK = registerBlock("brewing_cauldron",new BrewingCauldron(
            FabricBlockSettings.of(Material.METAL).strength(2f).hardness(2f).requiresTool().nonOpaque().luminance(15)),
            ItemGroup.BREWING);


    private static Block registerBlock(String name, Block block, ItemGroup group){
        registerBlockItem(name,block, group);
        Registry.register(Registry.BLOCK, new Identifier(PotionBlender.MODID, name), block);
        return block;
    }

    private static void registerBlockItem(String name, Block block, ItemGroup group){
        Registry.register(Registry.ITEM, new Identifier(PotionBlender.MODID, name), new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerAll(){
        PotionBlender.LOGGER.debug("Registered all block");
    }

}
