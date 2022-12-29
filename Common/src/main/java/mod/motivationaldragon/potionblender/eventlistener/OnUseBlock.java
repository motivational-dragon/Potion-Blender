package mod.motivationaldragon.potionblender.eventlistener;

import mod.motivationaldragon.potionblender.block.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class OnUseBlock {

    //Event handler responsible for placing a cauldron when a campfire is clicked with a cauldron in hand
    public static InteractionResult onBlockRightClick(Player player, Level world, BlockPos hitLocation) {
        if (world.isClientSide() || player.isSpectator()) {return InteractionResult.PASS;}

        BlockState blockState = world.getBlockState(new BlockPos(hitLocation));

        for (ItemStack itemStack:player.getHandSlots()) {
            if (itemStack.getItem() == Items.CAULDRON && !world.isEmptyBlock(hitLocation) && blockState.getBlock() == Blocks.CAMPFIRE) {
                if(!player.isCreative()) {itemStack.shrink(1);}

                world.removeBlock(hitLocation, false);
                Block.updateOrDestroy(blockState, ModBlock.BREWING_CAULDRON_BLOCK.defaultBlockState(), world, hitLocation,0);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }
}
