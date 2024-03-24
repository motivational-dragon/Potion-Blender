package mod.motivationaldragon.potionblender.blockentities;


import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.block.BrewingCauldron;
import mod.motivationaldragon.potionblender.config.ConfigController;
import mod.motivationaldragon.potionblender.config.PotionBlenderConfig;
import mod.motivationaldragon.potionblender.platform.Service;
import mod.motivationaldragon.potionblender.recipes.BrewingCauldronRecipe;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderRecipes;
import mod.motivationaldragon.potionblender.utils.ModNBTKey;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import mod.motivationaldragon.potionblender.utils.PotionEffectMerger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mod.motivationaldragon.potionblender.utils.ModUtils.isACombinedPotion;

public abstract class BrewingCauldronBlockEntity extends BlockEntity {


	private static final String POTION_MIXER_KEY = Constants.MOD_ID + ".ConfigController";


	/**
	 * How high dropped item spawn relative to the block position.
	 * Useful to avoid having item spawn by cauldron being thrown horizontally
	 */
	private static final int ITEM_DROP_OFFSET = 1;

	private static final PotionBlenderConfig config = ConfigController.getConfig();


	/**
	 * Is this cauldron counting brewing tick?
	 * A cauldron canBrew while not being actively brewing because the user is adding more item into it for instance
	 */
	private boolean isBrewing = false;

	/**
	 * Can the cauldron brew a potion?*
	 * Aka are there potion in the cauldron and was an ingredient thrown into the cauldron?
	 */
	private boolean canBrew = false;
	/**
	 * The brewing progress of the cauldron. It is reset when the cauldron is not brewing or cannot craft anymore
	 */
	private int brewingProgress = 0;


	/**
	 * The cauldron inventory. It is mean to only contain potion
	 */
	private NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
	/**
	 * The current amount of potion in the cauldron. Useful since the inventory size is constant
	 */
	private int numberOfItems;

	/**
	 * Quick check for the main thread
	 */
	private final RecipeManager.CachedCheck<Container, BrewingCauldronRecipe> quickCheck;



