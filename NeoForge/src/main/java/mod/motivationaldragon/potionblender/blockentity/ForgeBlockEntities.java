package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForgeBlockEntities {

	private static final net.neoforged.neoforge.registries.DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrewingCauldronBlockEntity>> BREWING_CAULDRON_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("brewing_cauldron", () ->
					BlockEntityType.Builder.of(BrewingCauldronBlockEntity::new, PotionBlenderBlock.BREWING_CAULDRON_BLOCK).build(null));

	public static void register(IEventBus modBus){
		BLOCK_ENTITIES.register(modBus);
	}
}
