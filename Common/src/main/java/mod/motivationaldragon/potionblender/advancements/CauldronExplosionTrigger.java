package mod.motivationaldragon.potionblender.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class CauldronExplosionTrigger extends SimpleCriterionTrigger<CauldronExplosionTrigger.TriggerInstance> {


	public TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> context, DeserializationContext deserializationContext) {
		return new TriggerInstance(context);
	}


	public void trigger(ServerPlayer player, BlockPos pos, ServerLevel level){
		this.trigger(player, triggerInstance -> {return true;});
	}


	public static class TriggerInstance extends AbstractCriterionTriggerInstance{

		public TriggerInstance(Optional<ContextAwarePredicate> context) {
			super(context);
		}

	}

}
