package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;


public class ModNetworkRegisterer {
    public static final ResourceLocation BREWING_CAULDRON_INV_SYNC = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_inv_sync");

//    @Environment(EnvType.CLIENT)
//    public static void registerS2CPackets(){
//        ClientPlayNetworking.registerGlobalReceiver(BREWING_CAULDRON_INV_SYNC, MixerCauldronInvSyncS2CPacket::execute);
//    }

}
