package mod.motivationaldragon.potionblender.block;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<BrewingCauldronBlockEntity> BREWING_CAULDRON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
            new Identifier(PotionBlender.MODID, "potion_mixer"),
            FabricBlockEntityTypeBuilder.create(BrewingCauldronBlockEntity::new,
                    ModBlock.BREWING_CAULDRON_BLOCK).build());


    public static void registerBlockEntity(){
        PotionBlender.LOGGER.debug("Loaded block entity");
    }
}
