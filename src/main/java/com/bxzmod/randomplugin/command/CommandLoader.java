package com.bxzmod.randomplugin.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandLoader
{
	public CommandLoader(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new ServerUUIDPrinter());
		event.registerServerCommand(new ServerShowHand());
	}
}
