package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInWorldManager.class)
public abstract class MixinItemInWorldManager
{
	@Shadow
	public EntityPlayerMP thisPlayerMP;

	@Inject(method = "tryUseItem", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/item/ItemStack;useItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"),
			cancellable = true)
	public void handler_tryUseItem(EntityPlayer player, World world, ItemStack itemStack,
			CallbackInfoReturnable<Boolean> cir)
	{
		if (itemStack.getItem() == ItemLoader.materialOrb)
		{
			if (itemStack.stackSize <= 0)
			{
				player.destroyCurrentEquippedItem();
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "onBlockClicked", at = @At("HEAD"))
	public void handler_onBlockClicked(int x, int y, int z, int facing, CallbackInfo ci)
	{
		ModPlayerData.getDataByPlayer(this.thisPlayerMP).setFacing(facing);
	}
}
