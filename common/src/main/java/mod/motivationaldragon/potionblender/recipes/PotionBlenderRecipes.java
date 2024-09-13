package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.BiConsumer;

public class PotionBlenderRecipes {

    public static final SimpleCraftingRecipeSerializer<CombinedTippedArrowRecipe> COMBINED_TIPPED_ARROW_SERIALIZER = new SimpleCraftingRecipeSerializer<>(CombinedTippedArrowRecipe::new);


    public static void registerRecipeSerializer(BiConsumer<RecipeSerializer<?>, ResourceLocation> r){
        r.accept(COMBINED_TIPPED_ARROW_SERIALIZER,new ResourceLocation(Constants.MOD_ID,"tipped_combined_arrow"));
        r.accept(BrewingCauldronRecipe.CauldronRecipeSerializer.INSTANCE, new ResourceLocation(Constants.MOD_ID,"potion_blending"));
        Constants.LOG.debug("Loaded recipe");
    }

    public static void registerRecipeType(BiConsumer<RecipeType<?>, ResourceLocation> r){
        r.accept(BrewingCauldronRecipe.Type.INSTANCE, new ResourceLocation(Constants.MOD_ID,BrewingCauldronRecipe.Type.ID));
    }
}
