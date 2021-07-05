package com.bxzmod.randomplugin.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class RenderHelper
{

	public static void renderTextAboveItemHotBar(String message)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		FontRenderer fontrenderer = mc.fontRenderer;
		int y = height - 59;
		if (!mc.playerController.shouldDrawHUD())
			y += 14;
		int x = (width - fontrenderer.getStringWidth(message)) / 2;
		renderText(fontrenderer, x, y, message);
	}

	public static void renderText(FontRenderer fontrenderer, int x, int y, String message)
	{
		prepareRenderText();
		fontrenderer.drawStringWithShadow(message, x, y, Color.WHITE);
		afterRender();
	}

	public static void prepareRenderText()
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
	}

	public static void afterRender()
	{
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
