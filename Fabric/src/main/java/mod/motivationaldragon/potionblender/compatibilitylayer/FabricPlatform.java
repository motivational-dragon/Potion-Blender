package mod.motivationaldragon.potionblender.compatibilitylayer;

import mod.motivationaldragon.potionblender.compabibilitylayer.PlatformSpecific;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class FabricPlatform extends PlatformSpecific {

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public boolean isForge() {
        return false;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> blockEntityConstructor, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(blockEntityConstructor::apply, blocks).build();
    }
}
