package mod.motivationaldragon.potionblender.event;

import mod.motivationaldragon.potionblender.eventlistener.OnUseBlock;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class OnUseBlockFabric {
	public static void registerHandler() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult)-> OnUseBlock.onBlockRightClick(player,world,hitResult.getBlockPos()));
	}
}
