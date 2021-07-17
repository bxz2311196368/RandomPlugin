package com.bxzmod.randomplugin.command;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class ServerUUIDPrinter extends CommandBase
{

	public ServerUUIDPrinter()
	{

	}

	@Override
	public String getName()
	{
		return "printUUID";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "show uuid common";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0 || args.length > 1)
		{
			sender.sendMessage(new TextComponentString("need player name or \"ALL\""));
		} else
		{
			if (args[0].equalsIgnoreCase("all"))
			{
				GameProfile[] list = server.getPlayerList().getOnlinePlayerProfiles();
				for (GameProfile e : list)
				{
					sender.sendMessage(
							new TextComponentString("name:" + e.getName() + " UUID:" + e.getId().toString()));
				}
			} else
			{
				EntityPlayerMP p = server.getPlayerList().getPlayerByUsername(args[0]);
				if (p != null)
				{
					GameProfile e = p.getGameProfile();
					sender.sendMessage(
							new TextComponentString("name:" + e.getName() + " UUID:" + e.getId().toString()));
				} else
				{
					sender.sendMessage(new TextComponentString("wrong player name!"));
				}
			}
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos)
	{
		if (args.length == 1)
		{
			List<String> list = Lists.newArrayList(server.getPlayerList().getOnlinePlayerNames());
			list.add("ALL");
			return list;
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}

}
