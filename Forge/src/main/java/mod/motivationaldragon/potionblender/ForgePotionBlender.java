package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import mod.motivationaldragon.potionblender.recipes.ModSpecialRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class ForgePotionBlender {


    public ForgePotionBlender() {

        PotionBlenderCommon.init();

        NetworkRegister.register();
        bind(ForgeRegistries.BLOCKS, ModBlock::registerBlock);
        bind(ForgeRegistries.ITEMS, ModItem::register);
        bind(ForgeRegistries.ITEMS, ModBlock::registerBlockItem);
        bind(ForgeRegistries.RECIPE_SERIALIZERS, ModSpecialRecipeSerializer::register);
        ForgeBlockEntities.register();
    }

    private static <T extends IForgeRegistryEntry<T>> void bind(IForgeRegistry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(registry.getRegistrySuperType(),
                (RegistryEvent.Register<T> event) -> {
            if (registry.equals(event.getRegistry())) {
                source.accept((t, rl) ->{
                    t.setRegistryName(rl);
                    event.getRegistry().register(t);
                });
            }
        });
    }
}