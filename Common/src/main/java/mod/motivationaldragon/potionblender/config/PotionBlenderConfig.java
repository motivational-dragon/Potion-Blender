package mod.motivationaldragon.potionblender.config;


/**
 * POJO Representing all configuration parameter.
 */
public class PotionBlenderConfig {

    /**
     * Set to -1 by default to represent a missing config version
     */
    private int configVersion = -1;
    private int cauldron_inventory_size = 4;

    public int getCauldron_inventory_size() {
        return cauldron_inventory_size;
    }

    public void setCauldron_inventory_size(int cauldron_inventory_size) {
        this.cauldron_inventory_size = cauldron_inventory_size;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }


}
