package mod.motivationaldragon.potionblender.item;

import mod.motivationaldragon.potionblender.PotionBlender;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItem {

    private ModItem(){
        throw new IllegalStateException("Utility class");
    }

    /* Potions Item */

    public static final Item COMBINED_POTION = register("combined_potion",
            new CombinedPotionItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1)));

    public static final Item SPLASH_COMBINED_POTION = register("splash_combined_potion",
            new SplashCombinedPotion(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1)));

    public static final Item COMBINED_LINGERING_POTION = register("lingering_combined_potion",
            new LingeringCombinedPotionItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1)));


    private static Item register(String name, Item item){
           return Registry.register(Registry.ITEM,
                    new Identifier(PotionBlender.MODID, name),
                    item);
    }

    public static void registerAll(){
        //Used to make sure that the class has been loaded and static field initialized
        PotionBlender.LOGGER.debug("Registered Items!");
    }


}
