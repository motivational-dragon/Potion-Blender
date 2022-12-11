package mod.motivationaldragon.potionblender.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;


public class ModNetworkRegisterer {

    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(BrewingCauldronInvSyncS2CPacket.fabricChannel,
                (Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
                 PacketSender sender) -> BrewingCauldronInvSyncS2CPacket.handle(friendlyByteBuf));
    }

}
