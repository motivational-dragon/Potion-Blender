package mod.motivationaldragon.potionblender.advancements;

import com.google.gson.JsonObject;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class CauldronExplosionTrigger extends SimpleCriterionTrigger<CauldronExplosionTrigger.TriggerInstance> {

	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cauldron_explosion");
	public static final CauldronExplosionTrigger INSTANCE = new CauldronExplosionTrigger();

	@Override
	public ResourceLocation getId() {return ID;}

	@Override
	protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
		LocationPredicate locationPredicate = LocationPredicate.fromJson(json.get("cauldron_location"));
		return new TriggerInstance(player, locationPredicate);
	}

	public void trigger(ServerPlayer player, BlockPos pos, ServerLevel level){
		this.trigger(player, triggerInstance -> triggerInstance.matches(level, pos));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance{

		private final LocationPredicate pos;

		@NotNull
		@Override
		public ResourceLocation getCriterion() {
			return ID;
		}

		public TriggerInstance(EntityPredicate.Composite playerPredicate, LocationPredicate cauldronLocation) {
			super(ID, playerPredicate);
			this.pos = cauldronLocation;
		}

		public LocationPredicate getPos() {
			return this.pos;
		}

		@NotNull
		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			if(pos != LocationPredicate.ANY){
				json.add("cauldron_location", pos.serializeToJson());
			}
			return json;
		}

		boolean matches(ServerLevel world, BlockPos pos){
			return this.pos.matches(world, pos.getX(), pos.getY(), pos.getZ());
		}

	}

}
