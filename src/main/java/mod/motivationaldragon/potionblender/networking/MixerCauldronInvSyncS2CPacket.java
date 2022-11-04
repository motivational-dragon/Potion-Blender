package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.block.BrewingCauldronBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class MixerCauldronInvSyncS2CPacket {

    private MixerCauldronInvSyncS2CPacket() {
        throw new IllegalStateException("utility class!");
    }

    @Environment(EnvType.CLIENT)
    public static void execute(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int size = buf.readInt();
        DefaultedList<ItemStack> list = DefaultedList.ofSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            list.set(i, buf.readItemStack());
        }
        BlockPos position = buf.readBlockPos();

        if (client.world == null) return;
        if(client.world.getBlockEntity(position) instanceof BrewingCauldronBlockEntity blockEntity) {
            blockEntity.setInventory(list);
        }
    }
}
