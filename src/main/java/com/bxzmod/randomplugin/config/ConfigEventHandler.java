package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.Info;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigEventHandler
{
	public static ConfigEventHandler INSTANCE = new ConfigEventHandler();

	private ConfigEventHandler()
	{

	}

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equals(Info.MODID))
		{
			ConfigLoader.INSTANCE.reload();
		}
	}
}
