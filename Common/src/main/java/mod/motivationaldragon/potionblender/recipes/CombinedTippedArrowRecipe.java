package mod.motivationaldragon.potionblender.recipes;

import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CombinedTippedArrowRecipe extends CustomRecipe {

    public CombinedTippedArrowRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, Level world) {
        if (craftingInventory.getWidth() != 3 || craftingInventory.getHeight() != 3) {
            return false;
        }
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getItem(i + j * craftingInventory.getWidth());

                if (i == 1 && j == 1) {
                    if(!itemStack.is(ModItem.COMBINED_LINGERING_POTION)){
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
    public ItemStack assemble(CraftingContainer craftingInventory) {
        ItemStack potionItemStack = craftingInventory.getItem(1 + craftingInventory.getWidth());
        if (!potionItemStack.is(ModItem.COMBINED_LINGERING_POTION)) {
            return ItemStack.EMPTY;
        }
        ItemStack craftedItemStack = new ItemStack(Items.TIPPED_ARROW, 8);
        PotionUtils.setPotion(craftedItemStack, PotionUtils.getPotion(potionItemStack));

        List<MobEffectInstance> statusEffectInstances = new ArrayList<>(3); //Capacity = max number combined effects
        for (MobEffectInstance effectInstance : PotionUtils.getCustomEffects(potionItemStack)) {
                //The duration of the effect is 1⁄8 that of the corresponding potion.
                //Since we already divided by 4 when making the lingering potion we only need to divide by 2.
                statusEffectInstances.add(new MobEffectInstance(effectInstance.getEffect(), effectInstance.getDuration() / 2,
                        effectInstance.getAmplifier()));
        }

        PotionUtils.setCustomEffects(craftedItemStack,statusEffectInstances);



        assert craftedItemStack.getTag() != null;

        craftedItemStack.getTag().putBoolean(ModNBTKey.FORCE_COLOR_RENDERING_KEY,true);
        craftedItemStack.getTag().putBoolean(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY,true);

        return craftedItemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {return ModSpecialRecipeSerializer.COMBINED_TIPPED_ARROW;}
}
