package mod.motivationaldragon.potionblender.item;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class LingeringCombinedPotionItem extends LingeringPotionItem {

    public LingeringCombinedPotionItem(Item.Properties settings) {
        super(settings);
    }


    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> stacks) {
        if (this.allowedIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> effects, TooltipFlag tooltipFlag) {
        PotionUtils.addPotionTooltip(stack, effects, 1);
    }

    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }
}
