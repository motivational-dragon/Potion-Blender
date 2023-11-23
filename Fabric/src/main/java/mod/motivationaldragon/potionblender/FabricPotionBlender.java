package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentity.FabricBlockEntities;
import mod.motivationaldragon.potionblender.event.OnUseBlockFabric;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.BiConsumer;


public class FabricPotionBlender implements ModInitializer {

	@Override
	public void onInitialize() {
		PotionBlenderCommon.init();

		PotionBlenderBlock.registerBlock(bind(BuiltInRegistries.BLOCK));
		PotionBlenderBlock.registerBlockItem(bind(BuiltInRegistries.ITEM));
		PotionBlenderRecipe.registerRecipeSerializer(bind(BuiltInRegistries.RECIPE_SERIALIZER));
		PotionBlenderRecipe.registerRecipeType(bind(BuiltInRegistries.RECIPE_TYPE));

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> ModItem.registerFunctionalBlocksItems(entries::accept));

		FabricBlockEntities.init();
		OnUseBlockFabric.registerHandler();
	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}
