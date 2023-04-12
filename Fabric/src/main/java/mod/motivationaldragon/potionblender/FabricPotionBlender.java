package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentity.FabricBlockEntities;
import mod.motivationaldragon.potionblender.event.OnUseBlockFabric;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderSpecialRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;


public class FabricPotionBlender implements ModInitializer {

	@Override
	public void onInitialize() {
		PotionBlenderCommon.init();

		ModItem.register(bind(Registry.ITEM));
		PotionBlenderBlock.registerBlock(bind(Registry.BLOCK));
		PotionBlenderBlock.registerBlockItem(bind(Registry.ITEM));
		PotionBlenderSpecialRecipeSerializer.register(bind(Registry.RECIPE_SERIALIZER));
		FabricBlockEntities.init();
		OnUseBlockFabric.registerHandler();
	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}
