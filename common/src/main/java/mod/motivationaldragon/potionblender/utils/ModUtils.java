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

    //Copied from the jave awt library
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }
}
