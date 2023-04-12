package mod.motivationaldragon.potionblender;

import mod.motivationaldragon.potionblender.advancements.PotionBlenderCriterionTrigger;
import mod.motivationaldragon.potionblender.config.PotionBlender;

public class PotionBlenderCommon {


    public static void init() {
        PotionBlender.init();
        PotionBlenderCriterionTrigger.init();
    }

}