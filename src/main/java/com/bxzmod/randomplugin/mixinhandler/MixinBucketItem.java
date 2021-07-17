package com.bxzmod.randomplugin.mixinhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem extends Item
{

	public MixinBucketItem(Properties p_i48487_1_)
	{
		super(p_i48487_1_);
	}

	@Inject(method = "<init>(Lnet/minecraft/fluid/Fluid;Lnet/minecraft/item/Item$Properties;)V", at = @At("TAIL"))
	public void Mixininit(Fluid p_i49025_1_, Properties p_i49025_2_, CallbackInfo ci)
	{
		this.maxStackSize = 64;
	}

	/**
	 * @author bxzsj
	 */
	@Overwrite
	protected ItemStack emptyBucket(ItemStack itemStack, PlayerEntity player)
	{
		if (player.abilities.isCreativeMode)
			return itemStack;
		itemStack.shrink(1);
		if (itemStack.isEmpty())
			return new ItemStack(Items.BUCKET);
		if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET)))
			player.dropItem(new ItemStack(Items.BUCKET), false);
		return itemStack;

	}
}
