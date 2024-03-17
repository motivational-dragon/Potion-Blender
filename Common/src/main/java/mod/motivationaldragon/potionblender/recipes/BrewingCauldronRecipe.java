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




	private final boolean usePotionMergingRules;

	private int color;

	private double decayRate;

	private boolean isOrdered;
	private final int brewingTime;

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;

	public BrewingCauldronRecipe(int brewingTime,
	                             boolean usePotionMergingRules,
	                             int color,
								 boolean isOrdered,
								 double decayRate,
	                             NonNullList<Ingredient> ingredients,
			                     ItemStack output) {
		this.ingredients = ingredients;
		this.brewingTime = brewingTime;
		this.color = color;
		this.decayRate = decayRate;
		this.isOrdered = isOrdered;
		this.usePotionMergingRules = usePotionMergingRules;
		this.output = output;
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
						Codec.INT.fieldOf("brewingTime").forGetter(x->x.brewingTime),
						Codec.BOOL.optionalFieldOf("usePotionMergingRules", false).forGetter(x->x.usePotionMergingRules),
						Codec.INT.fieldOf("color").forGetter(x->x.color),
						Codec.BOOL.fieldOf("isOrdered").forGetter(x->x.isOrdered),
						Codec.DOUBLE.fieldOf("decayRate").forGetter(x->x.decayRate),

						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.flatXmap(ingredientList -> {
									Ingredient[] ingredientArr = ingredientList.stream().filter(i -> !i.isEmpty()).toArray(Ingredient[]::new);
									if (ingredientArr.length == 0) {
										return DataResult.error(() -> "No ingredients for brewing cauldron recipe");
									}
									return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArr));
								}, DataResult::success).forGetter(x-> x.ingredients),

						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(x->x.output)
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
			int color  = buff.readInt();
			boolean isOrdered = buff.readBoolean();
			double decayRate = buff.readDouble();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(buff.readInt(), Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buff));
			return new BrewingCauldronRecipe(brewingTime, usePotionMergingRules, color, isOrdered, decayRate, ingredients, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buff, BrewingCauldronRecipe brewingCauldronRecipe) {
			buff.writeInt(brewingCauldronRecipe.brewingTime);
			buff.writeBoolean(brewingCauldronRecipe.usePotionMergingRules);
			buff.writeInt(brewingCauldronRecipe.color);
			buff.writeBoolean(brewingCauldronRecipe.isOrdered);
			buff.writeDouble(brewingCauldronRecipe.decayRate);
			NonNullList<Ingredient> recipeIngredients = brewingCauldronRecipe.getIngredients();
			buff.writeInt(recipeIngredients.size());
			for (Ingredient ingredient : recipeIngredients){ingredient.toNetwork(buff);}
			buff.writeItem(brewingCauldronRecipe.output);
		}
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getColor() {
		return color;
	}

	public boolean isOrdered() {
		return isOrdered;
	}


	public boolean usePotionMeringRules() {
		return usePotionMergingRules;
	}

	public double getDecayRate() {
		return decayRate;
	}
}
