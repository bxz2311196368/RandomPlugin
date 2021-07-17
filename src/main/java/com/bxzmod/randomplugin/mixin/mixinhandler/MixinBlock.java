package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class MixinBlock extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Block>
		implements IMixinBlock
{
	@Shadow
	protected abstract ItemStack getSilkTouchDrop(IBlockState state);

	@Override
	public ItemStack getSKDrop(IBlockState state)
	{
		return this.getSilkTouchDrop(state);
	}
}
