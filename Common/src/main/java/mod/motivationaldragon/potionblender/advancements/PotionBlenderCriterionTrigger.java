package mod.motivationaldragon.potionblender.advancements;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.mixins.CriteriaTriggersAccessor;
import net.minecraft.resources.ResourceLocation;

public class PotionBlenderCriterionTrigger {
	//public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cauldron_explosion");
	public static final CauldronExplosionTrigger INSTANCE = new CauldronExplosionTrigger();

	public static void init(){
		CriteriaTriggersAccessor.potionblender_register(new ResourceLocation(Constants.MOD_ID,"cauldron_explosion").toString(), INSTANCE);
	}
}
