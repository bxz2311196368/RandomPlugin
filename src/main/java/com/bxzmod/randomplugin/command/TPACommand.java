package com.bxzmod.randomplugin.command;

import com.bxzmod.randomplugin.utils.TeleportManager;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TPACommand extends ModCommandBase
{

	@Override
	public String getCommandName()
	{
		return "tpa";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		if (sender.getEntityWorld().isRemote)
			return;
		if (sender instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if (args.length == 1)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				ArrayList<String> namelist = Lists.newArrayList(server.getAllUsernames());
				if (namelist.contains(sender.getCommandSenderName()))
				{
					EntityPlayerMP target =
							server.getConfigurationManager().func_152612_a(sender.getCommandSenderName());
					if (target == null)
						return;
					TeleportManager
							.teleportEntityPlayer(player, target.posX, target.posY, target.posZ, target.dimension);
				}
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1)
		{
			String[] names = FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames();
			if (names.length <= 1)
				return Collections.emptyList();
			ArrayList<String> namelist = Lists.newArrayList(names);
			namelist.remove(sender.getCommandSenderName());
			return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, namelist);
		}
		return Collections.emptyList();
	}
}
