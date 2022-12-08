package mod.motivationaldragon.potionblender.item;


import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.jetbrains.annotations.NotNull;

/**
 * Class used to represent a potion with multiple Effects.
 * Since it is as special potion, it inherits all potions behaviour.
 * Inventory and translation uses default items behaviour
 */
public class CombinedPotionItem extends PotionItem {


        public CombinedPotionItem(Item.Properties settings) {
            super(settings);
        }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> stacks) {
        if (this.allowedIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }

}

