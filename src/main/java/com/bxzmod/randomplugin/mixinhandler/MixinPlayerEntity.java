package com.bxzmod.randomplugin.mixinhandler;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity
{
	/**
	 * @author bxzsj
	 */
	@Overwrite
	public float getCooledAttackStrength(float p_184825_1_)
	{
		return 1.0f;
	}
}
