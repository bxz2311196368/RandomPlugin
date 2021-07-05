package com.bxzmod.randomplugin.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;

public class MiningTickEventHandler
{
	public static final ArrayDeque<Runnable> TASKS = new ArrayDeque<Runnable>();

	public static MiningTickEventHandler INSTANCE = new MiningTickEventHandler();

	private MiningTickEventHandler()
	{
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if (TASKS.isEmpty())
			return;
		synchronized (TASKS)
		{
			while (!TASKS.isEmpty())
				TASKS.pop().run();
		}
	}

	public static void addTask(Runnable task)
	{
		synchronized (TASKS)
		{
			TASKS.addLast(task);
		}
	}
}
