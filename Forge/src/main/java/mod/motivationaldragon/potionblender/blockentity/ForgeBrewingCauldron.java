package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.networking.BrewingCauldronInvSyncS2CPacket;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;

public class ForgeBrewingCauldron extends BrewingCauldronBlockEntity {

    public ForgeBrewingCauldron(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void syncInventoryWithClient() {
        NetworkRegister.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(()-> Objects.requireNonNull(level).getChunkAt(worldPosition)
        ), new BrewingCauldronInvSyncS2CPacket(this.getInventory(), this.getBlockPos()));
    }
}
