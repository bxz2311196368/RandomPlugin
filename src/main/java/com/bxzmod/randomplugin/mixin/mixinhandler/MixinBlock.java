package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class MixinBlock implements IMixinBlock
{

	@Shadow
	protected abstract ItemStack createStackedBlock(int p_149644_1_);

	public ItemStack getSilkTouchDrop(int meta)
	{
		return this.createStackedBlock(meta);
	}
}
