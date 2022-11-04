package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSpecialRecipeSerializer {

    public static SpecialRecipeSerializer<CombinedTippedArrowRecipe> COMBINED_TIPPED_ARROW;



    public static void register(){
        COMBINED_TIPPED_ARROW = Registry.register(Registry.RECIPE_SERIALIZER,new Identifier(PotionBlender.MODID,"tipped_combined_arrow"),
                new SpecialRecipeSerializer<>(CombinedTippedArrowRecipe::new));
        PotionBlender.LOGGER.debug("Loaded recipe");
    }


}
