package mod.motivationaldragon.potionblender.event;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.eventlistener.OnUseBlock;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnBlockUsedForge {
	@SubscribeEvent
	public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock rightClickBlock){
		InteractionResult result = OnUseBlock.onBlockRightClick(rightClickBlock.getPlayer(), rightClickBlock.getWorld(),
				rightClickBlock.getHitVec().getBlockPos());

		if(result == InteractionResult.CONSUME){
			rightClickBlock.setCanceled(true);
		}
		rightClickBlock.setCancellationResult(result);
	}
}
