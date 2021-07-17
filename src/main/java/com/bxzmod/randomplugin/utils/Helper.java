package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Objects;

public class Helper
{
	public static int simulatePlayerBreakBlock(BlockPos pos, BlockPos playerPos, EntityPlayer player,
			boolean forceBreak, int fortune)
	{
		return simulatePlayerBreakBlock(pos, playerPos, player, forceBreak, fortune, false, true, true,
				NonNullList.create());
	}

	public static int simulatePlayerBreakBlock(BlockPos pos, BlockPos playerPos, EntityPlayer player,
			boolean forceBreak, int fortune, boolean silkTouch, NonNullList<ItemStack> itemStacks)
	{
		return simulatePlayerBreakBlock(pos, playerPos, player, forceBreak, fortune, silkTouch, true, true, itemStacks);
	}

	public static int simulatePlayerBreakBlock(BlockPos pos, BlockPos playerPos, EntityPlayer player,
			boolean forceBreak, ItemStack tool, NonNullList<ItemStack> itemStacks)
	{
		if (!tool.isEmpty())
		{
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
				return simulatePlayerBreakBlock(pos, playerPos, player, false, 0, true, false, false,
						NonNullList.create());
			else
			{
				int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
				if (fortune > 0)
					return simulatePlayerBreakBlock(pos, playerPos, player, false, fortune, false, false, false,
							NonNullList.create());
			}
		}
		return simulatePlayerBreakBlock(pos, playerPos, player, false, 0, false, false, false, NonNullList.create());
	}

	public static int simulatePlayerBreakBlock(BlockPos pos, BlockPos playerPos, EntityPlayer player,
			boolean forceBreak, int fortune, boolean silkTouch, boolean limitXPDrop, boolean limitItemDrop,
			NonNullList<ItemStack> itemStacks)
	{
		World world = player.world;
		if (!world.isBlockLoaded(pos))
			return 0;

		IBlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();

		if (!world.isRemote && !block.isAir(blockState, world, pos))
		{
			if (!player.capabilities.isCreativeMode && !block.canHarvestBlock(player.world, pos, player)
					&& blockState.getPlayerRelativeBlockHardness(player, world, pos) > 0)
				return 0;

			int exp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(),
					(EntityPlayerMP) player, pos);
			if (exp == -1)
				return 0;

			if (!player.capabilities.isCreativeMode)
			{
				if (block.removedByPlayer(blockState, world, pos, player, true))
				{
					block.onPlayerDestroy(world, pos, blockState);
					harvestBlock(block, world, player, pos, playerPos, blockState, fortune, silkTouch, limitItemDrop,
							itemStacks);
					if (!limitXPDrop && exp > 0)
						dropXp(world, playerPos, exp);
					return exp;
				}
			} else
				world.setBlockToAir(pos);
		}
		return 0;
	}

	public static void harvestBlock(Block block, World worldIn, EntityPlayer player, BlockPos pos, BlockPos playerPos,
			IBlockState state, int fortune, boolean silkTouch, boolean limitItemDrop, NonNullList<ItemStack> itemStacks)
	{
		player.addStat(StatList.getBlockStats(block));
		player.addExhaustion(0.025F);
		itemStacks.addAll(getBlockDrops(player, block, worldIn, pos, state, fortune, silkTouch));
		if (!limitItemDrop)
			dropBlockAsItemWithChance(itemStacks, player, worldIn, pos, playerPos, state, fortune, silkTouch);

	}

	public static NonNullList<ItemStack> getBlockDrops(EntityPlayer player, Block block, World worldIn, BlockPos pos,
			IBlockState state, int fortune, boolean silkTouch)
	{
		NonNullList<ItemStack> items = NonNullList.create();
		if (silkTouch && block.canSilkHarvest(worldIn, pos, state, player))
		{
			ItemStack itemstack = ((IMixinBlock) block).getSKDrop(state);
			if (!itemstack.isEmpty())
				items.add(itemstack);
		} else
			block.getDrops(items, worldIn, pos, state, fortune);
		return items;
	}

	public static void dropBlockAsItemWithChance(NonNullList<ItemStack> items, EntityPlayer player, World worldIn,
			BlockPos pos, BlockPos playerPos, IBlockState state, int fortune, boolean silkTouch)
	{
		float chance =
				ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, silkTouch, player);
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
			for (ItemStack item : items)
				if (worldIn.rand.nextFloat() <= chance)
					Block.spawnAsEntity(worldIn, playerPos, item);
		items.clear();
	}

	public static boolean isItemStackSame(ItemStack stack1, ItemStack stack2)
	{
		return isItemStackConditionalityEqual(stack1, stack2, false, false);
	}

	public static boolean isItemStackConditionalityEqual(ItemStack stack1, ItemStack stack2, boolean ignoreMeta,
			boolean ignoreNBT)
	{
		if (stack1.isEmpty() != stack2.isEmpty())
			return false;
		if (stack1.isEmpty())
			return true;
		if (!stack1.isItemEqual(stack2))
			return false;
		if (ignoreMeta)
			return true;
		if (stack1.getMetadata() != stack2.getMetadata())
			return false;
		if (ignoreNBT)
			return true;
		if (stack1.hasTagCompound() != stack2.hasTagCompound())
			return false;
		if (!stack1.hasTagCompound())
			return true;
		return Objects.equals(stack1.getTagCompound(), stack2.getTagCompound()) && stack1.areCapsCompatible(stack2);

	}

	public static void dropXp(World worldIn, BlockPos pos, int amount)
	{
		dropXp(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, amount);
	}

	public static void dropXp(World worldIn, double x, double y, double z, int amount)
	{
		if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops"))
			worldIn.spawnEntity(new EntityXPOrb(worldIn, x, y, z, amount));
	}

	public static boolean findMaterialOrb(EntityPlayer player)
	{
		for (ItemStack stack : player.inventory.mainInventory)
			if (stack.isEmpty() && stack.getItem() == ItemLoader.materialOrb)
				return true;
		return false;
	}

	public static boolean canPlayerHarvest(EntityPlayer player, World world, BlockPos pos)
	{
		ItemStack tool = player.getHeldItem(EnumHand.MAIN_HAND);
		IBlockState block = world.getBlockState(pos);
		if (tool.isEmpty())
		{
			return block.getMaterial().isToolNotRequired();
		} else
			return ForgeHooks.canToolHarvestBlock(world, pos, tool);
	}
}
