package com.bxzmod.randomplugin.mixin.mixininterface;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * 这个接口不能放置在mixin的包里
 */
public interface IMixinBlock
{
	/**
	 * 用于获取精准采集掉落物
	 *
	 * @param state
	 * @return
	 */
	public ItemStack getSKDrop(IBlockState state);
}
