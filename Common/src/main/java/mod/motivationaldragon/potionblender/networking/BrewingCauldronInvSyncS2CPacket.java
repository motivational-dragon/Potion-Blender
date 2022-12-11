package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import mod.motivationaldragon.potionblender.blockentities.BrewingCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BrewingCauldronInvSyncS2CPacket(NonNullList<ItemStack> inv, BlockPos containerLocation) implements PotionBlenderPacket {

    public static final ResourceLocation fabricChannel = new ResourceLocation(Constants.MOD_ID, "brewing_cauldron_sync_inv");

    public void encode(FriendlyByteBuf buf){
        for(ItemStack item : inv) {
            buf.writeItem(item);
            buf.writeBlockPos(containerLocation);
        }
    }

    public static void handle(FriendlyByteBuf buf) {
        int size = buf.readInt();
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            list.set(i, buf.readItem());
        }
        BlockPos position = buf.readBlockPos();

        if (Minecraft.getInstance().level == null) return;
        if(Minecraft.getInstance().level.getBlockEntity(position) instanceof BrewingCauldronBlockEntity blockEntity) {
            blockEntity.setInventory(list);
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return fabricChannel;
    }

}
