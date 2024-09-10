package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.networking.BrewingCauldronInvSyncS2CPacket;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricBrewingCauldronBlockEntity extends BrewingCauldronBlockEntity implements RenderDataBlockEntity {

    public FabricBrewingCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void syncInventoryWithClient() {

        assert this.getLevel() != null;

        if(this.getLevel().isClientSide()) {return;}

        for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) this.getLevel(), this.getBlockPos())) {
            ServerPlayNetworking.send(player, new BrewingCauldronInvSyncS2CPacket(this.getInventory(),getBlockPos()));
        }
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
