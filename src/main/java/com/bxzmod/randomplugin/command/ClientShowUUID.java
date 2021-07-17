package com.bxzmod.randomplugin.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class ClientShowUUID extends CommandBase
{

	public ClientShowUUID()
	{

	}

	@Override
	public String getName()
	{
		return "printMyUUID";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "show your uuid";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		GameProfile e = Minecraft.getMinecraft().player.getGameProfile();
		sender.sendMessage(new TextComponentString("name:" + e.getName() + " UUID:" + e.getId().toString()));
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return -1;
	}

}
