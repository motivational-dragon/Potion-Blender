package mod.motivationaldragon.potionblender.eventlistener;

import mod.motivationaldragon.potionblender.block.ModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class UseBlockMixin {
    public static void callback(Player player, Level world, EquipmentSlot hand, HitResult hitResult) {

        if (world.isClientSide() || player.isSpectator()) {return;}

        BlockPos blockPos = new BlockPos(hitResult.getLocation());
        BlockState blockState = world.getBlockState(new BlockPos(blockPos));

        for (ItemStack itemStack:player.getHandSlots()) {
            if (itemStack.getItem() == Items.CAULDRON && !world.isEmptyBlock(blockPos) && blockState.getBlock() == Blocks.CAMPFIRE) {
                if(!player.isCreative()) {itemStack.shrink(1);}

                world.removeBlock(blockPos, false);
                Block.updateOrDestroy(blockState, ModBlock.BREWING_CAULDRON_BLOCK.defaultBlockState(), world, blockPos,0);
            }
        }
    }
}
