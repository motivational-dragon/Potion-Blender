package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.networking.BrewingCauldronInvSyncS2CPacket;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static net.minecraftforge.network.PacketDistributor.TRACKING_CHUNK;

public class ForgeBrewingCauldron extends BrewingCauldronBlockEntity {

    public ForgeBrewingCauldron(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void syncInventoryWithClient() {
        NetworkRegister.INSTANCE.send(new BrewingCauldronInvSyncS2CPacket(this.getInventory(), this.getBlockPos()),
                TRACKING_CHUNK.with(Objects.requireNonNull(level).getChunkAt(worldPosition)));
    }
}
