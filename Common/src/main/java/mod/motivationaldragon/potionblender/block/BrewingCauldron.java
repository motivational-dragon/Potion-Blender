package mod.motivationaldragon.potionblender.block;

import com.mojang.serialization.MapCodec;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.config.ConfigController;
import mod.motivationaldragon.potionblender.platform.Service;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("deprecation")
public class BrewingCauldron extends BaseEntityBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty HAS_FLUID = BooleanProperty.create("has_fluid");
	public static final BooleanProperty IS_BREWING = BooleanProperty.create("is_brewing");

	public static final BooleanProperty IS_SOULFIRE = BooleanProperty.create("soulfire");
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private static final VoxelShape INSIDE = box(2.0, 8.0, 2.0, 14.0, 16.0, 14.0);

	public final MapCodec<BrewingCauldron> CODEC = simpleCodec(BrewingCauldron::new);

	//TODO MINOR: this shape is not 100% accurate
	protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
			box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE), BooleanOp.ONLY_FIRST);

	Set<Item> dowseItems = Set.of(ConfigController.getConfig().getDowsingItems());
	Set<Item> litItems = Set.of(ConfigController.getConfig().getLitItems());

	public BrewingCauldron(BlockBehaviour.Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(HAS_FLUID, false)
				.setValue(FACING, Direction.NORTH)
				.setValue(IS_BREWING, false)
				.setValue(LIT, false)
				.setValue(IS_SOULFIRE, false));
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(HAS_FLUID, FACING, IS_BREWING, LIT, IS_SOULFIRE);

	}
    @Override
    protected @NotNull MapCodec<BrewingCauldron> codec() {
        return  CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull  Level level,  @NotNull BlockState blockState,  @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, Service.PLATFORM.getPlatformBrewingCauldron(),BrewingCauldronBlockEntity::tick);
    }


	@Override
	public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean b) {

		if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
			tryGetBlockEntity(level, blockPos).ifPresent(brewingCauldronBlockEntity ->
					Containers.dropContents(level, blockPos, brewingCauldronBlockEntity.getInventory()));
		}
		super.onRemove(state, level, blockPos, state, b);
	}

	@Override
	@NotNull
	public ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		if (!world.isClientSide()) {
			// Shovel check is hardcoded with the shovel class to handle modded shovels
			if (dowseItems.contains(player.getItemInHand(hand).getItem()) || player.getMainHandItem().getItem() instanceof ShovelItem) {
				dowse(world, pos);
				return ItemInteractionResult.CONSUME;
			} else if (litItems.contains(player.getItemInHand(hand).getItem()) && !state.getValue(LIT)) {
				ignite(state, world, pos);
				return ItemInteractionResult.CONSUME;
			} else {
				tryGetBlockEntity(world, pos).ifPresent(brewingCauldronBlockEntity ->
						brewingCauldronBlockEntity.onUseDelegate(state, world, pos, player));
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

	private static void ignite(@NotNull BlockState state, Level world, @NotNull BlockPos pos) {
		world.setBlockAndUpdate(pos, state.setValue(LIT, true));
		world.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1F, 1F);
		CampfireBlock.makeParticles(world, pos, false, true);
		tryGetBlockEntity(world, pos).ifPresent(BrewingCauldronBlockEntity::update);
	}

	public static void dowse(Level world, @NotNull BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getValue(LIT)) {
			world.setBlockAndUpdate(pos, state.setValue(LIT, false));
			world.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1F, 1F);
			tryGetBlockEntity(world, pos).ifPresent(BrewingCauldronBlockEntity::update);
		}
	}


	@Override
	@NotNull
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource random) {


		if (state.getValue(LIT)) {

			createDisplayParticles(world, pos, random, state.getValue(FACING), ParticleTypes.SMOKE);
			createDisplayParticles(world, pos, random, state.getValue(FACING).getOpposite(), ParticleTypes.SMOKE);

			// Play campfire sound effect
			if (random.nextInt(10) == 0) {
				world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
			}
		}

		if (state.getValue(HAS_FLUID)) {
			Optional<BrewingCauldronBlockEntity> brewingCauldronBlockEntity = tryGetBlockEntity(world, pos);
			if (brewingCauldronBlockEntity.isEmpty()) {
				return;
			}

			int color = brewingCauldronBlockEntity.get().getWaterColor();
			ParticleOptions coloredSmoke = new DustParticleOptions(Vec3.fromRGB24(color).toVector3f(), 1.0f);
			float x = pos.getX() + random.nextIntBetweenInclusive(2, 8) / 10f;
			float z = pos.getZ() + random.nextIntBetweenInclusive(2, 8) / 10f;
			world.addParticle(coloredSmoke, x, pos.getY() + 1d, z, 0, 0, 0);

			if (state.getValue(IS_BREWING) && state.getValue(LIT)) {
				x = pos.getX() + random.nextIntBetweenInclusive(2, 8) / 10f;
				z = pos.getZ() + random.nextIntBetweenInclusive(2, 8) / 10f;
				world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, pos.getY() + 1d, z, 0, 0.07, 0);

				SimpleParticleType particles = state.getValue(IS_SOULFIRE) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

				createDisplayParticles(world, pos, random, state.getValue(FACING), particles);
				createDisplayParticles(world, pos, random, state.getValue(FACING).getOpposite(), particles);

				for (int i = 0; i < 10; i++) {
					x = pos.getX() + random.nextIntBetweenInclusive(2, 8) / 10f;
					z = pos.getZ() + random.nextIntBetweenInclusive(2, 8) / 10f;
					world.addParticle(coloredSmoke, x, pos.getY() + 1d, z, 0, 0, 0);
				}

			}
		}


	}

	private static void createDisplayParticles(Level world, BlockPos pos, RandomSource random, Direction direction, ParticleOptions particleTypes) {
		Direction.Axis axis = direction.getAxis();

		double xPos = pos.getX() + 0.5;
		double yPos = pos.getY();
		double zPos = pos.getZ() + 0.5;

		double h = random.nextDouble() * 0.6 - 0.3;
		double xOffset = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : h;
		double j = random.nextDouble() * 6.0 / 16.0;
		double zOffset = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : h;
		world.addParticle(particleTypes, xPos + xOffset, yPos + j, zPos + zOffset, 0.0, 0.0, 0.0);

	}

	@Override
	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

    @Override
    public @NotNull VoxelShape getShape( @NotNull BlockState blockState,  @NotNull BlockGetter blockGetter,  @NotNull BlockPos blockPos,  @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull  BlockState blockState, @NotNull  BlockGetter blockGetter,  @NotNull BlockPos blockPos) {
        return INSIDE;
    }

	@Override
	public void fallOn(@NotNull Level world, @NotNull BlockState blockState, @NotNull BlockPos pos, Entity entity, float speed) {

		Optional<BrewingCauldronBlockEntity> brewingCauldronBlockEntity = tryGetBlockEntity(entity.level(), entity.blockPosition());
		if (brewingCauldronBlockEntity.isPresent() && !entity.level().isClientSide()) {
			brewingCauldronBlockEntity.get().onEntityLandDelegate(entity);
		}
		super.fallOn(world, blockState, pos, entity, speed);
	}


	/**
	 * Helper for getting the block entity {@link BrewingCauldronBlockEntity} associated with this {@link BrewingCauldron}
	 *
	 * @param world the world this block is in
	 * @param pos   the pos the {@link BrewingCauldronBlockEntity}. Should be the same as the block
	 * @return the {@link BlockEntity} attached to this {@link BrewingCauldron}
	 */
	private static Optional<BrewingCauldronBlockEntity> tryGetBlockEntity(Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BrewingCauldronBlockEntity alchemyMixerBlockEntity) {
			return Optional.of(alchemyMixerBlockEntity);
		}
		return Optional.empty();

	}


	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return Service.PLATFORM.createPlatformBrewingCauldronBlockEntity(pos, state);
	}

}
