package mod.motivationaldragon.potionblender.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class BrewingCauldronRecipe implements Recipe<Container> {

	private final ItemStack output;
	private final NonNullList<Ingredient> ingredients;

	private final boolean usePotionMeringRules;
	private final int brewingTime;

	public BrewingCauldronRecipe(ItemStack output, int brewingTime, boolean usePotionMergingRules, NonNullList<Ingredient> ingredients) {
		this.output = output;
		this.ingredients = ingredients;
		this.brewingTime = brewingTime;
		this.usePotionMeringRules = usePotionMergingRules;
	}

	@Override
	public boolean matches(@NotNull Container container, @NotNull Level level) {
		if(level.isClientSide()) {return false;}
			return ingredients.stream().anyMatch(ingredient ->
					IntStream.range(0, container.getContainerSize())
							.anyMatch(i -> ingredient.test(container.getItem(i))));
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
		return PotionBlenderRecipe.POTION_BLENDING;
	}



	public int getBrewingTime() {
		return brewingTime;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return PotionBlenderRecipe.POTION_BLENDING_RECIPE_TYPE;
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
						Codec.BOOL.optionalFieldOf("usePotionMergingRules", false).forGetter(x->x.usePotionMeringRules),

						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.flatXmap(ingredientList -> {
									Ingredient[] ingredientArr = ingredientList.stream().filter(i -> !i.isEmpty()).toArray(Ingredient[]::new);
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
			int brewingTime = buff.readInt();
			boolean usePotionMergingRules = buff.readBoolean();
			NonNullList<Ingredient> ingredientsNonNullList = NonNullList.withSize(buff.readInt(), Ingredient.EMPTY);

			ingredientsNonNullList.replaceAll(ignored -> Ingredient.fromNetwork(buff));
			return new BrewingCauldronRecipe(output,brewingTime,usePotionMergingRules,ingredientsNonNullList);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buff, BrewingCauldronRecipe brewingCauldronRecipe) {

			buff.writeItem(brewingCauldronRecipe.output);
			buff.writeInt(brewingCauldronRecipe.brewingTime);
			buff.writeBoolean(brewingCauldronRecipe.usePotionMeringRules);

			NonNullList<Ingredient> recipeIngredients = brewingCauldronRecipe.getIngredients();
			buff.writeInt(recipeIngredients.size());
			for (Ingredient ingredient : recipeIngredients){
				ingredient.toNetwork(buff);
			}
		}
	}
}
