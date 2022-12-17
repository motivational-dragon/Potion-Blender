package mod.motivationaldragon.potionblender.blockentity;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.block.ModBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBlockEntities {

	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

	public static final RegistryObject<BlockEntityType<ForgeBrewingCauldron>> BREWING_CAULDRON_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("potion_mixer", () ->
					BlockEntityType.Builder.of(ForgeBrewingCauldron::new, ModBlock.BREWING_CAULDRON_BLOCK).build(null));

	public static void register(){
		BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
