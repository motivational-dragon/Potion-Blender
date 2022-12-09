package mod.motivationaldragon.potionblender.item;


import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import org.jetbrains.annotations.NotNull;

public class SplashCombinedPotion extends SplashPotionItem {

    public SplashCombinedPotion(Item.Properties settings) {
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
