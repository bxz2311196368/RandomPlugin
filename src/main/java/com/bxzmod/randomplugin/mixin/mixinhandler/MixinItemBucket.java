package com.bxzmod.randomplugin.mixin.mixinhandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBucket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBucket.class)
public abstract class MixinItemBucket
{
	@Shadow
	private Block isFull;

	@Inject(method = "tryPlaceContainedLiquid",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSoundEffect(DDDLjava/lang/String;FF)V"),
			cancellable = true)
	public void tryPlaceContainedLiquid(World p_77875_1_, int p_77875_2_, int p_77875_3_, int p_77875_4_,
			CallbackInfoReturnable<Boolean> cir)
	{
		Material material = p_77875_1_.getBlock(p_77875_2_, p_77875_3_, p_77875_4_).getMaterial();
		boolean flag = !material.isSolid();
		if (!p_77875_1_.isRemote && flag && !material.isLiquid())
		{
			p_77875_1_.func_147480_a(p_77875_2_, p_77875_3_, p_77875_4_, true);
		}

		p_77875_1_.setBlock(p_77875_2_, p_77875_3_, p_77875_4_, this.isFull, 0, 3);
		cir.setReturnValue(true);
	}
}
