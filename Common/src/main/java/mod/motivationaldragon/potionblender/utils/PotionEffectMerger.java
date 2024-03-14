package mod.motivationaldragon.potionblender.utils;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PotionEffectMerger {
	/**
	 * Merge same effects in a potion. For instance poison 30sec and poison 40sec merge both effect into poison 70sec instead
	 * @param effectInstances the list of potion effects
	 * @param decayRate the decay rate of same effects
	 */
	public static List<MobEffectInstance> mergeCombinableEffects(List<MobEffectInstance> effectInstances, int decayRate) {

	    Collection<MobEffect> mergedStatusEffects = new HashSet<>();
	    List<MobEffectInstance> finalPotionStatusEffects = new ArrayList<>(effectInstances);

	    // The tricky part here is that a potion type share 1 MobEffectInstance, making it impossible to differentiate them using ==
	    // Therefore to test if effectInstance1 == effectInstance2 we use a range based for loop and test indices
	    // This is otherwise a simple double iteration where we remember if we have already seen an effect type
	    for(int i=0; i<finalPotionStatusEffects.size(); i++ ){
	        MobEffectInstance effectInstance1 = finalPotionStatusEffects.get(i);

	        List<MobEffectInstance> combinableEffects = new ArrayList<>();

	        int totalDuration = effectInstance1.getDuration();
	        //Effect are always combinable with themselves
	        combinableEffects.add(effectInstance1);

	        //This is the inversely proportional gain. First added potion has 1/2 the duration, 2nd 1/3, 3rd 1/4
	        //decay = 1/potionDecay


	        for(int j=0; j<finalPotionStatusEffects.size(); j++ ){
	            MobEffectInstance effectInstance2 = finalPotionStatusEffects.get(j);

	            if(i!=j && !mergedStatusEffects.contains(effectInstance1.getEffect()) && areEffectsDurationsAddable(effectInstance1, effectInstance2)){
	                totalDuration += (int) ((1.0d / decayRate) * effectInstance2.getDuration());
		            decayRate++;
	                combinableEffects.add(effectInstance2);
	            }
	        }

	        mergedStatusEffects.add(effectInstance1.getEffect());

	        if(combinableEffects.size() > 1){
	            MobEffectInstance combinedEffect = ModUtils.copyEffectWithNewDuration(combinableEffects.get(0), totalDuration);
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
	public static List<MobEffectInstance> mergeLingeringPotionEffects(List<MobEffectInstance> finalPotionStatusEffects) {
	    List<MobEffectInstance> lingeringEffects = new ArrayList<>(finalPotionStatusEffects.size());
	    for (MobEffectInstance effectInstance : finalPotionStatusEffects){
	        if(effectInstance.getEffect().isInstantenous()){
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

	private static boolean areEffectsDurationsAddable(MobEffectInstance effectInstance1, MobEffectInstance effectInstance2) {
		return effectInstance1.getEffect() == effectInstance2.getEffect() &&
				effectInstance1.getAmplifier() == effectInstance2.getAmplifier();
	}

	public static boolean wouldIgnoreInstantPotion(ItemStack potion, List<MobEffectInstance> cauldronInventoryEffects) {
	    List<MobEffectInstance> effectInstances = PotionUtils.getMobEffects(potion);
	    effectInstances = effectInstances
	            .stream()
	            .filter(e->e.getEffect().isInstantenous()).toList();
	    return cauldronInventoryEffects.stream().anyMatch(effectInstances::contains);
	}
}
