package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.block.ModBlockEntities;
import mod.motivationaldragon.potionblender.config.ModConfig;
import mod.motivationaldragon.potionblender.eventlistener.UseBlockCallBackRegisterer;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.ModSpecialRecipeSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PotionBlender implements ModInitializer {

	public static final String MODID = "potionblender";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


	@Override
	public void onInitialize() {

		ModConfig.initConfig();
		ModItem.registerAll();
		ModBlockEntities.registerBlockEntity();
		ModBlock.registerAll();
		UseBlockCallBackRegisterer.registerListener();
		ModSpecialRecipeSerializer.register();


	}
}
