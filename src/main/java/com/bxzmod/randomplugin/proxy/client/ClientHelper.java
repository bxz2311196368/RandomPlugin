package com.bxzmod.randomplugin.proxy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

public class ClientHelper
{
	public static final String MESSAGE = "message.";

	public static void addChatMessage(ITextComponent textComponent, int chatIdTobedeleted)
	{
		Minecraft.getMinecraft().ingameGUI.getChatGUI()
				.printChatMessageWithOptionalDeletion(textComponent, chatIdTobedeleted);
	}

	public static String fixMessageI18n(String key)
	{
		return MESSAGE + key;
	}
}
