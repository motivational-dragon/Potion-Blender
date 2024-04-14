package mod.motivationaldragon.potionblender.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.recipes.BrewingCauldronRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIPotionBlender implements IModPlugin {

	public JEIPotionBlender() {
		Constants.LOG.info("JEI Plugin Loaded");
	}

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_jei_plugin");
	}
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new BrewingCauldronJeiCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		List<BrewingCauldronRecipe> recipes = recipeManager.getAllRecipesFor(BrewingCauldronRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).toList();
		registration.addRecipes(BrewingCauldronJeiCategory.brewing_cauldron_recipe_type, recipes);
	}

}

