package mod.motivationaldragon.potionblender.item;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ModItem {

    private ModItem(){
        throw new IllegalStateException("Utility class");
    }


    public static void registerFunctionalBlocksItems(Consumer<Item> r){
        r.accept(PotionBlenderBlock.BREWING_CAULDRON_ITEM);
    }


}
