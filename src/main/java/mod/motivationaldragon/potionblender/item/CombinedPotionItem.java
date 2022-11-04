package mod.motivationaldragon.potionblender.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.collection.DefaultedList;

/**
 * Class used to represent a potion with multiple Effects.
 * Since it is as special potion, it inherits all potions behaviour.
 * Inventory and translation uses default items behaviour
 */
public class CombinedPotionItem extends PotionItem {


        public CombinedPotionItem(Item.Settings settings) {
            super(settings);
        }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

}

