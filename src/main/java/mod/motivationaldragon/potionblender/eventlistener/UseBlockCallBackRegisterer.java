package mod.motivationaldragon.potionblender.eventlistener;

import mod.motivationaldragon.potionblender.block.ModBlock;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class UseBlockCallBackRegisterer
{
    public static void registerListener(){

        //Register a callback to replace a campfire with a BrewingCauldron when right-clicked with a cauldron
        UseBlockCallback.EVENT.register((player, world, hand, hitResult)->{

            if (world.isClient() || player.isSpectator()) {return ActionResult.PASS;}

            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            for (ItemStack itemStack:player.getHandItems()) {
                if (itemStack.getItem() == Items.CAULDRON && !world.isAir(blockPos) && blockState.getBlock() == Blocks.CAMPFIRE) {
                    if(!player.isCreative()) {itemStack.decrement(1);}

                    world.breakBlock(blockPos, false, null);
                    Block.replace(blockState, ModBlock.BREWING_CAULDRON_BLOCK.getDefaultState(), world, blockPos,0);
                    return ActionResult.CONSUME;
                }
            }
            return ActionResult.PASS;
        });
    }
}
