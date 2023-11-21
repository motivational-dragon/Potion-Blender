package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;


import java.util.function.BiConsumer;

public class PotionBlenderRecipeSerializer {

    public static final SimpleCraftingRecipeSerializer<CombinedTippedArrowRecipe> COMBINED_TIPPED_ARROW_RECIPE = new SimpleCraftingRecipeSerializer<>(CombinedTippedArrowRecipe::new);
    public static final CauldronRecipe.CauldronRecipeSerializer CAULDRON_RECIPE_SERIALIZER = new CauldronRecipe.CauldronRecipeSerializer();


    public static void register(BiConsumer<RecipeSerializer<?>, ResourceLocation> r){
        r.accept(COMBINED_TIPPED_ARROW_RECIPE,new ResourceLocation(Constants.MOD_ID,"tipped_combined_arrow_recipe"));
        r.accept(CAULDRON_RECIPE_SERIALIZER, new ResourceLocation(Constants.MOD_ID,"brewing_cauldron_recipe"));
        Constants.LOG.debug("Loaded recipe");
    }


}
