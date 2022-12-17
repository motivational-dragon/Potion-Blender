package mod.motivationaldragon.potionblender.platform;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.platform.service.PlatformSpecificHelper;

import java.util.ServiceLoader;

public class Service {
    public static final PlatformSpecificHelper PLATFORM = load(PlatformSpecificHelper.class);

    public static <T extends PlatformSpecificHelper> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
