package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.config.ModConfig.ChainMineConfig;
import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayDeque;

public class SimulatePlayer
{
	private IBlockState blockState;
	private Block block;
	private final EntityPlayerMP player;
	private final WorldServer world;
	private BlockPos targetPos;

	private final boolean silkTouch;
	private final int fortune;
	private final NonNullList<ItemStack> itemStackList = NonNullList.create();
	private ItemStack tool = ItemStack.EMPTY;
	private boolean canContinue = true;
	private final boolean forceBreak, damageItem, limitXPDrop, exhaustPlayer, limitItemDrop;

	private SimulatePlayer(EntityPlayerMP player, WorldServer world, BlockPos startPos, boolean silkTouch, int fortune,
			ItemStack tool, boolean exhaustPlayer, boolean limitItemDrop, boolean forceBreak, boolean damageItem,
			boolean limitXPDrop)
	{
		this.player = player;
		this.world = world;
		this.updateBlockPos(startPos);
		this.exhaustPlayer = exhaustPlayer;
		this.limitItemDrop = limitItemDrop;
		this.silkTouch = silkTouch;
		this.fortune = fortune;
		this.tool = tool;
		this.forceBreak = forceBreak;
		this.damageItem = damageItem;
		this.limitXPDrop = limitXPDrop;
	}

	public int startListMining(ArrayDeque<BlockPos> blockList)
	{
		int exp = 0;
		while (!blockList.isEmpty())
		{
			this.updateBlockPos(blockList.removeFirst());
			int temp = this.simulatePlayerBreakBlock();
			switch (temp)
			{
				case -1000:
				case -100:
					return exp;
			}
			if (temp > 0)
				exp += temp;
		}
		return exp;
	}

	/**
	 * 模拟玩家采掘方块
	 *
	 * @return {@code -100}：在客户端调用了这个方法（这不应该发生）<br/>
	 * {@code -1}：无法挖掘这个方块<br/>
	 * {@code -10}：玩家处于创造模式<br/>
	 * {@code -1000}：无法继续挖掘<br/>
	 * {@code 0}：这个方块不掉落经验<br/>
	 * {@code >0}：该方块的挖掘掉落经验<br/>
	 */
	public int simulatePlayerBreakBlock()
	{
		if (!this.canContinue)
			return -1000;
		if (this.world.isRemote)
		{
			new RuntimeException("Break block at client, that shouldn't happen!").printStackTrace();
			return -100;
		}

		if (!this.block.isAir(this.blockState, this.world, this.targetPos))
		{
			if (this.player.capabilities.isCreativeMode)
			{
				this.world.setBlockToAir(this.targetPos);
				return -10;
			}
			if (!this.canBlockHarvest())
				return -1;

			int exp = this.getBlockXpDrop();
			if (exp == -1)
				return -1;

			if (this.block.removedByPlayer(this.blockState, this.world, this.targetPos, this.player, true))
			{
				this.block.onBlockHarvested(this.world, this.targetPos, this.blockState, this.player);
				this.harvestBlock();
				if (this.damageItem && !this.tool.isEmpty())
				{
					this.tool.damageItem(1, this.player);
					this.canContinue = !this.tool.isEmpty();
				}
				if (!this.limitXPDrop && exp > 0)
					dropXp(exp);
				return exp;
			}
		}
		return 0;
	}

	public int getBlockXpDrop()
	{
		if (this.blockState.getPlayerRelativeBlockHardness(this.player, this.world, this.targetPos) <= 0.0D)
			return -1;
		return ForgeHooks.onBlockBreakEvent(this.world, this.player.interactionManager.getGameType(), this.player,
				this.targetPos);
	}

	public boolean canBlockHarvest()
	{
		if (!this.forceBreak)
		{
			if (!blockState.getMaterial().isToolNotRequired())
			{
				if (!this.tool.isEmpty())
				{
					return ForgeHooks.canToolHarvestBlock(this.world, this.targetPos, this.tool);
				} else
					return this.player.canHarvestBlock(this.blockState);

			} else
			{
				return true;
			}
		}
		return true;
	}

	public void harvestBlock()
	{
		this.player.addStat(StatList.getBlockStats(this.block), 1);
		if (this.exhaustPlayer)
			this.player.addExhaustion(0.005F);
		this.itemStackList.addAll(getBlockDrops());
		if (!this.limitItemDrop)
			this.dropBlockAsItemWithChance();

	}

