package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricBlockEntities {

    public static final BlockEntityType<FabricBrewingCauldronBlockEntity> BREWING_CAULDRON_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Constants.MOD_ID, "potion_mixer"),
            FabricBlockEntityTypeBuilder.create(FabricBrewingCauldronBlockEntity::new,
                    PotionBlenderBlock.BREWING_CAULDRON_BLOCK).build());

    /**
     * Ensure constant initialisation during mod initialisation
     */
    public static void init(){
        Constants.LOG.debug("BlockEntities loaded!");
    }

}
