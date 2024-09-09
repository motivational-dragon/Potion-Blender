package mod.motivationaldragon.potionblender;

import com.mojang.serialization.Codec;
import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.config.ConfigController;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

public class PotionBlenderCommon {


    public static DataComponentType<Integer> potionTypeData;

    public static void init() {
        ConfigController.init();
        PotionBlenderCriterionTrigger.init();
        Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                new ResourceLocation(Constants.MOD_ID, "potiontypedata"),
                DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    }

}