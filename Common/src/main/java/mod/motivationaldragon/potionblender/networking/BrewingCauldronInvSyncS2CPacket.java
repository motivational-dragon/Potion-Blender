package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BrewingCauldronInvSyncS2CPacket(NonNullList<ItemStack> inv, BlockPos containerLocation) implements PotionBlenderPacket {

	public static final ResourceLocation fabricChannel = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_sync_inv");

	public void encode(FriendlyByteBuf buf){
		buf.writeInt(inv.size());
		for(ItemStack item : inv) {
			buf.writeItem(item);
		}
		buf.writeBlockPos(containerLocation);
	}

	public static void handle(BrewingCauldronInvSyncS2CPacket packet) {

		BlockPos containerLocation = packet.containerLocation();
		ClientLevel level = Minecraft.getInstance().level;

		if (level == null || level.hasChunkAt(containerLocation)) return;

		if(level.getBlockEntity(containerLocation) instanceof BrewingCauldronBlockEntity blockEntity) {
			blockEntity.setInventory(packet.inv);
		}
	}

	@Override
	public ResourceLocation getFabricId() {
		return fabricChannel;
	}

	public static BrewingCauldronInvSyncS2CPacket decode(FriendlyByteBuf buf) {

		int size = buf.readInt();
		NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
		for(int i = 0; i < size; i++) {
			list.set(i, buf.readItem());
		}

		return new BrewingCauldronInvSyncS2CPacket(
				list,
				buf.readBlockPos()
		);
	}
}
