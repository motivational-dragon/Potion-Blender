package mod.motivationaldragon.potionblender.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PotionEffectMerger {
	/**
	 * Merge same effects in a potion. For instance poison 30sec and poison 40sec merge both effect into poison 70sec instead
	 * @param effectInstances the list of potion effects
	 * @param decayRate This is the inversely proportional gain. First added potion has 1/2 the duration, 2nd 1/3, 3rd 1/4
	 */
	public static List<MobEffectInstance> mergeCombinableEffects(List<MobEffectInstance> effectInstances, double decayRate) {

	    Collection<MobEffect> processedEffect = new HashSet<>();
	    List<MobEffectInstance> finalPotionStatusEffects = new ArrayList<>(effectInstances);

		//Generate all pair of items from the given mob effects and try to merge them
	    for(int i=0; i<finalPotionStatusEffects.size(); i++ ){
	        MobEffectInstance effectInstance1 = finalPotionStatusEffects.get(i);

	        List<MobEffectInstance> combinableEffects = new ArrayList<>();

	        int totalDuration = effectInstance1.getDuration();
	        //Effect are always combinable with themselves, so we add the current effect to the list
	        combinableEffects.add(effectInstance1);

	        for(int j=0; j<finalPotionStatusEffects.size(); j++ ){
	            MobEffectInstance effectInstance2 = finalPotionStatusEffects.get(j);

	            if(i!=j && !processedEffect.contains(effectInstance1.getEffect().value()) && areEffectsDurationsAddable(effectInstance1, effectInstance2)){
	                totalDuration += (int) ((1.0d / decayRate) * effectInstance2.getDuration());
		            decayRate++;
	                combinableEffects.add(effectInstance2);
	            }
	        }

	        processedEffect.add(effectInstance1.getEffect().value());

			//If there is multiple instance for this effect, merge them into the result
	        if(combinableEffects.size() > 1){
	            MobEffectInstance combinedEffect = ModUtils.copyEffectWithNewDuration(combinableEffects.getFirst(), totalDuration);
	            finalPotionStatusEffects.removeAll(combinableEffects);
	            finalPotionStatusEffects.add(combinedEffect);
	        }
	    }
	    return finalPotionStatusEffects;
	}

	/** Handle lingering potion lesser duration and potency combination
	 Quoting <a href="https://minecraft.fandom.com/wiki/Lingering_Potion">https://minecraft.fandom.com/wiki/Lingering_Potion</a>:
	 "For finalPotionStatusEffects with duration, the duration applied by the cloud is 1⁄4 that of the corresponding potion."
	 "For finalPotionStatusEffects without duration such as healing or harming, the potency of the effect is 1⁄2 that of the corresponding potion"
	 **/
	@NotNull
	public static List<MobEffectInstance> ApplyLingeringPotionDurationAndEffects(List<MobEffectInstance> finalPotionStatusEffects) {
	    List<MobEffectInstance> lingeringEffects = new ArrayList<>(finalPotionStatusEffects.size());
	    for (MobEffectInstance effectInstance : finalPotionStatusEffects){
	        if(effectInstance.getEffect().value().isInstantenous()){
	            //We are using the full constructor to copy effect witch is why the call is so long
	            lingeringEffects.add(new MobEffectInstance(effectInstance.getEffect(), effectInstance.getDuration(),
	                    Math.round(effectInstance.getAmplifier()*0.5f),
	                    effectInstance.isAmbient(), effectInstance.isVisible(),effectInstance.showIcon()));
	        } else {
	            lingeringEffects.add(ModUtils.copyEffectWithNewDuration(effectInstance, Math.round(effectInstance.getDuration() * 0.25f)));
	        }

	    }
	    return lingeringEffects;
	}

	/**
	 * Check if two potion effects are combinable. Two potion effects are combinable if they have the same effect and amplifier
	 * @param effectInstance1 the first potion effect
	 * @param effectInstance2 the second potion effect
	 * @return true if the two potion effects are combinable
	 */
	private static boolean areEffectsDurationsAddable(MobEffectInstance effectInstance1, MobEffectInstance effectInstance2) {
		return effectInstance1.getEffect() == effectInstance2.getEffect() &&
				effectInstance1.getAmplifier() == effectInstance2.getAmplifier();
	}

	/**
	 * Check if the potion would ignore instant potion effects because instant potion effects do not stack
	 * @param potion the potion to check
	 * @param cauldronInventoryEffects the list of effects in the cauldron
	 * @return true if the potion would ignore instant potion effects
	 */
	public static boolean wouldIgnoreInstantPotion(ItemStack potion, List<MobEffectInstance> cauldronInventoryEffects) {

		PotionContents potionContents = potion.get(DataComponents.POTION_CONTENTS);
		if(potionContents == null){return false;}
		List<MobEffectInstance> effectInstances = potionContents.customEffects();
	    effectInstances = effectInstances
	            .stream()
	            .filter(e->e.getEffect().value().isInstantenous()).toList();
	    return cauldronInventoryEffects.stream().anyMatch(effectInstances::contains);
	}
}
