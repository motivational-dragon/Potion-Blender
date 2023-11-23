package mod.motivationaldragon.potionblender.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BrewingCauldronRecipe implements Recipe<Container> {

	private static final  ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_recipe");
	private final ItemStack output;
	private final NonNullList<Ingredient> ingredients;
	private final int brewingTime;

	public BrewingCauldronRecipe(ItemStack output, Integer brewingTime, NonNullList<Ingredient> ingredients) {
		this.output = output;
		this.ingredients = ingredients;
		this.brewingTime = brewingTime;
	}

	@Override
	public boolean matches(@NotNull Container var1, @NotNull Level var2) {
		return false;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull Container var1, @NotNull RegistryAccess var2) {
		return output.copy();
	}

	@Override
	public boolean canCraftInDimensions(int var1, int var2) {
		return true;
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess var1) {
		return output;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return PotionBlenderRecipe.CAULDRON_RECIPE_SERIALIZER;
	}


	public int getBrewingTime() {
		return brewingTime;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return PotionBlenderRecipe.CAULDORN_RECIPE_TYPE;
	}

	@Override
	public @NotNull NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	public static class Type implements RecipeType<BrewingCauldronRecipe>{
	}

	public static class CauldronRecipeSerializer implements RecipeSerializer<BrewingCauldronRecipe> {

		private static final Codec<BrewingCauldronRecipe> CODEC = RecordCodecBuilder.create(
				in -> in.group(
						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("output").forGetter(x->x.output),
						Codec.INT.fieldOf("brewingTime").forGetter(x->x.brewingTime),

						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.flatXmap(ingredients -> {
									Ingredient[] ingredientArr = ingredients.stream().filter(i -> !i.isEmpty()).toArray(Ingredient[]::new);
									if (ingredientArr.length == 0) {
										return DataResult.error(() -> "No ingredients for brewing cauldron recipe");
									}
									return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArr));
								}, DataResult::success).forGetter(x-> x.ingredients)

				).apply(in, BrewingCauldronRecipe::new)
		);

		@Override
		public @NotNull Codec<BrewingCauldronRecipe> codec() {
			return CODEC;
		}

		@Override
		public @NotNull BrewingCauldronRecipe fromNetwork(FriendlyByteBuf buff) {
			ItemStack output = buff.readItem();
			int brewingtime = buff.readInt();
			NonNullList<Ingredient> ingredientsNonNullList = NonNullList.withSize(buff.readInt(), Ingredient.EMPTY);

			ingredientsNonNullList.replaceAll(ignored -> Ingredient.fromNetwork(buff));
			return new BrewingCauldronRecipe(output,brewingtime,ingredientsNonNullList);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buff, BrewingCauldronRecipe brewingCauldronRecipe) {

			buff.writeItem(brewingCauldronRecipe.output);
			buff.writeInt(brewingCauldronRecipe.brewingTime);
			for (Ingredient ingredient : brewingCauldronRecipe.getIngredients()){
				ingredient.toNetwork(buff);
			}
		}
	}
}
