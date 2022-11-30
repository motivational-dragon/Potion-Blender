package mod.motivationaldragon.potionblender.utils;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.NotNull;

public class ModUtils {

    ModUtils(){
        throw new IllegalStateException("Utility class");
    }

    @NotNull
    public static StatusEffectInstance copyEffectWithNewDuration(StatusEffectInstance effectInstance, int duration) {
        return new StatusEffectInstance(effectInstance.getEffectType(),
                duration,
                effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.shouldShowParticles(), effectInstance.shouldShowIcon());
    }
}
