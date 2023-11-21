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

public class CauldronRecipe implements Recipe<Container> {

	private static final  ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_recipe");
	private final ItemStack output;
	private final NonNullList<Ingredient> ingredients;
	private final int brewingTime;

	public CauldronRecipe(ItemStack output,Integer brewingTime, NonNullList<Ingredient> ingredients) {
		this.output = output;
		this.ingredients = ingredients;
		this.brewingTime = brewingTime;
	}

	@Override
	public boolean matches(Container var1, Level var2) {
		return false;
	}

	@Override
	public ItemStack assemble(Container var1, RegistryAccess var2) {
		return output.copy();
	}

	@Override
	public boolean canCraftInDimensions(int var1, int var2) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess var1) {
		return output;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return PotionBlenderRecipeSerializer.CAULDRON_RECIPE_SERIALIZER;
	}


	@Override
	public RecipeType<?> getType() {
		throw new RuntimeException("TODO: register this");
		return Type.INSTANCE;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	public static class Type implements RecipeType<CauldronRecipe>{
		public static final Type INSTANCE = new Type();
	}

	public static class CauldronRecipeSerializer implements RecipeSerializer<CauldronRecipe> {

		private static final Codec<CauldronRecipe> CODEC = RecordCodecBuilder.create(
				in -> in.group(
						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("output").forGetter(x->x.output),
						Codec.INT.fieldOf("brewingTime").forGetter(x->x.brewingTime),

						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients")
								.flatXmap(ingredients -> {
									Ingredient[] ingredientArr = ingredients.stream().filter(i -> !i.isEmpty()).toArray(i -> new Ingredient[i]);
									if (ingredientArr.length == 0) {
										return DataResult.error(() -> "No ingredients for brewing cauldron recipe");
									}
									return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArr));
								}, DataResult::success).forGetter(x-> x.ingredients)

				).apply(in, CauldronRecipe::new)
		);

		@Override
		public Codec<CauldronRecipe> codec() {
			return CODEC;
		}

		@Override
		public CauldronRecipe fromNetwork(FriendlyByteBuf buff) {
			ItemStack output = buff.readItem();
			int brewingtime = buff.readInt();
			NonNullList<Ingredient> ingredientsNonNullList = NonNullList.withSize(buff.readInt(), Ingredient.EMPTY);

			ingredientsNonNullList.replaceAll(ignored -> Ingredient.fromNetwork(buff));
			return new CauldronRecipe(output,brewingtime,ingredientsNonNullList);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buff, CauldronRecipe cauldronRecipe) {

			buff.writeItem(cauldronRecipe.output);
			buff.writeInt(cauldronRecipe.brewingTime);
			for (Ingredient ingredient : cauldronRecipe.getIngredients()){
				ingredient.toNetwork(buff);
			}
		}
	}
}
