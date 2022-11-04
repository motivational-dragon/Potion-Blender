package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;


public class ModNetworkRegisterer {
    public static final Identifier BREWING_CAULDRON_INV_SYNC = new Identifier(PotionBlender.MODID, "brewing_cauldron_inv_sync");

    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(BREWING_CAULDRON_INV_SYNC, MixerCauldronInvSyncS2CPacket::execute);
    }








}
