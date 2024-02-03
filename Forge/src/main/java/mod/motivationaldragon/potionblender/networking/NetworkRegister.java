package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkRegister {

    public static final SimpleChannel INSTANCE = ChannelBuilder.named(
            new ResourceLocation(Constants.MOD_ID, "main")
    ).han;


    public static void register(){
        INSTANCE.messageBuilder(BrewingCauldronInvSyncS2CPacket.class)
                .encoder(BrewingCauldronInvSyncS2CPacket::encode)
                .decoder(BrewingCauldronInvSyncS2CPacket::decode);
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }

}
