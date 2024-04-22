package mod.motivationaldragon.potionblender.advancements;

import com.google.gson.JsonObject;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class CauldronExplosionTrigger extends SimpleCriterionTrigger<CauldronExplosionTrigger.TriggerInstance> {

	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cauldron_explosion");

	@Override
	public @NotNull ResourceLocation getId() {return ID;}

	@Override
	public TriggerInstance createInstance(JsonObject json, ContextAwarePredicate context, DeserializationContext deserializationContext) {
		return new TriggerInstance(context);
	}


	public void trigger(ServerPlayer player){
		this.trigger(player, instance -> true);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance{
		@NotNull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		public TriggerInstance(ContextAwarePredicate context) {
			super(ID, context);
		}
	}

}
