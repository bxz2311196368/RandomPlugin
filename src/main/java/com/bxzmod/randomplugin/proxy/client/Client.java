package com.bxzmod.randomplugin.proxy.client;

import com.bxzmod.randomplugin.command.ClientShowHand;
import com.bxzmod.randomplugin.command.ClientShowUUID;
import com.bxzmod.randomplugin.hotkey.ClientKeyBind;
import com.bxzmod.randomplugin.proxy.common.Common;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
		ClientCommandHandler.instance.registerCommand(new ClientShowUUID());
		ClientCommandHandler.instance.registerCommand(new ClientShowHand());
		ClientKeyBind.registerKey();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		super.loadComplete(event);
	}
}
