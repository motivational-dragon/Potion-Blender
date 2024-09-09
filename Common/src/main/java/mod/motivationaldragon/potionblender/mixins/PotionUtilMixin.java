package mod.motivationaldragon.potionblender.mixins;


/*@Mixin(PotionUtils.class)
public abstract class PotionContentMixin {

    //Color a normal tipped arrow if used as a combined arrow
    @Inject(method = "getColor*", at = @At("RETURN"), cancellable = true)
    private static void getColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        CompoundTag nbtCompound = stack.getTag();
        if (nbtCompound != null && (ModUtils.isTagValueTrue(nbtCompound,ModNBTKey.FORCE_COLOR_RENDERING_KEY))){
                cir.setReturnValue(PotionUtils.getColor(PotionUtils.getMobEffects(stack)));

        }
    }

}*/
