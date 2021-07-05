package com.bxzmod.randomplugin.proxy.common;

import com.bxzmod.randomplugin.config.ConfigLoader;
import com.bxzmod.randomplugin.creativetabs.CreativeTabsLoader;
import com.bxzmod.randomplugin.events.EventLoader;
import com.bxzmod.randomplugin.hotkey.HotKeys;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.network.NetworkLoader;
import com.bxzmod.randomplugin.recipes.CraftingLoader;
import cpw.mods.fml.common.event.*;

public class Common
{
	public void construct(FMLConstructionEvent event)
	{
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		new ConfigLoader(event);
		new CreativeTabsLoader(event);
		new ItemLoader(event);
		new NetworkLoader();
	}

	public void init(FMLInitializationEvent event)
	{
		new CraftingLoader(event);
		new EventLoader();
		new HotKeys();
	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public void loadComplete(FMLLoadCompleteEvent event)
	{
	}

}
