package mod.motivationaldragon.potionblender.item;

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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> effects, @NotNull TooltipFlag tooltipFlag) {
        PotionUtils.addPotionTooltip(stack, effects, 1);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }
}
