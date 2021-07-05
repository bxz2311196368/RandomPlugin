package com.bxzmod.randomplugin.proxy.client;

import com.bxzmod.randomplugin.config.ConfigLoader;
import com.bxzmod.randomplugin.hotkey.ClientKeyBind;
import com.bxzmod.randomplugin.proxy.common.Common;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Client extends Common
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		ClientKeyBind.registerKey();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
		//ClientCommandHandler.instance.registerCommand();
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		super.loadComplete(event);
		ConfigLoader.INSTANCE.initConfigComment();
		ConfigLoader.config.save();
	}
}
