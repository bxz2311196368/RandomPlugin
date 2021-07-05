package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.hotkey.KeyHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerKeyPressPacket implements IMessage
{
	public NBTTagCompound nbtTagCompound;

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

	public static class SendToServerHandler implements IMessageHandler<PlayerKeyPressPacket, IMessage>
	{

		@Override
		public IMessage onMessage(PlayerKeyPressPacket message, MessageContext ctx)
		{
			if (ctx.side == Side.SERVER)
			{
				try
				{
					final NBTTagCompound nbt = message.nbtTagCompound;
					KeyHandler.onKeyPressPacket(nbt);
				} catch (Exception ignored)
				{

				}
			}
			return null;
		}
	}

}
