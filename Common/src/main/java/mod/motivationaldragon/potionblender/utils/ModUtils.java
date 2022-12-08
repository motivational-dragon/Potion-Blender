package mod.motivationaldragon.potionblender.utils;

import net.minecraft.world.effect.MobEffectInstance;
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
}
