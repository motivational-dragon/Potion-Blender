package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.datatype.PotionBlender;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import mod.motivationaldragon.potionblender.utils.PotionType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CombinedTippedArrowRecipe extends CustomRecipe {

    public CombinedTippedArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, @NotNull Level world) {
        if (craftingInventory.getWidth() != 3 || craftingInventory.getHeight() != 3) {
            return false;
        }
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getItem(i + j * craftingInventory.getWidth());

                if (i == 1 && j == 1) {
                    if(!ModUtils.isCombinedLingeringPotion(itemStack)){
                        return false;
                    }
                } else if (itemStack.isEmpty() || !itemStack.is(Items.ARROW)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingInventory, @NotNull HolderLookup.Provider provider) {
        ItemStack potionItemStack = craftingInventory.getItem(1 + craftingInventory.getWidth());
        if (!ModUtils.isCombinedLingeringPotion(potionItemStack)) {
            return ItemStack.EMPTY;
        }
        ItemStack craftedItemStack = new ItemStack(Items.TIPPED_ARROW, 8);

        List<MobEffectInstance> statusEffectInstances = new ArrayList<>(3); //Capacity = max number combined effects
        for (MobEffectInstance effectInstance : (Objects.requireNonNull(potionItemStack.get(DataComponents.POTION_CONTENTS)).getAllEffects())) {
                //The duration of the effect is 1⁄8 that of the corresponding potion.
                //Since we already divided by 4 when making the lingering potion we only need to divide by 2.
                statusEffectInstances.add(new MobEffectInstance(effectInstance.getEffect(), effectInstance.getDuration() / 2,
                        effectInstance.getAmplifier()));
        }
        craftedItemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(),Optional.empty(),statusEffectInstances));
        craftedItemStack.set(PotionBlender.potionTypeData, PotionType.TIPPEDARROW.code);
        return craftedItemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {return PotionBlenderRecipes.COMBINED_TIPPED_ARROW_SERIALIZER;}
}
