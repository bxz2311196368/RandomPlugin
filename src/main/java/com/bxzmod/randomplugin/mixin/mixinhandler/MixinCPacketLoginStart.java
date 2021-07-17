package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.RandomPlugin;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.CPacketLoginStart;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CPacketLoginStart.class)
public abstract class MixinCPacketLoginStart
{
	@Shadow
	private GameProfile profile;

	@Inject(method = "readPacketData", at = @At("HEAD"), cancellable = true)
	public void handlerReadPacketData(PacketBuffer buf, CallbackInfo ci)
	{
		try
		{
			String s = buf.readString(100);
			String name = StringUtils.substringBetween(s, "name:", "UUID:");
			UUID id = UUID.fromString(StringUtils.substringAfter(s, "UUID:"));
			this.profile = new GameProfile(id, name);
			RandomPlugin.LOGGER.warn("接收到连接请求：" + s);
		} catch (Exception e)
		{
			return;
		}
		//this.profile = new GameProfile();
		ci.cancel();
	}

	@Inject(method = "writePacketData", at = @At("HEAD"), cancellable = true)
	public void handlerWritePacketData(PacketBuffer buf, CallbackInfo ci)
	{
		try
		{
			String s = "name:" + this.profile.getName() + "UUID:" + this.profile.getId().toString();
			if (s.length() > 100)
				return;
			buf.writeString(s);
			RandomPlugin.LOGGER.warn("连接服务器，玩家属性:" + s);
		} catch (Exception e)
		{
			return;
		}
		ci.cancel();
	}
}
