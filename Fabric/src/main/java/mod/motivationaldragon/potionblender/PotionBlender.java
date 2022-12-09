package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.block.blockentities.ModBlockEntities;
import mod.motivationaldragon.potionblender.compabibilitylayer.PlatformSpecific;
import mod.motivationaldragon.potionblender.compatibilitylayer.FabricPlatform;
import mod.motivationaldragon.potionblender.config.ModConfig;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.ModSpecialRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;


public class PotionBlender implements ModInitializer {

	@Override
	public void onInitialize() {

		PlatformSpecific.INSTANCE = new FabricPlatform();

		ModConfig.initConfig();
		ModItem.register(bind(Registry.ITEM));
		ModBlockEntities.registerBlockEntity(bind(Registry.BLOCK_ENTITY_TYPE));
		ModBlock.registerBlock(bind(Registry.BLOCK));
		ModBlock.registerBlockItem(bind(Registry.ITEM));
		ModSpecialRecipeSerializer.register(bind(Registry.RECIPE_SERIALIZER));

	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}
