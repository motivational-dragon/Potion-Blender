package mod.motivationaldragon.potionblender.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BrewingCauldron extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HAS_FLUID = BooleanProperty.of("has_fluid");
    public static final BooleanProperty REDRAW_DUMMY = BooleanProperty.of("redraw");

    public BrewingCauldron(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(HAS_FLUID, false)
                .with(FACING, Direction.NORTH).
                with(REDRAW_DUMMY, false));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(HAS_FLUID,FACING);
        builder.add(REDRAW_DUMMY);
    }


    @Override
    public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BrewingCauldronBlockEntity brewingCauldronBlockEntity = tryGetBlockEntity(world,pos);
            if(brewingCauldronBlockEntity != null) {
                brewingCauldronBlockEntity.onUseDelegate(state, world, pos, player);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos,Random random) {

        if (random.nextInt(10) == 0) {
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }


        createDisplayParticles(world, pos, random, state.get(FACING));
        createDisplayParticles(world, pos, random, state.get(FACING).getOpposite());


    }

    private static void createDisplayParticles(World world, BlockPos pos, Random random, Direction direction) {
        Direction.Axis axis = direction.getAxis();

        double xPos = pos.getX() + 0.5;
        double yPos = pos.getY();
        double zPos = pos.getZ() + 0.5;

        double h = random.nextDouble() * 0.6 - 0.3;
        double xOffset = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : h;
        double j = random.nextDouble() * 6.0 / 16.0;
        double zOffset = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : h;
        world.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos + j, zPos + zOffset, 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {

        BrewingCauldronBlockEntity brewingCauldronBlockEntity = tryGetBlockEntity(entity.getWorld(),entity.getBlockPos().down());
        if(brewingCauldronBlockEntity != null) {
            brewingCauldronBlockEntity.markDirty();
            brewingCauldronBlockEntity.getRenderAttachmentData();
        }


        if (!entity.getWorld().isClient) {
             brewingCauldronBlockEntity = tryGetBlockEntity(entity.getWorld(),entity.getBlockPos().down());
            if(brewingCauldronBlockEntity != null) {
                brewingCauldronBlockEntity.onEntityLandDelegate(entity);
            }
        }
        super.onEntityLand(world, entity);
    }


    /**
     * Helper for getting the block entity {@link BrewingCauldronBlockEntity} associated with this {@link BrewingCauldron}
     * @param world the world this block is in
     * @param pos the pos the {@link BrewingCauldronBlockEntity}. Should be the same as the block
     * @return the {@link BlockEntity} attached to this {@link BrewingCauldron}
     */
        @Nullable
        private BrewingCauldronBlockEntity tryGetBlockEntity(World world, BlockPos pos){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return blockEntity instanceof BrewingCauldronBlockEntity alchemyMixerBlockEntity ? alchemyMixerBlockEntity : null;

        }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BrewingCauldronBlockEntity(pos,state);
    }
}
