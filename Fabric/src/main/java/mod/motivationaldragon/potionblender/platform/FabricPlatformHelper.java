package mod.motivationaldragon.potionblender.platform;

import mod.motivationaldragon.potionblender.blockentity.FabricBrewingCauldronBlockEntity;
import mod.motivationaldragon.potionblender.blockentity.FabricBlockEntities;
import mod.motivationaldragon.potionblender.platform.service.PlatformSpecificHelper;

public class FabricPlatformHelper extends PlatformSpecificHelper {

    public FabricPlatformHelper() {
        super(FabricBlockEntities.BREWING_CAULDRON_BLOCK_ENTITY,
                FabricBrewingCauldronBlockEntity::new, null);
    }

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public boolean isForge() {
        return false;
    }

}
