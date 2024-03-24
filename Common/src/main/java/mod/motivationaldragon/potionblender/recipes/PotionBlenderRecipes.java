package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;


import java.util.function.BiConsumer;

public class PotionBlenderRecipes {

    public static final SimpleCraftingRecipeSerializer<CombinedTippedArrowRecipe> COMBINED_TIPPED_ARROW_RECIPE = new SimpleCraftingRecipeSerializer<>(CombinedTippedArrowRecipe::new);
    public static final BrewingCauldronRecipe.CauldronRecipeSerializer POTION_BLENDING = new BrewingCauldronRecipe.CauldronRecipeSerializer();

    public static final BrewingCauldronRecipe.Type POTION_BLENDING_RECIPE_TYPE = new BrewingCauldronRecipe.Type();


    public static void registerRecipeSerializer(BiConsumer<RecipeSerializer<?>, ResourceLocation> r){
        r.accept(COMBINED_TIPPED_ARROW_RECIPE,new ResourceLocation(Constants.MOD_ID,"tipped_combined_arrow"));
        r.accept(POTION_BLENDING, new ResourceLocation(Constants.MOD_ID,"potion_blending"));
        Constants.LOG.debug("Loaded recipe");
    }

    public static void registerRecipeType(BiConsumer<RecipeType<?>, ResourceLocation> r){
        r.accept(POTION_BLENDING_RECIPE_TYPE, new ResourceLocation(Constants.MOD_ID,"potion_blending"));
    }


}
