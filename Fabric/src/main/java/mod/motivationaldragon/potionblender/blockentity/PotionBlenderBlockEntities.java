package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.PotionBlender;
import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PotionBlenderBlockEntities {

    public static final BlockEntityType<FabricBrewingCauldronBlockEntity> BREWING_CAULDRON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Constants.MOD_ID, "potion_mixer"),
            FabricBlockEntityTypeBuilder.create(FabricBrewingCauldronBlockEntity::new,
                    ModBlock.BREWING_CAULDRON_BLOCK).build());

}
