package mod.motivationaldragon.potionblender.compatibilitylayer;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.compabibilitylayer.PlatformSpecific;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class FabricPlatform extends PlatformSpecific {

    public FabricPlatform(BlockEntityType<? extends BrewingCauldronBlockEntity> brewingCauldron, BiFunction<BlockPos, BlockState, BlockEntity> brewingCauldronConstructor) {
        super(brewingCauldron, brewingCauldronConstructor);
    }

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public boolean isForge() {
        return false;
    }

}
