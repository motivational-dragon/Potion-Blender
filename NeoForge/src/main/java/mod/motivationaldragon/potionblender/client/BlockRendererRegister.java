package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRendererRegister {

	@SubscribeEvent
	public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		PotionBlenderRenderer.register(event::registerBlockEntityRenderer);
	}
}
