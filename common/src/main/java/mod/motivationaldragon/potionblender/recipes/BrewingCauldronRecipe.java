package mod.motivationaldragon.potionblender.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.config.ConfigController;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class BrewingCauldronRecipe implements Recipe<RecipeInput> {


	private final boolean usePotionMergingRules;

	private final int color;

	private final double decayRate;

	private final boolean isOrdered;
	private final int brewingTime;

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;

	public BrewingCauldronRecipe(
	                             int brewingTime,
	                             boolean usePotionMergingRules,
	                             int color,
	                             boolean isOrdered,
	                             double decayRate,
	                             NonNullList<Ingredient> ingredients,
	                             ItemStack output) {

		//Disallow using usePotionMergingRules if the output is not a potion
		if (usePotionMergingRules && !(output.getItem() instanceof PotionItem)) {
			throw new IllegalArgumentException("Output must be a potion if usePotionMergingRules is true");
		}

		//At metadata to display potion item in JEI, otherwise it will display as an uncraftable potion
		// Since test are performed on the item class, NBT data is not relevant and can be set to whatever we want
		if (usePotionMergingRules) {
			for (Ingredient ingredient : ingredients) {
				for (ItemStack stack : ingredient.getItems()) {
					if ((stack.getItem() instanceof PotionItem)) {
						stack.set(DataComponents.CUSTOM_NAME, Component.translatable(Constants.MOD_ID + ".recipe.potion_wildcard_names"));
					}
				}
			}
			output.set(DataComponents.CUSTOM_NAME,Component.translatable(Constants.MOD_ID + ".recipe.merged_potion_wildcard_names"));
		}

		this.ingredients = ingredients;
		this.brewingTime = brewingTime;
		this.color = color;
		this.decayRate = decayRate;
		this.isOrdered = isOrdered;
		this.usePotionMergingRules = usePotionMergingRules;
		this.output = output;
	}

	@Override
	public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
		if (level.isClientSide()) {
			return false;
		}

		if (isOrdered) {
			//For each ingredient in ingredients, there is an item in the recipeInput that matches the ingredient at the same index
			if (recipeInput.size() != ingredients.size()) {
				return false;
			}
			return IntStream.range(0, ingredients.size()).allMatch(i -> ingredients.get(i).test(recipeInput.getItem(i)));
		} else {
			//For each ingredient, there is at lease one item in the recipeInput that matches the ingredient
			if (recipeInput.size() != ingredients.size()) {
				return false;
			}
			return ingredients.stream().allMatch(ingredient -> IntStream.range(0, recipeInput.size())
					.anyMatch(i -> ingredient.test(recipeInput.getItem(i))));
		}
	}



	@Override
	public @NotNull ItemStack assemble(@NotNull RecipeInput var1, HolderLookup.@NotNull Provider provider) {
		return output.copy();
	}


	@Override
	public boolean canCraftInDimensions(int var1, int var2) {
		return true;
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
		return output;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return CauldronRecipeSerializer.INSTANCE;
	}


	public int getBrewingTime() {
		return brewingTime;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	@Override
	public @NotNull NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}



	public static class Type implements RecipeType<BrewingCauldronRecipe> {
		public static final Type INSTANCE = new Type();
		public static final String ID = "brewing_cauldron_recipe";
	}

	public static class CauldronRecipeSerializer implements RecipeSerializer<BrewingCauldronRecipe> {

		public static final CauldronRecipeSerializer INSTANCE = new CauldronRecipeSerializer();

		private static final MapCodec<BrewingCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(
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
								}, DataResult::success).forGetter(x-> {
									if(x.ingredients.size() > ConfigController.getConfig().getCauldronInventorySize()) {
										throw new IllegalArgumentException("Too many ingredients for brewing cauldron recipe");
									}
									return x.ingredients;
								}),
						ItemStack.CODEC.fieldOf("output").flatXmap(
								itemStack -> {
									if (itemStack.isEmpty()) {
										return DataResult.error(() -> "Empty output for brewing cauldron recipe");
									}
									return DataResult.success(itemStack);
								}, DataResult::success).forGetter(x->x.output)
				).apply(in, BrewingCauldronRecipe::new)
		);

		public static final StreamCodec<RegistryFriendlyByteBuf, BrewingCauldronRecipe> STREAM_CODEC = StreamCodec.of(CauldronRecipeSerializer::toNetwork, CauldronRecipeSerializer::fromNetwork);


		@Override
		public @NotNull MapCodec<BrewingCauldronRecipe> codec() {
			return CODEC;
		}

		@Override
		public @NotNull StreamCodec<RegistryFriendlyByteBuf, BrewingCauldronRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static @NotNull BrewingCauldronRecipe fromNetwork(RegistryFriendlyByteBuf buff) {
			int brewingTime = buff.readInt();
			boolean usePotionMergingRules = buff.readBoolean();
			int color = buff.readInt();
			boolean isOrdered = buff.readBoolean();
			double decayRate = buff.readDouble();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(buff.readInt(), Ingredient.EMPTY);
			ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buff));
			ItemStack output = ItemStack.STREAM_CODEC.decode(buff);
			return new BrewingCauldronRecipe(brewingTime, usePotionMergingRules, color, isOrdered, decayRate, ingredients, output);
		}


		private static void toNetwork(RegistryFriendlyByteBuf buff, BrewingCauldronRecipe brewingCauldronRecipe) {
			buff.writeInt(brewingCauldronRecipe.brewingTime);
			buff.writeBoolean(brewingCauldronRecipe.usePotionMergingRules);
			buff.writeInt(brewingCauldronRecipe.color);
			buff.writeBoolean(brewingCauldronRecipe.isOrdered);
			buff.writeDouble(brewingCauldronRecipe.decayRate);
			NonNullList<Ingredient> recipeIngredients = brewingCauldronRecipe.getIngredients();
			buff.writeInt(recipeIngredients.size());
			for (Ingredient ingredient : recipeIngredients) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(buff, ingredient);
			}
			ItemStack.STREAM_CODEC.encode(buff,brewingCauldronRecipe.output);
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
