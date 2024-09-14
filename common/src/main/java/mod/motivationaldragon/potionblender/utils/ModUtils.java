package mod.motivationaldragon.potionblender.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static mod.motivationaldragon.potionblender.datatype.PotionBlender.potionTypeData;

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
        PotionType potionType = PotionType.codeToPotionType.get(itemStack.get(potionTypeData));
        return potionType == PotionType.LINGERING;
    }

    public static boolean isACombinedPotion(ItemStack itemStack){
        return PotionType.codeToPotionType.get(itemStack.get(potionTypeData)) != null;
    }
}
