package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.networking.BrewingCauldronInvSyncS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FabricBrewingCauldronBlockEntity extends BrewingCauldronBlockEntity implements RenderAttachmentBlockEntity {

    public FabricBrewingCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void syncInventoryWithClient() {

        assert this.getLevel() != null;

        if(this.getLevel().isClientSide()) {return;}

        FriendlyByteBuf data = PacketByteBufs.create();
        data.writeInt(this.size());
        for (ItemStack stack : this.getInventory()) {
            data.writeItem(stack);
        }
        data.writeBlockPos(getBlockPos());

        for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) this.getLevel(), this.getBlockPos())) {
            ServerPlayNetworking.send(player, BrewingCauldronInvSyncS2CPacket.fabricChannel, data);
        }
    }

    /**
     * Fabric specific code used to by the render thread to get block entity data for rendering
     * @return An Integer representing the water color of the cauldron
     */
    public @Nullable Object getRenderAttachmentData() {
        return PotionUtils.getColor(getInventoryStatusEffectsInstances());
    }

//    //TODO: removing this will probably make the cauldron color desync when reloading a world
//    @Override
//    public CompoundTag toInitialChunkDataNbt() {
//        CompoundTag CompoundTag = new CompoundTag();
//        Inventories.writeNbt(CompoundTag, this.inventory, true);
//        CompoundTag.putInt(POTION_MIXER_KEY, numberOfPotion);
//        return CompoundTag;
//    }



}
