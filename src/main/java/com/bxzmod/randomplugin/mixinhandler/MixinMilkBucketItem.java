package com.bxzmod.randomplugin.mixinhandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MixinMilkBucketItem extends Item
{

	public MixinMilkBucketItem(Properties p_i48487_1_)
	{
		super(p_i48487_1_);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void Mixininit(Properties p_i48481_1_, CallbackInfo ci)
	{
		this.maxStackSize = 64;
	}

	@Inject(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V"))
	public void onItemUseFinish(ItemStack itemStack, World world, LivingEntity entity,
			CallbackInfoReturnable<ItemStack> cir)
	{
		if (itemStack.getCount() > 1)
		{
			if (!((PlayerEntity) entity).inventory.addItemStackToInventory(new ItemStack(Items.BUCKET)))
			{
				((PlayerEntity) entity).dropItem(new ItemStack(Items.BUCKET), false);
			}
		}
	}
}
