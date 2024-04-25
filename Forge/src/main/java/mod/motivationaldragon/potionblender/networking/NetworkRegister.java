package mod.motivationaldragon.potionblender.networking;

import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkRegister {

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MOD_ID, "main"),
            () -> "0", "0"::equals, "0"::equals
    );


    public static void register(){
        int i = 0;
        INSTANCE.registerMessage(i++, BrewingCauldronInvSyncS2CPacket.class, BrewingCauldronInvSyncS2CPacket::encode
        , BrewingCauldronInvSyncS2CPacket::decode, makeClientHandler(BrewingCauldronInvSyncS2CPacket::handle));
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }

}
