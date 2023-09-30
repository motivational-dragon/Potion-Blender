package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.config.ConfigController;

public class PotionBlenderCommon {


    public static void init() {
        ConfigController.init();
        PotionBlenderCriterionTrigger.init();
    }

}