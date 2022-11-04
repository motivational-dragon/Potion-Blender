package mod.motivationaldragon.potionblender.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LingeringCombinedPotionItem extends LingeringPotionItem {

    public LingeringCombinedPotionItem(Settings settings) {
        super(settings);
    }


    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        PotionUtil.buildTooltip(stack, tooltip, 1);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}
