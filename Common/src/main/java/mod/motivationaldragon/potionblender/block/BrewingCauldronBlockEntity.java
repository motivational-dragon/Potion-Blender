package mod.motivationaldragon.potionblender.block;

import mod.motivationaldragon.potionblender.PotionBlender;
import mod.motivationaldragon.potionblender.config.ModConfig;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.networking.ModNetworkRegisterer;
import mod.motivationaldragon.potionblender.utils.ModUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BrewingCauldronBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {

    private static final String POTION_MIXER_KEY = PotionBlender.MODID+".PotionBlender";

    /**
     * Hard coded recipe for the cauldron.
     *
     */
    //TODO: might be a good idea to make this modifiable without recompiling the mod
    private static final Map<Item,Item> recipes = new HashMap<>(3);

    static {
        recipes.put(Items.NETHER_WART, ModItem.COMBINED_POTION);
        recipes.put(Items.GUNPOWDER, ModItem.SPLASH_COMBINED_POTION);
        recipes.put(Items.DRAGON_BREATH, ModItem.COMBINED_LINGERING_POTION);
    }

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(ModConfig.getConfig().max_effects, ItemStack.EMPTY);

    private int numberOfPotion = 0;


    public BrewingCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREWING_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }


    public int size() {
        return this.inventory.size();
    }

    private void emptyCauldron(@NotNull World world){
        inventory.clear();
        numberOfPotion = 0;

        BlockState hasFluid = world.getBlockState(this.getPos()).with(BrewingCauldron.HAS_FLUID, false);
        world.setBlockState(this.getPos(), hasFluid);

        updateListeners();
    }


    private void updateListeners() {
        this.markDirty();
        syncInventoryWithClient();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    /**
     * Send a packet to sync this block entity inventory with the client
     */
    private void syncInventoryWithClient() {
        assert world != null;
        if(world.isClient()) {return;}

        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(inventory.size());
        for (ItemStack stack : inventory) {
            data.writeItemStack(stack);
        }

        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModNetworkRegisterer.BREWING_CAULDRON_INV_SYNC, data);
        }
    }

    /**
     * Delegation from the onEntityLand method in the {@link net.minecraft.block.Block} class
     * Useful to access data such as inventory attached to the block entity from {@link net.minecraft.block.Block} callback
     *
     */
    public void onUseDelegate(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if ( numberOfPotion >= 1) {
            dropInventoryContent(world, pos);
        }
        ItemStack itemStackInMainHand = player.getHandItems().iterator().next();
        if(itemStackInMainHand.isOf(Items.ARROW) && itemStackInMainHand.getCount() >=16){
            craftCombinedPotion(itemStackInMainHand,world, pos);
        }
    }


    private void craftCombinedPotion(ItemStack recipeItemStack, World world, @NotNull BlockPos pos){
        if(world.isClient()) {return;}
        List<StatusEffectInstance> finalPotionStatusEffects = getInventoryStatusEffectsInstances();

        mergeCombinableEffects(finalPotionStatusEffects);

        //read recipe
        ItemStack potionToCraft = new ItemStack(recipes.get(recipeItemStack.getItem()));



        if(potionToCraft.isOf(ModItem.COMBINED_LINGERING_POTION)){
            finalPotionStatusEffects = handleLingeringEffect(finalPotionStatusEffects);
        }

        //create and drop the potion contained all the effect of the previous potion
        ItemStack potionItemStack = PotionUtil.setCustomPotionEffects(potionToCraft, finalPotionStatusEffects);
        assert potionItemStack.getNbt() != null;

        int color = PotionUtil.getColor(finalPotionStatusEffects);

        //Used to force tipped arrow color with the help of mixins
        potionItemStack.getNbt().putInt(PotionUtil.CUSTOM_POTION_COLOR_KEY, color);

        ItemScatterer.spawn(world, pos.getX(),pos.getY(), pos.getZ(), potionItemStack);

        //Drop all old potion bottle minus the one used for the new potion.
        ItemScatterer.spawn(world, pos.getX(),pos.getY(), pos.getZ(), new ItemStack(Items.GLASS_BOTTLE, numberOfPotion -1));

        world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0f, 1.0f);
        emptyCauldron(world);
    }

    /**
        Merge same effects in a potion. For instance poison 30sec and poison 40sec merge both effect into poison 70sec instead
     */
    private static List<StatusEffectInstance> mergeCombinableEffects(List<StatusEffectInstance> finalPotionStatusEffects) {

        Collection<StatusEffect> mergedStatusEffects = new HashSet<>();

        // The tricky part here is that a potion type share 1 statusEffectInstance, making it impossible to differentiate them
        // Therefore to test effectInstance1 == effectInstance2 we need range based for loop
        // This is otherwise a simple double iteration where we remember if we have already seen an effect type
        for(int i=0; i<finalPotionStatusEffects.size(); i++ ){
            StatusEffectInstance effectInstance1 = finalPotionStatusEffects.get(i);

            List<StatusEffectInstance> combinableEffects = new ArrayList<>();

            int totalDuration = effectInstance1.getDuration();
            //Effect are always combinable with themselves
            combinableEffects.add(effectInstance1);

            //This is the inversely proportional gain. First added potion has 1/2 the duration, 2nd 1/3, 3rd 1/4
            int potionDecay = 2;

            for(int j=0; j<finalPotionStatusEffects.size(); j++ ){
                StatusEffectInstance effectInstance2 = finalPotionStatusEffects.get(j);

                if(i!=j && !mergedStatusEffects.contains(effectInstance1.getEffectType())
                        && areEffectsDurationsAddable(effectInstance1, effectInstance2)){
                    totalDuration += (1.0d / potionDecay) * effectInstance2.getDuration();
                    potionDecay++;

                    combinableEffects.add(effectInstance2);
                }
            }

            mergedStatusEffects.add(effectInstance1.getEffectType());

            if(combinableEffects.size() > 1){
                StatusEffectInstance combinedEffect = ModUtils.copyEffectWithNewDuration(combinableEffects.get(0), totalDuration);
                finalPotionStatusEffects.removeAll(combinableEffects);
                finalPotionStatusEffects.add(combinedEffect);
            }
        }
        return finalPotionStatusEffects;
    }


    /** Handle lingering potion lesser duration and potency
     Quoting <a href="https://minecraft.fandom.com/wiki/Lingering_Potion">https://minecraft.fandom.com/wiki/Lingering_Potion</a>:
     "For finalPotionStatusEffects with duration, the duration applied by the cloud is 1⁄4 that of the corresponding potion."
     "For finalPotionStatusEffects without duration such as healing or harming, the potency of the effect is 1⁄2 that of the corresponding potion"
     **/
    @NotNull
    private static List<StatusEffectInstance> handleLingeringEffect(List<StatusEffectInstance> finalPotionStatusEffects) {
        List<StatusEffectInstance> lingeringEffects = new ArrayList<>(finalPotionStatusEffects.size());
        for (StatusEffectInstance effectInstance : finalPotionStatusEffects){
            if(effectInstance.getEffectType().isInstant()){
                //We are using the full constructor to copy effect witch is why the call is so long
                lingeringEffects.add(new StatusEffectInstance(effectInstance.getEffectType(), effectInstance.getDuration(),
                        Math.round(effectInstance.getAmplifier()*0.5f),
                        effectInstance.isAmbient(), effectInstance.shouldShowParticles(),effectInstance.shouldShowIcon()));
            } else {
                lingeringEffects.add(ModUtils.copyEffectWithNewDuration(effectInstance, Math.round(effectInstance.getDuration() * 0.25f)));
            }

        }
        finalPotionStatusEffects = lingeringEffects;
        return finalPotionStatusEffects;
    }

    private static boolean areEffectsDurationsAddable(StatusEffectInstance effectInstance1, StatusEffectInstance effectInstance2) {
        return effectInstance1.getEffectType() == effectInstance2.getEffectType() &&
                effectInstance1.getAmplifier() == effectInstance2.getAmplifier();
    }


    private void dropInventoryContent(@NotNull World world, BlockPos pos) {
        if(world.isClient()) {return;}
        world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0f, 1.0f);
        ItemScatterer.spawn(world, pos, getItems());
        emptyCauldron(world);
    }

    /**
     * Delegation from the onEntityLand method in the {@link net.minecraft.block.Block} class
     * Usefully to access data such as inventory attached to the block entity from {@link net.minecraft.block.Block} callback
     * @param entity the entity that landed on the attached block
     * @see net.minecraft.block.Block#onEntityLand(BlockView, Entity) for the full method documentation
     * and ommited parameter
     */
    public void onEntityLandDelegate(Entity entity) {
        assert world != null;
        if(world.isClient()) {return;}

        if (entity instanceof ItemEntity itemEntity){

            ItemStack itemStack = itemEntity.getStack();
            if(itemStack.isOf(Items.POTION) && numberOfPotion < inventory.size()){
                addItemToCauldron(entity, itemEntity);

            }
            if(recipes.containsKey(itemStack.getItem()) && numberOfPotion > 0) {
                craftCombinedPotion(itemStack,world, pos);
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }

    }


    private void addItemToCauldron(@NotNull Entity entity, @NotNull ItemEntity itemEntity) {

        assert world != null;
        if(world.isClient()) {return;}

        World world = entity.getWorld();

        world.playSound(null, this.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0f, 1.0f);

        //add potion to cauldron inventory
        addItem(itemEntity);

        //Since we added a potion, the cauldron must now appear with fluid
        BlockState mixerCauldronBlockState = world.getBlockState(this.getPos()).with(BrewingCauldron.HAS_FLUID, true);
        world.setBlockState(this.getPos(),mixerCauldronBlockState);

        //To force re-rendering of the block tint
        forceChunkUpdate();

        entity.remove(Entity.RemovalReason.DISCARDED);

        updateListeners();
    }
    private void addItem(@NotNull ItemEntity itemEntity) {
        assert world != null;
        //Check for incoherent state if inventory has changed since last world load
        if(numberOfPotion > inventory.size()){
            countPotion(inventory);}
        inventory.set(numberOfPotion, itemEntity.getStack());
        numberOfPotion++;
    }

    /**
     * Force a chunk rerender by toggling a block state back and forth
     * Only used when an item is added to force rendering of the block tint
     */
    private void forceChunkUpdate() {
        if(world == null){return;}
        BlockState blockState = world.getBlockState(this.getPos());

        boolean redrawValue = blockState.get(BrewingCauldron.REDRAW_DUMMY);

        BlockState newRedraw = blockState.with(BrewingCauldron.REDRAW_DUMMY, !redrawValue);
        world.setBlockState(this.getPos(), newRedraw);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true);
        nbtCompound.putInt(POTION_MIXER_KEY, numberOfPotion);
        return nbtCompound;
    }
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        numberOfPotion = nbt.getInt(POTION_MIXER_KEY);
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt,inventory);
        nbt.putInt(POTION_MIXER_KEY, numberOfPotion);
        super.writeNbt(nbt);
    }

    @Override
    public @Nullable Object getRenderAttachmentData() {
            return PotionUtil.getColor(getInventoryStatusEffectsInstances());
    }

    @NotNull
    private List<StatusEffectInstance> getInventoryStatusEffectsInstances() {
        List<StatusEffectInstance> effects = new ArrayList<>();

        //Check for incoherent state if inventory has changed since last world load
        if(this.numberOfPotion > this.inventory.size()){
            this.numberOfPotion = this.size()-1;
        }

        for (int i = 0; i< this.numberOfPotion; i++ ) {
            ItemStack potionItemStack = inventory.get(i);
            effects.addAll(PotionUtil.getPotionEffects(potionItemStack));
        }
        return effects;
    }

    public void setInventory(DefaultedList<ItemStack> newInventory) {
        this.numberOfPotion = 0;
        this.inventory = newInventory;
        countPotion(newInventory);
    }

    private void countPotion(DefaultedList<ItemStack> newInventory) {
        for (ItemStack stack : newInventory) {
            if(!stack.isOf(Items.AIR)){
                numberOfPotion++;
            }
        }
    }
}
