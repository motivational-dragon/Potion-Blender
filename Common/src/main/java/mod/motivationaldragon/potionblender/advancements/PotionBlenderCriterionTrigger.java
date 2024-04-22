package mod.motivationaldragon.potionblender.advancements;

import mod.motivationaldragon.potionblender.mixins.CriteriaTriggersAccessor;

public class PotionBlenderCriterionTrigger {
	public static final CauldronExplosionTrigger INSTANCE = new CauldronExplosionTrigger();

	public static void init(){
		CriteriaTriggersAccessor.potionblender_register(INSTANCE);
	}
}
