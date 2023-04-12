package mod.motivationaldragon.potionblender.advancements;

import mod.motivationaldragon.potionblender.mixins.CriteriaTriggersAccessor;

public class PotionBlenderCriterionTrigger {
	public static void init(){
		CriteriaTriggersAccessor.potionblender_register(CauldronExplosionTrigger.INSTANCE);
	}
}
