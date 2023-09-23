package mod.motivationaldragon.potionblender.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
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

    public static boolean isTippedArrowCombined(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            assert itemStack.getTag() != null;
            return itemStack.getTag().contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY);
        }
        return false;
    }

    public static boolean isCombinedPotion(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            assert itemStack.getTag() != null;
            return itemStack.getTag().contains(ModNBTKey.IS_COMBINED_POTION);
        }
        return false;
    }

    public static boolean isCombinedSplashPotion(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            assert itemStack.getTag() != null;
            return itemStack.getTag().contains(ModNBTKey.IS_COMBINED_SPLASH_POTION);
        }
        return false;
    }

    public static boolean isCombinedLingeringPotion(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            assert itemStack.getTag() != null;
            return itemStack.getTag().contains(ModNBTKey.IS_COMBINED_LINGERING_POTION);
        }
        return false;
    }

    public static boolean isACombinedPotion(ItemStack itemStack){
        boolean isTippedArrow = false;
        if(itemStack.hasTag()){
            assert itemStack.getTag() != null;
            return itemStack.getTag().contains(ModNBTKey.IS_TIPPED_ARROW_COMBINED_KEY)||
                    itemStack.getTag().contains(ModNBTKey.IS_COMBINED_POTION)||
                    itemStack.getTag().contains(ModNBTKey.IS_COMBINED_SPLASH_POTION)||
                    itemStack.getTag().contains(ModNBTKey.IS_COMBINED_LINGERING_POTION);
        }
        return false;
    }
}
