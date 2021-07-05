package com.bxzmod.randomplugin.creativetabs;

import com.bxzmod.randomplugin.Info;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CreativeTabsLoader
{
	public static CreativeTabsRandomPlugin modTab;

	public CreativeTabsLoader(FMLPreInitializationEvent event)
	{
		modTab = new CreativeTabsRandomPlugin(Info.MODID);
	}

}
