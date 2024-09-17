package mod.motivationaldragon.potionblender.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record MultipleInputRecipe(Container inv) implements RecipeInput {

	@Override
	public @NotNull ItemStack getItem(int i) {
		return inv.getItem(i);
	}

	@Override
	public int size() {
		return inv.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return inv.isEmpty();
	}
}
