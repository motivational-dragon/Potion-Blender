package mod.motivationaldragon.potionblender.compabibilitylayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public abstract class PlatformSpecific {

    public static PlatformSpecific INSTANCE = null;

    public abstract boolean isFabric();
    public abstract boolean isForge();

    public abstract <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks);
}
