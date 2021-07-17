package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.RandomPlugin;
import com.bxzmod.randomplugin.config.ModConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigSyncPacket implements IMessage
{
	public NBTTagCompound nbtTagCompound = new NBTTagCompound();

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.nbtTagCompound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbtTagCompound);
	}

	public static class SendToServerHandler implements IMessageHandler<ConfigSyncPacket, IMessage>
	{

		@Override
		public IMessage onMessage(ConfigSyncPacket message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				final NBTTagCompound nbt = message.nbtTagCompound;
				RandomPlugin.LOGGER.warn("received config from client");
				ModConfig.deserializeNBT(nbt);
			}
			return null;
		}
	}

	public static class SendToClientHandler implements IMessageHandler<ConfigSyncPacket, IMessage>
	{

		@Override
		public IMessage onMessage(ConfigSyncPacket message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				return getMessage();
			}
			return null;
		}

		private IMessage getMessage()
		{

			ConfigSyncPacket packet = new ConfigSyncPacket();
			packet.nbtTagCompound = ModConfig.serializeNBT();
			return packet;
		}
	}

	/**
	 * 从服务端执行指令发包到客户端获取配置
	 * 这个包仅是标记，所以不需要任何数据
	 *
	 * @param player 执行指令的玩家
	 */
	public static void sendMessage(EntityPlayerMP player)
	{
		try
		{
			ConfigSyncPacket packet = new ConfigSyncPacket();
			NetworkLoader.instance.sendTo(packet, player);
		} catch (Exception ignore)
		{

		}
	}
}
