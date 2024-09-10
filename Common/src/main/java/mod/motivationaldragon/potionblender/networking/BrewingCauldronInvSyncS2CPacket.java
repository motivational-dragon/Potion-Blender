package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BrewingCauldronInvSyncS2CPacket(NonNullList<ItemStack> inv,
                                              BlockPos containerLocation) implements CustomPacketPayload {

	public static final ResourceLocation channel = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_sync_inv");

	public static final StreamCodec<RegistryFriendlyByteBuf, BrewingCauldronInvSyncS2CPacket> STREAM_CODEC = CustomPacketPayload.codec(BrewingCauldronInvSyncS2CPacket::write, BrewingCauldronInvSyncS2CPacket::new);
	public static final CustomPacketPayload.Type<BrewingCauldronInvSyncS2CPacket> packetType = CustomPacketPayload.createType(channel.toString());


	@Override
	public Type<BrewingCauldronInvSyncS2CPacket> type() {
		return packetType;
	}


	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(inv.size());
		for (ItemStack item : inv) {
			ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, item);
			}
		buf.writeBlockPos(containerLocation);
	}

	public static void setCauldronInventory(BrewingCauldronInvSyncS2CPacket payload) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null || !level.hasChunkAt(payload.containerLocation)) return;
		if (level.getBlockEntity(payload.containerLocation) instanceof BrewingCauldronBlockEntity blockEntity) {
			blockEntity.setInventory(payload.inv);
		}
	}


	public BrewingCauldronInvSyncS2CPacket(RegistryFriendlyByteBuf buf) {
		this(readInventory(buf),buf.readBlockPos());
	}

	private static NonNullList<ItemStack> readInventory(RegistryFriendlyByteBuf buf) {
		int size = buf.readInt();
		NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
		for (int i = 0; i < size; i++) {
			list.set(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
		}
		return list;
	}

}
