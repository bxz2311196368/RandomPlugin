package com.bxzmod.randomplugin.mixin.mixinhandler;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer
{
	@Inject(method = "resetCooldown", at = @At("HEAD"), cancellable = true)
	public void handler_resetCooldown(CallbackInfo ci)
	{
		ci.cancel();
	}

	@Inject(method = "getCooledAttackStrength", at = @At("HEAD"), cancellable = true)
	public void handler_getCooledAttackStrength(CallbackInfoReturnable<Float> cir)
	{
		cir.setReturnValue(1.0F);
	}

	@Inject(method = "getCooldownPeriod", at = @At("HEAD"), cancellable = true)
	public void handler_getCooldownPeriod(CallbackInfoReturnable<Float> cir)
	{
		cir.setReturnValue(1.0F);
	}
}
