package mod.motivationaldragon.potionblender.compabibilitylayer;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class PlatformSpecific {

    public static PlatformSpecific INSTANCE;

    private final BlockEntityType<? extends BrewingCauldronBlockEntity> brewingCauldron;

    private final BiFunction<BlockPos, BlockState, BlockEntity> brewingCauldronConstructor;

    public abstract boolean isFabric();
    public abstract boolean isForge();

    protected PlatformSpecific(BlockEntityType<? extends BrewingCauldronBlockEntity> brewingCauldron, BiFunction<BlockPos, BlockState, BlockEntity> brewingCauldronConstructor) {
        this.brewingCauldron = brewingCauldron;
        this.brewingCauldronConstructor = brewingCauldronConstructor;
    }

    public BlockEntity createBrewingCauldronBlockEntity(BlockPos pos, @NotNull BlockState state){
        return brewingCauldronConstructor.apply(pos,state);
    }

    public BlockEntityType<? extends BrewingCauldronBlockEntity> getBrewingCauldron() {
        return brewingCauldron;
    }



}
