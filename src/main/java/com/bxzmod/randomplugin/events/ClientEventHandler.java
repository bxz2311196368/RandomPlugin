package com.bxzmod.randomplugin.events;

import com.bxzmod.randomplugin.hotkey.KeyHandler;
import com.bxzmod.randomplugin.render.RenderHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientEventHandler
{
	public static final ClientEventHandler INSTANCE = new ClientEventHandler();

	private ClientEventHandler()
	{

	}

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			KeyHandler.sendSyncData();
			KeyHandler.sendKeyChange();
		}

	}

	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event)
	{
		KeyHandler.handlerKeyPress();
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			RenderHandler.onRenderTick();
		}
	}
}
