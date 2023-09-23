package mod.motivationaldragon.potionblender.platform;

import mod.motivationaldragon.potionblender.blockentity.ForgeBlockEntities;
import mod.motivationaldragon.potionblender.blockentity.ForgeBrewingCauldron;
import mod.motivationaldragon.potionblender.platform.service.PlatformSpecificHelper;

public class ForgePlatformHelper extends PlatformSpecificHelper {

    public ForgePlatformHelper() {
        super(ForgeBlockEntities.BREWING_CAULDRON_BLOCK_ENTITY.get(), ForgeBrewingCauldron::new, null);
    }

    @Override
    public boolean isFabric() {
        return false;
    }

    @Override
    public boolean isForge() {
        return true;
    }
}
