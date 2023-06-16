package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.networking.NetworkRegister;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderSpecialRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgePotionBlender {


    public ForgePotionBlender() {

        PotionBlenderCommon.init();

        NetworkRegister.register();
        bind(Registries.BLOCK, PotionBlenderBlock::registerBlock);
        bind(Registries.ITEM, PotionBlenderBlock::registerBlockItem);
        bind(Registries.ITEM, ModItem::register);
        bind(Registries.RECIPE_SERIALIZER, PotionBlenderSpecialRecipeSerializer::register);
        ForgeBlockEntities.register();

    }

    @SubscribeEvent
    public static void buildContents(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            ModItem.registerFunctionalBlocksItems(event::accept);
        }
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}