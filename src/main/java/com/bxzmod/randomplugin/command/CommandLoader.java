package com.bxzmod.randomplugin.command;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandLoader
{

	public CommandLoader(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new ModCommand());
		event.registerServerCommand(new TPACommand());
		event.registerServerCommand(new TPDCommand());
	}

}
