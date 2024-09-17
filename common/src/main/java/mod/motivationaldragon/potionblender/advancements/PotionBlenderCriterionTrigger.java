package mod.motivationaldragon.potionblender.advancements;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class PotionBlenderCriterionTrigger {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cauldron_explosion");
	public static final CauldronExplosionTrigger BLEW_CAULDRON = new CauldronExplosionTrigger();

	public static void register(BiConsumer<CriterionTrigger<?>, ResourceLocation> r){
		r.accept(BLEW_CAULDRON, ID);
	}
}
