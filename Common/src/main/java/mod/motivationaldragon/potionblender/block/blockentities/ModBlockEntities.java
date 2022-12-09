package mod.motivationaldragon.potionblender.block.blockentities;


import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.compabibilitylayer.PlatformSpecific;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ModBlockEntities {

    private static final Map<ResourceLocation, BlockEntityType<?>> ressourceToBlockEntity = new HashMap<>();

    public static final BlockEntityType<BrewingCauldronBlockEntity> BREWING_CAULDRON_BLOCK_ENTITY =createBlockEntityType(
            new ResourceLocation(Constants.MOD_ID, "potion_mixer"), BrewingCauldronBlockEntity::new, ModBlock.BREWING_CAULDRON_BLOCK);

    public static <T extends BlockEntity> BlockEntityType<T>
    createBlockEntityType(ResourceLocation id, BiFunction<BlockPos, BlockState, T> blockEntityConstructor, Block... blocks){

        if (ressourceToBlockEntity.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }

        BlockEntityType<T> blockEntityType = PlatformSpecific.INSTANCE.createBlockEntityType(blockEntityConstructor, blocks);

        ressourceToBlockEntity.put(id, blockEntityType);
        return blockEntityType;
    }

    public static void registerBlockEntity(BiConsumer<BlockEntityType<?>, ResourceLocation> registry) {
        for (var entry : ressourceToBlockEntity.entrySet()) {
            registry.accept(entry.getValue(), entry.getKey());
        }
    }

//    FabricBlockEntityTypeBuilder.create(BrewingCauldronBlockEntity::new,
//    ModBlock.BREWING_CAULDRON_BLOCK).build()
}
