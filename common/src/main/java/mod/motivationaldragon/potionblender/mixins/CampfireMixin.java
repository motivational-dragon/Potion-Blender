package mod.motivationaldragon.potionblender.mixins;

import mod.motivationaldragon.potionblender.block.BrewingCauldron;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public abstract class CampfireMixin {

	@Shadow @Final public static DirectionProperty FACING;

	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	public void useItemOn(ItemStack ignored,BlockState campfireBlockstate, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {

		if(player.isSpectator()) {return;}

		ItemStack itemInHand = player.getItemInHand(interactionHand);
		if(itemInHand.is(Items.CAULDRON)){
			if(!player.isCreative()) {
				itemInHand.shrink(1);}

			level.removeBlock(blockHitResult.getBlockPos(), false);
			BlockState cauldronBlockState = PotionBlenderBlock.BREWING_CAULDRON_BLOCK.defaultBlockState();
			cauldronBlockState = cauldronBlockState.setValue(BrewingCauldron.LIT,  campfireBlockstate.getValue(CampfireBlock.LIT));
			cauldronBlockState = cauldronBlockState.setValue(BrewingCauldron.IS_SOULFIRE, campfireBlockstate.getBlock().equals(Blocks.SOUL_CAMPFIRE));
			cauldronBlockState = cauldronBlockState.setValue(BrewingCauldron.FACING, campfireBlockstate.getValue(FACING));

			level.playSound(null, blockPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1F, 1F);

			Block.updateOrDestroy(null, cauldronBlockState, level, blockPos,0);
			cir.setReturnValue(ItemInteractionResult.CONSUME);
		}
	}
}
