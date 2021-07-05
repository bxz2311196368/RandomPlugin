package com.bxzmod.randomplugin;

import com.bxzmod.randomplugin.command.CommandLoader;
import com.bxzmod.randomplugin.proxy.common.Common;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.mojang.util.UUIDTypeAdapter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Info.MODID, name = Info.MODNAME, dependencies = Info.dependencies, version = Info.VERSION,
		acceptedMinecraftVersions = Info.acceptedMinecraftVersions,
		guiFactory = "com.bxzmod.randomplugin.config.ModGuiFactory")
public class Main
{
	public static final Logger LOGGER = LogManager.getLogger(Info.MODID);

	@SidedProxy(clientSide = "com.bxzmod.randomplugin.proxy.client.Client",
			serverSide = "com.bxzmod.randomplugin.proxy.common.Common")
	public static Common proxy;

	@Instance(Info.MODID)
	public static Main instance;

	@EventHandler
	public void construct(FMLConstructionEvent event)
	{
		proxy.construct(event);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
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
	public void onServerStarted(FMLServerStartedEvent event)
	{
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			ModPlayerData
					.initPlayerFlag(UUIDTypeAdapter.fromString(Minecraft.getMinecraft().getSession().getPlayerID()));
		}
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		new CommandLoader(event);
	}

	@EventHandler
	public void onServerStop(FMLServerStoppedEvent event)
	{
		ModPlayerData.ModPlayerClientData.reset();
		if (FMLCommonHandler.instance().getSide().isClient())
			ModPlayerData
					.initPlayerFlag(UUIDTypeAdapter.fromString(Minecraft.getMinecraft().getSession().getPlayerID()));
	}
}
