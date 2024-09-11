package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricBrewingCauldronBlockEntity extends BrewingCauldronBlockEntity implements RenderDataBlockEntity {

    public FabricBrewingCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * FabricPlatformHelper specific code used to by the render thread to get block entity data for rendering
     * @return An Integer representing the water color of the cauldron
     */
    @Override
    public @Nullable Object getRenderData () {
        return getWaterColor();
    }
}
