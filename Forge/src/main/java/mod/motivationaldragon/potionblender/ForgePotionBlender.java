package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.ModBlock;
import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import mod.motivationaldragon.potionblender.recipes.ModSpecialRecipeSerializer;
import mod.motivationaldragon.potionblender.platform.service.PlatformSpecificHelper;
import mod.motivationaldragon.potionblender.platform.Service;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class ForgePotionBlender {


    public ForgePotionBlender() {

        PotionBlenderCommon.init();

        NetworkRegister.register();
        bind(Registry.BLOCK_REGISTRY, ModBlock::registerBlock);
        bind(Registry.ITEM_REGISTRY, ModItem::register);
        bind(Registry.ITEM_REGISTRY, ModBlock::registerBlockItem);
        bind(Registry.RECIPE_SERIALIZER_REGISTRY, ModSpecialRecipeSerializer::register);
        ForgeBlockEntities.register();
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}