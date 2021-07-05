package com.bxzmod.randomplugin.command;

import com.bxzmod.randomplugin.utils.TeleportManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

public class TPDCommand extends ModCommandBase
{
	@Override
	public String getCommandName()
	{
		return "tpd";
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
				try
				{
					int dimension = Integer.parseInt(args[0]);
					TeleportManager.teleportEntityPlayer(player, 0, 64, 0, dimension);
				} catch (Exception e)
				{
					throw new WrongUsageException(this.getCommandUsageI18nKey());
				}
			} else if (args.length == 4)
			{
				try
				{
					int dimension = Integer.parseInt(args[0]);
					double x = Double.parseDouble(args[1]);
					double y = Double.parseDouble(args[2]);
					double z = Double.parseDouble(args[3]);
					TeleportManager.teleportEntityPlayer(player, x, y, z, dimension);
				} catch (Exception e)
				{
					throw new WrongUsageException(this.getCommandUsageI18nKey());
				}
			} else
				throw new WrongUsageException(this.getCommandUsageI18nKey());
		}
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}
}
