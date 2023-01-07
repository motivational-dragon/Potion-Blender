package mod.motivationaldragon.potionblender.item;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.jetbrains.annotations.NotNull;

/**
 * Class used to represent a potion with multiple Effects.
 * Since it is as special potion, it inherits all potions' behaviour.
 * Inventory and translation uses default items behaviour
 */
public class CombinedPotionItem extends PotionItem {

    public CombinedPotionItem(Item.Properties settings) {
            super(settings);
        }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }

}

