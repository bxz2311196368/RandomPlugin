package com.bxzmod.randomplugin.event;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.RandomPlugin;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents
{
	@SubscribeEvent
	public static void onServerAboutToStart(FMLServerAboutToStartEvent event)
	{

	}

	@SubscribeEvent
	public static void onServerStarting(FMLServerStartingEvent event)
	{
		// do something when the server starts
		RandomPlugin.LOGGER.info("HELLO from server starting");
	}

	@SubscribeEvent
	public static void onServerStarted(FMLServerStartedEvent event)
	{

	}

	@SubscribeEvent
	public static void onServerStopping(FMLServerStoppingEvent event)
	{

	}

	@SubscribeEvent
	public static void onServerStopped(FMLServerStoppedEvent event)
	{

	}
}
