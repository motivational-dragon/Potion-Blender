package mod.motivationaldragon.potionblender.platform.service;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class PlatformSpecificHelper {

    //Ugly way to specify brewing cauldron block entity implementation by making it a global variable rather than
    //providing a proper hook. Since there is only 1 block entity it is easier.
    private final BlockEntityType<? extends BrewingCauldronBlockEntity> brewingCauldron;

    private final BiFunction<BlockPos, BlockState, BlockEntity> brewingCauldronConstructor;

    public abstract boolean isFabric();
    public abstract boolean isForge();

    protected PlatformSpecificHelper(BlockEntityType<? extends BrewingCauldronBlockEntity> brewingCauldron, BiFunction<BlockPos, BlockState, BlockEntity> brewingCauldronConstructor) {
        this.brewingCauldron = brewingCauldron;
        this.brewingCauldronConstructor = brewingCauldronConstructor;
    }

    public BlockEntity createPlateformBrewingCauldronBlockEntity(BlockPos pos, @NotNull BlockState state){
        return brewingCauldronConstructor.apply(pos,state);
    }

    public BlockEntityType<? extends BrewingCauldronBlockEntity> getPlatformBrewingCauldron() {
        return brewingCauldron;
    }



}
