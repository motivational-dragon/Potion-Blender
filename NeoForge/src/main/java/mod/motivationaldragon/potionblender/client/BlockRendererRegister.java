package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRendererRegister {

	@SubscribeEvent
	public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		PotionBlenderRenderer.register(event::registerBlockEntityRenderer);
	}
}
