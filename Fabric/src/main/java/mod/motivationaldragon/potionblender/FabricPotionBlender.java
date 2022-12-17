package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentity.FabricBlockEntities;
import mod.motivationaldragon.potionblender.platform.FabricPlatformHelper;
import mod.motivationaldragon.potionblender.platform.service.PlatformSpecificHelper;
import mod.motivationaldragon.potionblender.platform.Service;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.ModSpecialRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;


public class FabricPotionBlender implements ModInitializer {

	@Override
	public void onInitialize() {
		PotionBlenderCommon.init();

		ModItem.register(bind(Registry.ITEM));
		ModBlock.registerBlock(bind(Registry.BLOCK));
		ModBlock.registerBlockItem(bind(Registry.ITEM));
		ModSpecialRecipeSerializer.register(bind(Registry.RECIPE_SERIALIZER));
		FabricBlockEntities.init();

	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}
