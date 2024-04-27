package mod.motivationaldragon.potionblender.config;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * POJO Representing all configuration parameter.
 */
public class PotionBlenderConfig {

    /**
     * Set to -1 by default to represent a missing config version
     */
    private int configVersion = -1;
    private int cauldronInventorySize = 4;

    private Item[] litItems = {Items.FLINT_AND_STEEL, Items.FIRE_CHARGE};

    // Shovel check is hardcoded with the shovel class to handle modded shovels
    private Item[] dowsingItems = {Items.WATER_BUCKET};

    public int getCauldronInventorySize() {
        return cauldronInventorySize;
    }
    public void setCauldronInventorySize(int cauldronInventorySize) {
        this.cauldronInventorySize = cauldronInventorySize;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public Item[] getLitItems() {
        return litItems;
    }

    public void setLitItems(Item[] litItems) {
        this.litItems = litItems;
    }

    public Item[] getDowsingItems() {
        return dowsingItems;
    }

    public void setDowsingItems(Item[] dowsingItems) {
        this.dowsingItems = dowsingItems;
    }
}
