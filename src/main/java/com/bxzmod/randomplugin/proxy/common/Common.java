package com.bxzmod.randomplugin.proxy.common;

import com.bxzmod.randomplugin.block.BlockLoader;
import com.bxzmod.randomplugin.capability.CapabilityLoader;
import com.bxzmod.randomplugin.command.CommandLoader;
import com.bxzmod.randomplugin.config.ModConfig;
import com.bxzmod.randomplugin.creativetab.CreativeTabLoader;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.network.NetworkLoader;
import com.bxzmod.randomplugin.utils.ModModify;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;

public class Common
{
	public void preInit(FMLPreInitializationEvent event)
	{
		new CapabilityLoader();
		new CreativeTabLoader();
		new BlockLoader();
		new ItemLoader();
		new NetworkLoader();
	}

	public void init(FMLInitializationEvent event)
	{

	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public void loadComplete(FMLLoadCompleteEvent event)
	{
		ModConfig.initConfigLanguageKey();
		ModModify.modifyMaxStack();
		FluidRegistry.addBucketForFluid(FluidRegistry.WATER);
		FluidRegistry.addBucketForFluid(FluidRegistry.LAVA);
	}

	public void serverStarting(FMLServerStartingEvent event)
	{
		new CommandLoader(event);
		//new AdvancementLoader(event.getServer());
	}

	public void serverStarted(FMLServerStartedEvent event)
	{

	}

	public void serverStopping(FMLServerStoppingEvent event)
	{

	}
}