	public NonNullList<ItemStack> getBlockDrops()
	{
		NonNullList<ItemStack> items = NonNullList.create();
		if (this.silkTouch && this.block.canSilkHarvest(this.world, this.targetPos, this.blockState, this.player))
		{
			ItemStack itemstack = ((IMixinBlock) this.blockState).getSKDrop(this.blockState);
			if (!itemstack.isEmpty())
				items.add(itemstack);
		} else
		{
			this.block.getDrops(items, this.world, this.targetPos, this.blockState, this.fortune);
		}
		return items;
	}

	public void dropBlockAsItemWithChance()
	{
		float chance = ForgeEventFactory
				.fireBlockHarvesting(this.itemStackList, this.world, this.targetPos, this.blockState, this.fortune,
						1.0f, this.silkTouch, this.player);
		if (!this.world.isRemote && !this.world.restoringBlockSnapshots)
			for (ItemStack item : this.itemStackList)
				if (this.world.rand.nextFloat() <= chance)
					this.world.spawnEntity(
							new EntityItem(this.world, this.player.posX, this.player.posY, this.player.posZ, item));
		this.itemStackList.clear();
	}

	public void dropXp(int amount)
	{
		dropXp((double) this.targetPos.getX() + 0.5D, (double) this.targetPos.getY() + 0.5D,
				(double) this.targetPos.getZ() + 0.5D, amount);
	}

	public void dropXp(double x, double y, double z, int amount)
	{
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doTileDrops"))
			this.world.spawnEntity(new EntityXPOrb(this.world, x, y, z, amount));
	}

	public void updateBlockPos(int x, int y, int z)
	{
		this.updateBlockPos(new BlockPos(x, y, z));
	}

	public void updateBlockPos(BlockPos blockPos)
	{
		this.targetPos = blockPos;
		this.setBlockData();

	}

	private void setBlockData()
	{
		this.blockState = this.world.getBlockState(this.targetPos);
		this.block = this.blockState.getBlock();
	}

	public NonNullList<ItemStack> getDropList()
	{
		return this.itemStackList;
	}

	public static class Builder
	{
		private final EntityPlayerMP player;
		private final WorldServer world;
		private final BlockPos pos;
		private boolean exhaustPlayer = true;
		private boolean limitItemDrop = false;
		private ItemStack tool = ItemStack.EMPTY;
		private int fortune = 0;
		private boolean silkTouch = false;
		private boolean forceBreak = false;
		private boolean damageItem = true;
		private boolean limitXPDrop = false;

		public Builder(EntityPlayerMP player, WorldServer world, BlockPos pos)
		{
			this.player = player;
			this.world = world;
			this.pos = pos;
		}

		public Builder fromConfig()
		{
			this.damageItem = ChainMineConfig.SHOULD_DAMAGE_ITEM;
			this.exhaustPlayer = ChainMineConfig.EXHAUST_PLAYER;
			this.forceBreak = !ChainMineConfig.CHECK_TOOL;
			return this;
		}

		public Builder noExhaust()
		{
			this.exhaustPlayer = false;
			return this;
		}

		public Builder noDrop()
		{
			return this.noItemDrop().noXpDrop();
		}

		public Builder noItemDrop()
		{
			this.limitItemDrop = true;
			return this;
		}

		public Builder force()
		{
			this.forceBreak = true;
			return this;
		}

		public Builder noXpDrop()
		{
			this.limitXPDrop = true;
			return this;
		}

		public Builder noDamage()
		{
			this.damageItem = false;
			return this;
		}

		public Builder withTool(ItemStack tool)
		{
			if (tool == null)
				return this;
			this.tool = tool;
			this.findToolEnchantment(tool);
			return this;
		}

		public Builder playerTool()
		{
			return this.withTool(this.player.getHeldItem(EnumHand.MAIN_HAND));
		}

		public SimulatePlayer create()
		{
			return new SimulatePlayer(this.player, this.world, this.pos, this.silkTouch, this.fortune, this.tool,
					this.exhaustPlayer, this.limitItemDrop, this.forceBreak, this.damageItem, this.limitXPDrop);
		}

		/**
		 * 获取工具附魔属性，存入{@link #fortune}, {@link #silkTouch}
		 */
		private void findToolEnchantment(ItemStack tool)
		{
			this.fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
			this.silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
		}
	}
}
