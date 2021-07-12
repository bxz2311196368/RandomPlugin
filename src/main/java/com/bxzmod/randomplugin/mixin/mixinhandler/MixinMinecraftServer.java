package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.mixin.mixininterface.IMixinEntityTracker;
import net.minecraft.entity.EntityTracker;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer
{
	@Redirect(method = "updateTimeLightAndEntities",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityTracker;updateTrackedEntities()V"))
	public void handler_updateTimeLightAndEntities(EntityTracker entityTracker)
	{
		((IMixinEntityTracker) entityTracker).safeUpdateTrackedEntities();
	}
}
