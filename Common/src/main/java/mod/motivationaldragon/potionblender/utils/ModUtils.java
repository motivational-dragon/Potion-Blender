package mod.motivationaldragon.potionblender.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public class ModUtils {

    ModUtils(){
        throw new IllegalStateException("Utility class");
    }

    @NotNull
    public static MobEffectInstance copyEffectWithNewDuration(MobEffectInstance effectInstance, int duration) {
        return new MobEffectInstance(effectInstance.getEffect(),
                duration,
                effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
    }

    public static boolean isTagValueTrue(@NotNull CompoundTag nbtCompound, String NbtKey) {
        return nbtCompound.contains(NbtKey) && nbtCompound.getBoolean(NbtKey);
    }
    public static float lerp(float min, float max, float f)
    {
        return (float) ((min * (1.0 - f)) + (max * f));
    }


    public static boolean isCombinedLingeringPotion(ItemStack itemStack) {
            return  itemStack.get(DataComponents.CUSTOM_DATA).copyTag().contains(ModNBTKey.IS_COMBINED_LINGERING_POTION);
    }

    public static boolean isACombinedPotion(ItemStack itemStack){

        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if(customData == null){
            CompoundTag tag = customData.copyTag();
            return tag.contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)||
                    tag.contains(ModNBTKey.IS_COMBINED_POTION)||
                    tag.contains(ModNBTKey.IS_COMBINED_SPLASH_POTION)||
                    tag.contains(ModNBTKey.IS_COMBINED_LINGERING_POTION);
        }
        return false;
    }
}
