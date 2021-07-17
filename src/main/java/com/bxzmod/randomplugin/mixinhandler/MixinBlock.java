package com.bxzmod.randomplugin.mixinhandler;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class MixinBlock
{
	@Inject(method = "spawnAsEntity",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"),
			cancellable = true)
	private static void popResource(World world, BlockPos blockPos, ItemStack itemStack, CallbackInfo ci)
	{

	}

	@Inject(method = "dropXpOnBlockBreak", at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 0),
			cancellable = true)
	public void popExperience(ServerWorld world, BlockPos blockPos, int amount, CallbackInfo ci)
	{

	}
}
