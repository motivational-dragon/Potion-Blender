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
    private int configVersion;
    private int maxNbOfEffects;
    private int brewingTime;
    private Item normalPotionIngredient;
    private Item splashPotionIngredient;
    private Item lingeringPotionIngredient;


    public PotionBlenderConfig() {
          configVersion = -1;
          maxNbOfEffects = 3;
          brewingTime = 140;
          normalPotionIngredient = Items.NETHER_WART;
          splashPotionIngredient = Items.GUNPOWDER;
          lingeringPotionIngredient = Items.DRAGON_BREATH;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public void setBrewingTime(int brewingTime) {
        this.brewingTime = brewingTime;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public int getMaxNbOfEffects() {
        return maxNbOfEffects;
    }

    public void setMaxNbOfEffects(int maxNbOfEffects) {
        this.maxNbOfEffects = maxNbOfEffects;
    }

    public Item getNormalPotionIngredient() {
        return normalPotionIngredient;
    }

    public void setNormalPotionIngredient(Item normalPotionIngredient) {
        this.normalPotionIngredient = normalPotionIngredient;
    }

    public Item getSplashPotionIngredient() {
        return splashPotionIngredient;
    }

    public void setSplashPotionIngredient(Item splashPotionIngredient) {
        this.splashPotionIngredient = splashPotionIngredient;
    }

    public Item getLingeringPotionIngredient() {
        return lingeringPotionIngredient;
    }

    public void setLingeringPotionIngredient(Item lingeringPotionIngredient) {
        this.lingeringPotionIngredient = lingeringPotionIngredient;
    }
}
