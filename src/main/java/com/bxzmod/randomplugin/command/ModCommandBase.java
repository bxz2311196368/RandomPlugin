package com.bxzmod.randomplugin.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class ModCommandBase extends CommandBase
{
	public static final String COMMAND_USAGE = "command.usage.";

	public String getCommandUsageI18nKey()
	{
		return COMMAND_USAGE + this.getCommandName();
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return getCommandUsageI18nKey();
	}
}
