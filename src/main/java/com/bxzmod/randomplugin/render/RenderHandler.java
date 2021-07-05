package com.bxzmod.randomplugin.render;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderHandler
{
	private static final HashMap<String, RenderTask> bindTasks = Maps.newHashMap();

	public static void onRenderTick()
	{
		if (bindTasks.isEmpty())
			return;
		synchronized (bindTasks)
		{
			Iterator<Map.Entry<String, RenderTask>> iterator = bindTasks.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry<String, RenderTask> entry = iterator.next();
				RenderTask task = entry.getValue();
				if (task.shouldRender())
					task.run();
				else
					iterator.remove();
			}
		}
	}

	public static void addBindTask(String name, RenderTask task)
	{
		bindTasks.put(name, task);
	}
}
