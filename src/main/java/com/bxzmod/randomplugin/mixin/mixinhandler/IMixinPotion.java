package com.bxzmod.randomplugin.mixin.mixinhandler;

import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Potion.class)
public interface IMixinPotion
{
	@Accessor("isBadEffect")
	boolean getBadEffect();
}
