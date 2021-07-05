package com.bxzmod.randomplugin.proxy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class ClientHelper
{
	public static final String MESSAGE = "message.";

	public static void addChatMessage(IChatComponent chatComponent, int chatIdTobedeleted)
	{
		Minecraft.getMinecraft().ingameGUI.getChatGUI()
				.printChatMessageWithOptionalDeletion(chatComponent, chatIdTobedeleted);
	}

	public static String fixMessageI18n(String key)
	{
		return MESSAGE + key;
	}
}
