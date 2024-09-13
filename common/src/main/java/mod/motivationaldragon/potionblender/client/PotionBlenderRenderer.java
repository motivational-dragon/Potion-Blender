package mod.motivationaldragon.potionblender.client;

import mod.motivationaldragon.potionblender.platform.Service;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class PotionBlenderRenderer {

	//TODO: do not use raw types, but for now since there is only one renderer, it is fine
	public static  void register(BiConsumer<BlockEntityType, BlockEntityRendererProvider> r){
		r.accept(Service.PLATFORM.getPlatformBrewingCauldron(),BrewingCauldronRenderer::new);
	}

}
