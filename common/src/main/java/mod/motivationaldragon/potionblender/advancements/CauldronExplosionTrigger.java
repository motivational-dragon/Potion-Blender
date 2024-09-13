package mod.motivationaldragon.potionblender.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CauldronExplosionTrigger extends SimpleCriterionTrigger<CauldronExplosionTrigger.TriggerInstance> {


	public void trigger(ServerPlayer player){
		this.trigger(player, triggerInstance -> true);
	}


	public @NotNull Codec<TriggerInstance> codec(){return CauldronExplosionTrigger.TriggerInstance.CODEC;}


	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance{

		public static final Codec<CauldronExplosionTrigger.TriggerInstance> CODEC = Codec.unit(new TriggerInstance(Optional.empty()));
		public static Criterion<TriggerInstance> blewCauldron() {
			return PotionBlenderCriterionTrigger.BLEW_CAULDRON.createCriterion(new TriggerInstance(Optional.empty()));
		}


		@Override
		public @NotNull Optional<ContextAwarePredicate> player() {
			return Optional.empty();
		}
	}


}
