package com.bxzmod.randomplugin.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class ServerShowHand extends CommandBase
{
	@Override
	public String getName()
	{
		return "server_hand";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack stack = player.getHeldItemMainhand();
			String nbt = stack.serializeNBT().toString();
			sender.sendMessage(new TextComponentString("Server:" + nbt));
		}
	}
}
