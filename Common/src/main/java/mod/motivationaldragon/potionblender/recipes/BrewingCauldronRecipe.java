package mod.motivationaldragon.potionblender.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
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

		//TODO DEBUG BROKEN CONDITION
		if(isOrdered) {
			//For each ingredient in ingredients, there is an item in the container that matches the ingredient at the same index
			if(container.getContainerSize() != ingredients.size()) {return false;}
			return IntStream.range(0, ingredients.size()).allMatch(i -> ingredients.get(i).test(container.getItem(i)));
		} else {
			//For each ingredients, there is at lease one item in the container that matches the ingredient
			if(container.getContainerSize() != ingredients.size()) {return false;}
			return ingredients.stream().allMatch(ingredient -> IntStream.range(0, container.getContainerSize())
					.anyMatch(i -> ingredient.test(container.getItem(i))));
		}
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
		return PotionBlenderRecipes.POTION_BLENDING;
	}



	public int getBrewingTime() {
		return brewingTime;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return PotionBlenderRecipes.POTION_BLENDING_RECIPE_TYPE;
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
						Codec.INT.optionalFieldOf("color", Constants.WATER_TINT).forGetter(x->x.color),
						Codec.BOOL.fieldOf("isOrdered").forGetter(x->x.isOrdered),
						Codec.DOUBLE.optionalFieldOf("decayRate",2.0).forGetter(x->x.decayRate),

						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.flatXmap(ingredientList -> {
									Ingredient[] ingredientArr = ingredientList.stream().filter(i -> !i.isEmpty()).toArray(Ingredient[]::new);
									if (ingredientArr.length == 0) {
										return DataResult.error(() -> "No ingredients for brewing cauldron recipe");
									}
									return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArr));
								}, DataResult::success).forGetter(x-> x.ingredients),


						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("output").flatXmap(
								itemStack -> {
									if (itemStack.isEmpty()) {
										return DataResult.error(() -> "Empty output for brewing cauldron recipe");
									}
									//Disallow using usePotionMergingRules if the output is not a potion
									if (itemStack.getItem() instanceof PotionItem) {
										return DataResult.error(() -> "Output is a potion but has no tag");
									}
									return DataResult.success(itemStack);
								}, DataResult::success).forGetter(x->x.output)
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
