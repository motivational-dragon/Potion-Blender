package mod.motivationaldragon.potionblender.item;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup MULTI_POTION_GROUP = FabricItemGroupBuilder.build(
            new Identifier(PotionBlender.MODID, PotionBlender.MODID),
            () -> new ItemStack(ModItem.COMBINED_POTION));

}
