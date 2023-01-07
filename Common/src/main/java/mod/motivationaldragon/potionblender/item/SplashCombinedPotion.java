package mod.motivationaldragon.potionblender.item;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import org.jetbrains.annotations.NotNull;

public class SplashCombinedPotion extends SplashPotionItem {

    public SplashCombinedPotion(Item.Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }
}
