package com.bxzmod.randomplugin.mixin.mixininterface;

import net.minecraft.item.ItemStack;

public interface IMixinBlock
{
	ItemStack getSilkTouchDrop(int meta);
}
