package mod.motivationaldragon.potionblender.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class CauldronExplosionTrigger extends SimpleCriterionTrigger<CauldronExplosionTrigger.TriggerInstance> {


	public void trigger(ServerPlayer player){
		this.trigger(player, triggerInstance -> true);
	}


	public Codec<TriggerInstance> codec(){return CauldronExplosionTrigger.TriggerInstance.CODEC;}


	public record TriggerInstance() implements SimpleCriterionTrigger.SimpleInstance{

		public static final Codec<CauldronExplosionTrigger.TriggerInstance> CODEC = Codec.unit(new TriggerInstance());
		public static Criterion<TriggerInstance> blewCauldron() {
			return PotionBlenderCriterionTrigger.BLEW_CAULDRON.createCriterion(new TriggerInstance());
		}

		@Override
		public Optional<ContextAwarePredicate> player() {
			return this.player();
		}
	}


/*	public static class TriggerInstance extends AbstractCriterionTriggerInstance{

		public TriggerInstance(Optional<ContextAwarePredicate> context) {
			super(context);
		}

	}*/

}
