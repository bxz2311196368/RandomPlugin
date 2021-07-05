package com.bxzmod.randomplugin.command;

import com.bxzmod.randomplugin.network.ConfigSyncPacket;
import com.google.common.collect.ImmutableList;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class ModCommand extends ModCommandBase
{

	@Override
	public String getCommandName()
	{
		return "rpset";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	public List getCommandAliases()
	{
		return ImmutableList.of("randomplugin_setting");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1)
		{
			return ImmutableList.of("sync");
		}
		return super.addTabCompletionOptions(sender, args);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		if (args.length > 0)
		{
			if (args[0].equals("sync") && sender instanceof EntityPlayerMP)
			{
				ConfigSyncPacket.sendMessage((EntityPlayerMP) sender);
			}
		}
	}

}
