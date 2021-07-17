package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.utils.ModPlayerData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInteractionManager.class)
public class MixinPlayerInteractionManager
{
	@Shadow
	public EntityPlayerMP player;

	@Inject(method = "onBlockClicked", at = @At("HEAD"))
	public void handler_onBlockClicked(BlockPos pos, EnumFacing side, CallbackInfo ci)
	{
		ModPlayerData.getDataByPlayer(this.player).setFacing(side.getOpposite());
	}
}
