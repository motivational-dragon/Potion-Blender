package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.block.BrewingCauldron;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.projectile.ThrownPotion.class)
public abstract class ThrownPotionMixin extends Entity {


	protected ThrownPotionMixin(EntityType<? extends ThrownPotion> entityType, Level level) {
		super(entityType, level);
		throw new IllegalStateException("Cannot instantiate mixin class");
	}

	/**
	 * Make thrown potions dowse BrewingCauldron
	 */
	@Inject(method = "dowseFire", at = @At("TAIL"))
	private void dowseFire(BlockPos pos, CallbackInfo ci) {
		BlockState blockstate = this.level().getBlockState(pos);
		if (blockstate.getBlock() instanceof BrewingCauldron) {
			BrewingCauldron.dowse(this.level(), pos);
		}
	}

}
