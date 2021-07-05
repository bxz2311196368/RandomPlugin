package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.Main;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(C00PacketLoginStart.class)
public abstract class MixinC00PacketLoginStart
{
	@Shadow
	private GameProfile field_149305_a;

	@Inject(method = "readPacketData", at = @At("HEAD"), cancellable = true)
	public void handler_readPacketData(PacketBuffer buffer, CallbackInfo ci)
	{
		try
		{
			String s = buffer.readStringFromBuffer(100);
			String name = StringUtils.substringBetween(s, "name:", "UUID:");
			UUID id = UUID.fromString(StringUtils.substringAfter(s, "UUID:"));
			this.field_149305_a = new GameProfile(id, name);
			Main.LOGGER.warn("接收到连接请求：" + s);
		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		ci.cancel();
	}

	@Inject(method = "writePacketData", at = @At("HEAD"), cancellable = true)
	public void handler_writePacketData(PacketBuffer buffer, CallbackInfo ci)
	{
		try
		{
			String s = "name:" + this.field_149305_a.getName() + "UUID:" + this.field_149305_a.getId().toString();
			if (s.length() > 100)
			{
				Main.LOGGER.error("too long write");
				return;
			}
			buffer.writeStringToBuffer(s);
			Main.LOGGER.warn("连接服务器，玩家属性:" + s);
		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		ci.cancel();
	}
}
