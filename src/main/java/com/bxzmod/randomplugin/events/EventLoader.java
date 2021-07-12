package com.bxzmod.randomplugin.events;

import com.bxzmod.randomplugin.config.ConfigEventHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class EventLoader
{
	public EventLoader()
	{

		FMLCommonHandler.instance().bus().register(MiningTickEventHandler.INSTANCE);
		FMLCommonHandler.instance().bus().register(ServerEventHandler.INSTANCE);
		FMLCommonHandler.instance().bus().register(ConfigEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ChromaticCraftDropHandler.INSTANCE);
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
			FMLCommonHandler.instance().bus().register(ClientEventHandler.INSTANCE);
		}
	}
}
