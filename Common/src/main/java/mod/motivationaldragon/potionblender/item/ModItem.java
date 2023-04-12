package mod.motivationaldragon.potionblender.item;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModItem {

    private ModItem(){
        throw new IllegalStateException("Utility class");
    }

    /* Potions Item */

    public static final Item COMBINED_POTION = new CombinedPotionItem(
            new Item.Properties().stacksTo(1));

    public static final Item COMBINED_SPLASH_POTION = new SplashCombinedPotion(
            new Item.Properties().stacksTo(1));

    public static final Item COMBINED_LINGERING_POTION = new LingeringCombinedPotionItem(
            new Item.Properties().stacksTo(1));


    public static void register(BiConsumer<Item, ResourceLocation> r){
        r.accept(COMBINED_POTION, new ResourceLocation(Constants.MOD_ID, "combined_potion"));
        r.accept(COMBINED_SPLASH_POTION, new ResourceLocation(Constants.MOD_ID, "splash_combined_potion"));
        r.accept(COMBINED_LINGERING_POTION, new ResourceLocation(Constants.MOD_ID, "lingering_combined_potion"));
        Constants.LOG.debug("Registered Items!");
    }

    public static void registerFunctionalBlocksItems(Consumer<Item> r){
        r.accept(PotionBlenderBlock.BREWING_CAULDRON_ITEM);
    }


}
