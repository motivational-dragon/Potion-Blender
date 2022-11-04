package mod.motivationaldragon.potionblender.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.util.collection.DefaultedList;

public class SplashCombinedPotion extends SplashPotionItem {

    public SplashCombinedPotion(Settings settings) {
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
