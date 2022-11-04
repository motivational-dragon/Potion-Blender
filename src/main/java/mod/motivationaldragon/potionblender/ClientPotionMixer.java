package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.client.ModColorProvider;
import mod.motivationaldragon.potionblender.networking.ModNetworkRegisterer;
import net.fabricmc.api.ClientModInitializer;

public class ClientPotionMixer implements ClientModInitializer {




    @Override
    public void onInitializeClient() {
        ModColorProvider.registerColorProvider();
        ModNetworkRegisterer.registerS2CPackets();
    }
}
