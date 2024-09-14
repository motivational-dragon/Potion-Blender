package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.block.PotionBlenderBlock;
import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.datatype.PotionBlender;
import mod.motivationaldragon.potionblender.item.ModItem;
import mod.motivationaldragon.potionblender.recipes.PotionBlenderRecipes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ForgePotionBlender {

    public ForgePotionBlender(IEventBus bus) {

        PotionBlenderCommon.init();

        bind(Registries.BLOCK, PotionBlenderBlock::registerBlock, bus);
        bind(Registries.ITEM, PotionBlenderBlock::registerBlockItem,bus);
        bind(Registries.RECIPE_SERIALIZER, PotionBlenderRecipes::registerRecipeSerializer, bus);
        bind(Registries.RECIPE_TYPE, PotionBlenderRecipes::registerRecipeType, bus);
        bind(Registries.TRIGGER_TYPE, PotionBlenderCriterionTrigger::register, bus);
        bind(Registries.DATA_COMPONENT_TYPE, PotionBlender::registerDataComponentType, bus);
        ForgeBlockEntities.register(bus);
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            ModItem.registerFunctionalBlocksItems(event::accept);
        }
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source, IEventBus bus) {
        bus.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}