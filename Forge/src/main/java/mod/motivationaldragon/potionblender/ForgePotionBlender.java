package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderSpecialRecipeSerializer;
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
        bind(Registry.BLOCK_REGISTRY, PotionBlenderBlock::registerBlock);
        bind(Registry.ITEM_REGISTRY, ModItem::register);
        bind(Registry.ITEM_REGISTRY, PotionBlenderBlock::registerBlockItem);
        bind(Registry.RECIPE_SERIALIZER_REGISTRY, PotionBlenderSpecialRecipeSerializer::register);
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