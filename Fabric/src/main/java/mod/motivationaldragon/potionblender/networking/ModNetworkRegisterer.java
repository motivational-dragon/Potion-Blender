package mod.motivationaldragon.potionblender.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class ModNetworkRegisterer {



    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(BrewingCauldronInvSyncS2CPacket.packetType,(payload, context) -> {
            BrewingCauldronInvSyncS2CPacket.setCauldronInventory(payload);});
    }

}
