package mod.motivationaldragon.potionblender.datatype;

import com.mojang.serialization.Codec;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class PotionBlender {
	public static DataComponentType<Integer> potionTypeData = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build();

	public static void registerDataComponentType(BiConsumer<DataComponentType<?>, ResourceLocation> r) {
	    r.accept(potionTypeData, new ResourceLocation(Constants.MOD_ID, "potiontypedata"));
	}
}
