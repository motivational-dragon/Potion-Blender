package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CombinedTippedArrowRecipe extends SpecialCraftingRecipe {

    public CombinedTippedArrowRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        if (craftingInventory.getWidth() != 3 || craftingInventory.getHeight() != 3) {
            return false;
        }
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getStack(i + j * craftingInventory.getWidth());

                if (i == 1 && j == 1) {
                    if(!itemStack.isOf(ModItem.COMBINED_LINGERING_POTION)){
                        return false;
                    }
                } else if (itemStack.isEmpty() || !itemStack.isOf(Items.ARROW)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack potionItemStack = craftingInventory.getStack(1 + craftingInventory.getWidth());
        if (!potionItemStack.isOf(ModItem.COMBINED_LINGERING_POTION)) {
            return ItemStack.EMPTY;
        }
        ItemStack craftedItemStack = new ItemStack(Items.TIPPED_ARROW, 8);
        PotionUtil.setPotion(craftedItemStack, PotionUtil.getPotion(potionItemStack));

        List<StatusEffectInstance> statusEffectInstances = new ArrayList<>(3); //Capacity = max number combined effects
        for (StatusEffectInstance effectInstance : PotionUtil.getCustomPotionEffects(potionItemStack)) {
                //The duration of the effect is 1⁄8 that of the corresponding potion.
                //Since we already divided by 4 when making the lingering potion we only need to divide by 2.
                statusEffectInstances.add(new StatusEffectInstance(effectInstance.getEffectType(), effectInstance.getDuration() / 2,
                        effectInstance.getAmplifier()));
        }

        PotionUtil.setCustomPotionEffects(craftedItemStack,statusEffectInstances);



        assert craftedItemStack.getNbt() != null;

        craftedItemStack.getNbt().putBoolean(ModNBTKey.FORCE_COLOR_RENDERING_KEY,true);
        craftedItemStack.getNbt().putBoolean(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY,true);

        return craftedItemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {return ModSpecialRecipeSerializer.COMBINED_TIPPED_ARROW;}
}
