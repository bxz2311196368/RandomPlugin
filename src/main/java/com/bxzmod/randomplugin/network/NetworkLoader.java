package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.ModInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkLoader
{
	public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);

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
