package mod.motivationaldragon.potionblender.utils;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PotionType {


	NORMAL(0),
	SPLASH(1),
	LINGERING(2);

	PotionType(int num) {
		code = num;
	}

	public final int code;

	public static final Map<Integer, PotionType> codeToPotionType;
	static {
		codeToPotionType = HashMap.newHashMap(3);
		codeToPotionType.put(0, NORMAL);
		codeToPotionType.put(1, SPLASH);
		codeToPotionType.put(2, LINGERING);
	}

	static Optional<PotionType> itemToPotion(Item item){
		if(item == Items.LINGERING_POTION){return Optional.of(LINGERING);}
		if(item == Items.SPLASH_POTION){return Optional.of(SPLASH);}
		if(item == Items.POTION){return Optional.of(NORMAL);}
		 Constants.LOG.error(
				 String.format("Failed to recognize potion type " +
						 "Valid potion are Potions, Splash Potions, Lingering Potion. Did you try merge potion into an item that is not a potion?" +
						 "Invalid item: %s", item));
		return Optional.empty();
	}
}
