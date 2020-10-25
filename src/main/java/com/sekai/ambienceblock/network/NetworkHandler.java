package com.sekai.ambienceblock.network;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.network.packets.PacketUpdateAmbienceTE;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(AmbienceMod.MOD_ID);
    public static final int PACKET_ID = 0;

    public static void register()
    {
        //NET_CHANNEL.registerMessage();
        //NET.registerMessage(0, PacketCapabilitiesWildCard.class, PacketCapabilitiesWildCard::encode, PacketCapabilitiesWildCard::decode, PacketCapabilitiesWildCard::handle);
        //NET.registerMessage(1, PacketDebug.class, PacketDebug::encode, PacketDebug::decode, PacketDebug::handle);
        //NET.registerMessage(0, PacketDebug.class, PacketDebug::encode, PacketDebug::decode, PacketDebug::handle);

        //NET.registerMessage(0, PacketUpdateAmbienceTE.class, PacketUpdateAmbienceTE::encode, PacketUpdateAmbienceTE::decode, PacketUpdateAmbienceTE::handle);
        NET.registerMessage(PacketUpdateAmbienceTE.MessageHandler.class, PacketUpdateAmbienceTE.class, PACKET_ID, Side.SERVER);

        //NET.registerMessage(2, PacketOpenAmbienceGui.class, PacketOpenAmbienceGui::encode, PacketOpenAmbienceGui::decode, PacketOpenAmbienceGui::handle);
    }
}
