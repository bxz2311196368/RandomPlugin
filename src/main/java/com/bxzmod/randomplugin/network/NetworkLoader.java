package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.Info;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkLoader
{
	public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Info.MODID);

	private static int nextID = 0;

	public NetworkLoader()
	{
		instance.registerMessage(PlayerKeyPressPacket.SendToServerHandler.class, PlayerKeyPressPacket.class, nextID++,
				Side.SERVER);
		instance.registerMessage(PlayerDataPacket.SendToServerHandler.class, PlayerDataPacket.class, nextID++,
				Side.SERVER);
		instance.registerMessage(ConfigSyncPacket.SendToServerHandler.class, ConfigSyncPacket.class, nextID++,
				Side.SERVER);
		instance.registerMessage(ConfigSyncPacket.SendToClientHandler.class, ConfigSyncPacket.class, nextID++,
				Side.CLIENT);
	}
}
