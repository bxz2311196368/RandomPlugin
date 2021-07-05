package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.config.ChainMineConfig;
import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import javax.vecmath.Point3i;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class SimulatePlayer
{
	private Block block;
	private final EntityPlayerMP player;
	private final WorldServer world;
	private int posX, posY, posZ;

	private final boolean silkTouch;
	private final int fortune;
	private int meta;
	private final ArrayList<ItemStack> itemStackList = Lists.newArrayList();
	private ItemStack tool = null;
	private boolean canContinue = true;
	private final boolean forceBreak, damageItem, limitXPDrop, exhaustPlayer, limitItemDrop;

	private SimulatePlayer(EntityPlayerMP player, WorldServer world, int posX, int posY, int posZ, boolean silkTouch,
			int fortune, ItemStack tool, boolean exhaustPlayer, boolean limitItemDrop, boolean forceBreak,
			boolean damageItem, boolean limitXPDrop)
	{
		this.player = player;
		this.world = world;
		this.updateBlockPos(posX, posY, posZ);
		this.exhaustPlayer = exhaustPlayer;
		this.limitItemDrop = limitItemDrop;
		this.silkTouch = silkTouch;
		this.fortune = fortune;
		this.tool = tool;
		this.forceBreak = forceBreak;
		this.damageItem = damageItem;
		this.limitXPDrop = limitXPDrop;
	}

	public int startListMining(ArrayDeque<Point3i> blockList)
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

		if (!this.block.isAir(this.world, this.posX, this.posY, this.posZ))
		{
			if (this.player.capabilities.isCreativeMode)
			{
				this.world.setBlockToAir(this.posX, this.posY, this.posZ);
				return -10;
			}
			if (!this.canBlockHarvest())
				return -1;

			int exp = this.getBlockXpDrop();
			if (exp == -1)
				return -1;

			if (this.block.removedByPlayer(this.world, this.player, this.posX, this.posY, this.posZ, true))
			{
				this.block.onBlockHarvested(this.world, this.posX, this.posY, this.posZ, this.meta, this.player);
				this.harvestBlock();
				if (this.damageItem && this.tool != null)
				{
					this.tool.damageItem(1, this.player);
					if (this.tool.isItemStackDamageable() && this.tool.stackSize == 0)
					{
						this.player.destroyCurrentEquippedItem();
						this.canContinue = false;
					}
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
		if (this.block.getPlayerRelativeBlockHardness(this.player, this.world, this.posX, this.posY, this.posZ) <= 0.0D)
			return -1;
		return ForgeHooks
				.onBlockBreakEvent(this.world, this.player.theItemInWorldManager.getGameType(), this.player, this.posX,
						this.posY, this.posZ).getExpToDrop();
	}

	public boolean canBlockHarvest()
	{
		if (!this.forceBreak)
		{
			if (!block.getMaterial().isToolNotRequired())
			{
				if (this.tool != null)
				{
					return ForgeHooks.canToolHarvestBlock(this.block, this.meta, this.tool);
				} else
					return this.player.canHarvestBlock(this.block);

			} else
			{
				return true;
			}
		}
		return true;
	}

	public void harvestBlock()
	{
		this.player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this.block)], 1);
		if (this.exhaustPlayer)
			this.player.addExhaustion(0.005F);
		this.itemStackList.addAll(getBlockDrops());
		if (!this.limitItemDrop)
			this.dropBlockAsItemWithChance();

	}

	public ArrayList<ItemStack> getBlockDrops()
	{
		ArrayList<ItemStack> items = Lists.newArrayList();
		if (this.silkTouch && this.block
				.canSilkHarvest(this.world, this.player, this.posX, this.posY, this.posZ, this.meta))
		{
			ItemStack itemstack = ((IMixinBlock) this.block).getSilkTouchDrop(this.meta);
			if (itemstack != null)
				items.add(itemstack);
		} else
			return this.block.getDrops(this.world, this.posX, this.posY, this.posZ, this.meta, this.fortune);
		return items;
	}

	public void dropBlockAsItemWithChance()
	{
		float chance = ForgeEventFactory
				.fireBlockHarvesting(this.itemStackList, this.world, this.block, this.posX, this.posY, this.posZ,
						this.meta, this.fortune, 1.0f, this.silkTouch, this.player);
		if (!this.world.isRemote && !this.world.restoringBlockSnapshots)
			for (ItemStack item : this.itemStackList)
				if (this.world.rand.nextFloat() <= chance)
					this.world.spawnEntityInWorld(
							new EntityItem(this.world, this.player.posX, this.player.posY, this.player.posZ, item));
		this.itemStackList.clear();
	}

	public void dropXp(int amount)
	{
		dropXp((double) this.posX + 0.5D, (double) this.posY + 0.5D, (double) this.posZ + 0.5D, amount);
	}

	public void dropXp(double x, double y, double z, int amount)
	{
		if (!this.world.isRemote && this.world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
			this.world.spawnEntityInWorld(new EntityXPOrb(this.world, x, y, z, amount));
	}

	public void updateBlockPos(int x, int y, int z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.setBlockData();
	}

	public void updateBlockPos(Point3i blockPos)
	{
		this.updateBlockPos(blockPos.x, blockPos.y, blockPos.z);
	}

	private void setBlockData()
	{
		this.block = this.world.getBlock(this.posX, this.posY, this.posZ);
		this.meta = this.world.getBlockMetadata(this.posX, this.posY, this.posZ);
	}

	public ArrayList<ItemStack> getDropList()
	{
		return this.itemStackList;
	}

	public static class Builder
	{
		private final EntityPlayerMP player;
		private final WorldServer world;
		private final int posX, posY, posZ;
		private boolean exhaustPlayer = true;
		private boolean limitItemDrop = false;
		private ItemStack tool = null;
		private int fortune = 0;
		private boolean silkTouch = false;
		private boolean forceBreak = false;
		private boolean damageItem = true;
		private boolean limitXPDrop = false;

		public Builder(EntityPlayerMP player, WorldServer world, int posX, int posY, int posZ)
		{
			this.player = player;
			this.world = world;
			this.posX = posX;
			this.posY = posY;
			this.posZ = posZ;
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
			return this.withTool(this.player.getHeldItem());
		}

		public SimulatePlayer create()
		{
			return new SimulatePlayer(this.player, this.world, this.posX, this.posY, this.posZ, this.silkTouch,
					this.fortune, this.tool, this.exhaustPlayer, this.limitItemDrop, this.forceBreak, this.damageItem,
					this.limitXPDrop);
		}

		/**
		 * 获取工具附魔属性，存入{@link #fortune}, {@link #silkTouch}
		 */
		private void findToolEnchantment(ItemStack tool)
		{
			this.fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, tool);
			this.silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, tool) > 0;
		}
	}
}
