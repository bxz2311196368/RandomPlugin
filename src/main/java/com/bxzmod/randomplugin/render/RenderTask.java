package com.bxzmod.randomplugin.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class RenderTask implements Runnable
{
	public int renderTick = 40;

	public static void createStringRender(String name, String text)
	{
		RenderHandler.addBindTask(name, new RenderStringTask(text));
	}

	public boolean shouldRender()
	{
		return this.renderTick-- > 0;
	}

	public void stopRender()
	{
		this.renderTick = -1;
	}

	static class RenderStringTask extends RenderTask
	{
		int x, y;
		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
		String text;

		public RenderStringTask(String text)
		{
			this.text = text;
		}

		@Override
		public void run()
		{
			RenderHelper.renderTextAboveItemHotBar(this.text);
		}

	}
}
