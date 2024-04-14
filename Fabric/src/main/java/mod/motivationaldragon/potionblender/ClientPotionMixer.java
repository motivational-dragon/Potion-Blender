package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.client.PotionBlenderRenderer;
import mod.motivationaldragon.potionblender.networking.ModNetworkRegisterer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Environment(EnvType.CLIENT)
public class ClientPotionMixer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModNetworkRegisterer.registerS2CPackets();
        PotionBlenderRenderer.register(BlockEntityRenderers::register);
    }
}
