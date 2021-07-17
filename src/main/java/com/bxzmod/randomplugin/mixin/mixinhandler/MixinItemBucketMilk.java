package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.registry.ModObjectHolder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBucketMilk.class)
public class MixinItemBucketMilk
{
	@Inject(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V"))
	public void onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving,
			CallbackInfoReturnable<ItemStack> cir)
	{
		if (stack.getCount() > 1)
			if (!((EntityPlayer) entityLiving).inventory.addItemStackToInventory(new ItemStack(ModObjectHolder.bucket)))
			{
				((EntityPlayer) entityLiving).dropItem(new ItemStack(ModObjectHolder.bucket), false);
			}
	}
}
