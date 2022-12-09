package mod.motivationaldragon.potionblender.networking;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import mod.motivationaldragon.potionblender.block.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

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
