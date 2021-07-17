package com.bxzmod.randomplugin.network;

import com.bxzmod.randomplugin.hotkey.KeyHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