	protected BrewingCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(Service.PLATFORM.getPlatformBrewingCauldron(), pos, state);
		this.numberOfItems = 0;
		this.quickCheck = RecipeManager.createCheck(PotionBlenderRecipes.POTION_BLENDING_RECIPE_TYPE);
	}

	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	public int size() {
		return this.inventory.size();
	}

	private void updateListeners() {
		this.setChanged();
		syncInventoryWithClient();
		assert this.getLevel() != null;
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_NEIGHBORS);
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/**
	 * Send a packet to sync this block entity inventory with the client
	 */
	protected abstract void syncInventoryWithClient();

	/**
	 * Delegation from the onEntityUse method in the {@link net.minecraft.world.level.block.Block} class with the same signature
	 * Useful to access data such as inventory attached to the block entity from {@link net.minecraft.world.level.block.Block} callback
	 * T
	 */
	public void onUseDelegate(BlockState state, Level level, BlockPos pos, Player player) {
		if (numberOfItems >= 1) {
			stopBrewing();
			dropInventoryContent();
		}
	}

	private void stopBrewing() {
		canBrew = false;
		isBrewing = false;
		this.resetProgress();
		this.getBlockState().setValue(BrewingCauldron.IS_BREWING, false);
		this.setChanged();
	}


	/**
	 * Delegation from the onEntityLand method in the {@link net.minecraft.world.level.block.Block} class
	 * Useful to access data such as inventory attached to the block entity from {@link net.minecraft.world.level.block.Block} callback
	 *
	 * @param entity the entity that landed on the attached block
	 * @see net.minecraft.world.level.block.Block#fallOn(Level, BlockState, BlockPos, Entity, float) for the full method documentation
	 * and ommited parameter
	 */
	public void onEntityLandDelegate(Entity entity) {
		assert level != null;
		if (level.isClientSide() || isBrewing) {
			return;
		}

		if (entity instanceof ItemEntity itemEntity) {
			ItemStack itemStack = itemEntity.getItem();

			//Handle overload mechanic where a cauldron explode if a combined potion is thrown into it
			if (isACombinedPotion(itemStack) && itemEntity.getOwner() != null) {
				explode(entity);
				entity.remove(Entity.RemovalReason.DISCARDED);
				return;
			}

			//Deny adding duplicated instant potion
			if (itemStack.is(Items.POTION) && numberOfItems < inventory.size() &&
					PotionEffectMerger.wouldIgnoreInstantPotion(itemStack, this.getInventoryStatusEffectsInstances()))
				return;

			//Prevent adding more item than the inventory can hold
			if (numberOfItems >= inventory.size()) {
				return;
			}

			addItemToCauldron(itemEntity);

			//Craft the potion if a recipe is found
			Optional<RecipeHolder<BrewingCauldronRecipe>> recipe = getRecipe();
			if (recipe.isPresent()) {
				entity.remove(Entity.RemovalReason.DISCARDED);

				canBrew = true;


				getLevel().setBlockAndUpdate(getBlockPos(), getLevel().getBlockState(this.getBlockPos()).setValue(BrewingCauldron.IS_BREWING, true));

				this.setChanged();
				level.playSound(null, this.getBlockPos(), SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
			}
		}
	}


	private Optional<RecipeHolder<BrewingCauldronRecipe>> getRecipe() {
		if (numberOfItems <= 0) {
			return Optional.empty();
		}
		Container container = new SimpleContainer(
				this.inventory.stream()
						.filter(itemStack -> !itemStack.is(Items.AIR))
						.toArray(ItemStack[]::new));
		return quickCheck.getRecipeFor(container, level);
	}


	/**
	 * Main method used to handle the brewing logic when merging the potion
	 *
	 * @param level the level attached to this block entity
	 * @param pos   the pos of this block entity
	 */
	private void craftCombinedPotion(Level level, @NotNull BlockPos pos, BrewingCauldronRecipe recipe) {
		if (level.isClientSide()) {
			return;
		}
		//read recipe
		Item potionType = recipe.getResultItem(level.registryAccess()).getItem();
		ItemStack potionToCraft = new ItemStack(potionType);

		//This madness is to give the correct tag to the correct potion type. That way mixin can determinate how to name the potions
		if (potionToCraft.is(Items.POTION)) {
			potionToCraft.getOrCreateTag().putBoolean(ModNBTKey.IS_COMBINED_POTION, true);
		} else if (potionToCraft.is(Items.SPLASH_POTION)) {
			potionToCraft.getOrCreateTag().putBoolean(ModNBTKey.IS_COMBINED_SPLASH_POTION, true);
		} else if (potionToCraft.is(Items.LINGERING_POTION)) {
			potionToCraft.getOrCreateTag().putBoolean(ModNBTKey.IS_COMBINED_LINGERING_POTION, true);
		} else {
			Constants.LOG.error(
					String.format("Cannot merge potion to an item that is not a potion. " +
							"Valid potion are Potions, Splash Potions, Lingering Potion. Did you try to add an invalid recipe?" +
							"The item is: %s", potionToCraft.getItem()));
			return;
		}
		List<MobEffectInstance> finalPotionStatusEffects = PotionEffectMerger.mergeCombinableEffects(this.getInventoryStatusEffectsInstances(), recipe.getDecayRate());

		if (ModUtils.isCombinedLingeringPotion(potionToCraft)) {
			finalPotionStatusEffects = PotionEffectMerger.mergeLingeringPotionEffects(finalPotionStatusEffects);
		}

		//create and drop the potion contained all the effect of the previous potion
		ItemStack potionItemStack = PotionUtils.setCustomEffects(potionToCraft, finalPotionStatusEffects);

		int color = PotionUtils.getColor(finalPotionStatusEffects);
		//Used to force potion color rendering with the help of mixins
		potionItemStack.getOrCreateTag().putInt(PotionUtils.TAG_CUSTOM_POTION_COLOR, color);

		outputItem(level, pos, potionItemStack);

		//Drop all old potion bottle minus the one used for the new potion.
		Containers.dropItemStack(level, pos.getX(), (double) pos.getY() + ITEM_DROP_OFFSET, pos.getZ(), new ItemStack(Items.GLASS_BOTTLE, numberOfItems - 1));

		emptyCauldron();
	}

	private static void outputItem(Level level, @NotNull BlockPos pos, ItemStack potionItemStack) {
		ItemEntity outputItemEntity = new ItemEntity(level, pos.getX() + 0.5, (double) pos.getY() + ITEM_DROP_OFFSET, pos.getZ() + 0.5, potionItemStack);

		//Combined potion need to float, otherwise it's hitting the cauldron and trigger the explosion mechanic
		outputItemEntity.setNoGravity(true);
		outputItemEntity.setDeltaMovement(Vec3.ZERO);
		outputItemEntity.setThrower(null);
		level.addFreshEntity(outputItemEntity);
		level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 1.0f);
	}


	public static void tick(Level level, BlockPos pos, BlockState state, BrewingCauldronBlockEntity brewingCauldron) {
		if (level.isClientSide()) {
			return;
		}

		if (!brewingCauldron.canBrew) {
			brewingCauldron.resetProgress();
			return;
		}

		brewingCauldron.isBrewing = true;
		brewingCauldron.brewingProgress++;
		RecipeHolder<BrewingCauldronRecipe> recipe = brewingCauldron.getRecipe().orElse(null);
		if (recipe == null) {
			brewingCauldron.stopBrewing();
			return;
		}

		if (brewingCauldron.brewingProgress >= recipe.value().getBrewingTime()) {
			if (recipe.value().usePotionMeringRules()) {
				brewingCauldron.craftCombinedPotion(level, pos, recipe.value());
			} else {
				//Handle the case where we are not crafting potion
				outputItem(level, pos, recipe.value().getResultItem(level.registryAccess()));

				brewingCauldron.emptyCauldron();
				brewingCauldron.updateListeners();
			}
			brewingCauldron.stopBrewing();
		}
		setChanged(level, pos, state);
	}

	private void resetProgress() {
		this.brewingProgress = 0;
	}

	/**
	 * Drop all items in the cauldron inventory in the world
	 */
	private void dropInventoryContent() {
		if (level.isClientSide()) {
			return;
		}
		level.playSound(null, this.getBlockPos(), SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0f, 1.0f);
		Containers.dropContents(level, this.getBlockPos().offset(0, ITEM_DROP_OFFSET, 0), this.getInventory());
		emptyCauldron();
	}

	/**
	 * Delete the cauldron inventory and set the block state to reflect the change
	 */
	private void emptyCauldron() {
		inventory.clear();
		numberOfItems = 0;

		BlockState hasFluid = level.getBlockState(this.getBlockPos())
				.setValue(BrewingCauldron.HAS_FLUID, false)
				.setValue(BrewingCauldron.IS_BREWING, false);
		level.setBlockAndUpdate(this.getBlockPos(), hasFluid);
		updateListeners();
	}

	private void explode(Entity entity) {
		assert level != null;
		assert !level.isClientSide();

		BlockPos pos = this.getBlockPos();

		List<ServerPlayer> nearbyPlayers = this.getLevel().getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(5));
		for (ServerPlayer player : nearbyPlayers) {
			PotionBlenderCriterionTrigger.INSTANCE.trigger(player, pos, (ServerLevel) this.getLevel());
		}
		this.level.explode(entity, pos.getX(), pos.getY(), pos.getZ(), 1.5F, Level.ExplosionInteraction.BLOCK);
	}


	/**
	 * Add an item entity to the cauldron inventory
	 *
	 * @param itemEntity the item entity
	 */
	private void addItemToCauldron(@NotNull ItemEntity itemEntity) {
		assert level != null;
		if (level.isClientSide()) {
			return;
		}
		level.playSound(null, this.getBlockPos(), SoundEvents.BOAT_PADDLE_WATER, SoundSource.BLOCKS, 2 * level.random.nextFloat(), 1.0f);

		//add potion to cauldron inventory
		addItem(itemEntity.getItem());

		//Since we added a potion, the cauldron must now appear with fluid
		BlockState mixerCauldronBlockState = level.getBlockState(this.getBlockPos()).setValue(BrewingCauldron.HAS_FLUID, true);
		level.setBlockAndUpdate(this.getBlockPos(), mixerCauldronBlockState);

		//To force re-rendering of the block tint
		forceChunkUpdate();
		itemEntity.remove(Entity.RemovalReason.DISCARDED);
		updateListeners();
	}


	/**
	 * Add an itemStack to the inventory of this cauldron
	 *
	 * @param itemStack the item to add
	 */
	private void addItem(@NotNull ItemStack itemStack) {
		assert level != null;
		//Check for incoherent state if inventory has changed since last Level load
		if (numberOfItems > inventory.size()) {
			countPotion(inventory);
		}

		//Prevent adding more item than the inventory can hold
		if (numberOfItems >= inventory.size()) {
			return;
		}

		inventory.set(numberOfItems, itemStack);
		numberOfItems++;
	}

	/**
	 * Force a chunk rerender by toggling a block state back and forth
	 * Used when an item is added to force rendering of the block tint
	 */
	private void forceChunkUpdate() {
		if (level == null) {
			return;
		}
		BlockState blockState = level.getBlockState(this.getBlockPos());

		boolean redrawValue = blockState.getValue(BrewingCauldron.REDRAW_DUMMY);

		BlockState newRedraw = blockState.setValue(BrewingCauldron.REDRAW_DUMMY, !redrawValue);
		level.setBlockAndUpdate(this.getBlockPos(), newRedraw);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		this.inventory = NonNullList.withSize(this.size(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.inventory);
		numberOfItems = nbt.getInt(POTION_MIXER_KEY);
		this.isBrewing = nbt.getBoolean(POTION_MIXER_KEY + "_isBrewing");
		this.canBrew = nbt.getBoolean(POTION_MIXER_KEY + "_canBrew");

		super.load(nbt);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag nbt) {
		ContainerHelper.saveAllItems(nbt, inventory);
		nbt.putInt(POTION_MIXER_KEY, numberOfItems);
		nbt.putBoolean(POTION_MIXER_KEY + "_isBrewing", isBrewing);
		nbt.putBoolean(POTION_MIXER_KEY + "_canBrew", canBrew);
		super.saveAdditional(nbt);
	}


	public int getWaterColor() {

		//Todo: read the color from the recipe

		var effects = getInventoryStatusEffectsInstances();
		if (effects.isEmpty()) {
			//return getColorFromInventoryItemsSprite();
			return Constants.WATER_TINT;
		} else {
			return PotionUtils.getColor(effects);
		}
	}

	private int getColorFromInventoryItemsSprite() {
		//TODO: Find a way to average the color of the item sprite
		return Constants.WATER_TINT;
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@NotNull
	protected List<MobEffectInstance> getInventoryStatusEffectsInstances() {
		List<MobEffectInstance> effects = new ArrayList<>();

		//Check for incoherent state if inventory has changed since last Level load
		if (this.numberOfItems > this.inventory.size()) {
			this.numberOfItems = this.size() - 1;
		}

		for (int i = 0; i < this.numberOfItems; i++) {
			ItemStack itemStack = inventory.get(i);
			//Since all potion derive from the same class, we only need to check for the potion item
			if (itemStack.getItem() instanceof PotionItem) {
				effects.addAll(PotionUtils.getMobEffects(itemStack));
			}
		}
		return effects;
	}

	public void setInventory(NonNullList<ItemStack> newInventory) {
		this.numberOfItems = 0;
		this.inventory = newInventory;
		countPotion(newInventory);

	}

	private void countPotion(NonNullList<ItemStack> newInventory) {
		for (ItemStack stack : newInventory) {
			if (!stack.is(Items.AIR)) {
				numberOfItems++;
			}
		}
	}
}
