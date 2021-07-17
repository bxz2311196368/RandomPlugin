package com.bxzmod.randomplugin;

import com.bxzmod.randomplugin.proxy.common.Common;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, guiFactory = ModInfo.GUI_FACTORY,
		acceptedMinecraftVersions = ModInfo.MINECRAFT_VERSION, dependencies = ModInfo.DEPENDENCIES)
public class RandomPlugin
{
	@SidedProxy(clientSide = "com.bxzmod.randomplugin.proxy.client.Client",
			serverSide = "com.bxzmod.randomplugin.proxy.common.Common")
	public static Common proxy;

	@Mod.Instance(ModInfo.MODID)
	public static RandomPlugin instance;

	public static Logger LOGGER;

	@EventHandler
	public void onConstruct(FMLConstructionEvent event)
	{
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER = event.getModLog();
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		proxy.loadComplete(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		proxy.serverStarting(event);
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		proxy.serverStarted(event);
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		proxy.serverStopping(event);
	}
}
