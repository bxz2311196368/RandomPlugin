package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.Main;
import com.bxzmod.randomplugin.config.ConfigLoader;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

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
				Main.LOGGER.warn("received config from client");
				ConfigLoader.INSTANCE.deserializeNBT(nbt);
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
			packet.nbtTagCompound = ConfigLoader.INSTANCE.serializeNBT();
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
