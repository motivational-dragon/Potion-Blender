package mod.motivationaldragon.potionblender.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Function;


public class ModNetworkRegisterer {

    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(BrewingCauldronInvSyncS2CPacket.fabricChannel,
                makeClientHandler(BrewingCauldronInvSyncS2CPacket::decode,BrewingCauldronInvSyncS2CPacket::handle));
    }

    private static <T> ClientPlayNetworking.PlayChannelHandler makeClientHandler(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler){
        return (_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf));
    }

}
