package com.bxzmod.randomplugin.utils.chainmining;

import com.bxzmod.randomplugin.RandomPlugin;
import com.bxzmod.randomplugin.config.ModConfig;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.utils.*;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class PlayerChainMining implements IMiningControl
{
	private int totalBlockCount = 1;
	private boolean miningDone = true;
	private final EntityPlayerMP player;
	private final UUID uuid;
	private final BlockPos blockPos;
	private final IBlockState targetBlock;
	private final ArrayDeque<BlockPos> next_blocks = new ArrayDeque<BlockPos>();
	private final ArrayDeque<BlockPos> harvestBlockList = new ArrayDeque<BlockPos>();
	private final HashSet<BlockPos> skipBlockList = Sets.newHashSet();
	private ItemStack targetItemBlock = ItemStack.EMPTY;

	public PlayerChainMining(EntityPlayerMP player, BlockPos blockPos)
	{
		this.player = player;
		this.uuid = player.getGameProfile().getId();
		this.targetBlock = player.world.getBlockState(blockPos);
		this.blockPos = blockPos;
		this.initBlockList(this.blockPos);
		this.findTargetItemResult(this.targetBlock, this.blockPos);
	}

	private void initBlockList(BlockPos pos)
	{
		this.next_blocks.addLast(pos);
		this.harvestBlockList.addLast(pos);
		this.skipBlockList.add(pos);
	}

	@Override
	public void run()
	{
		SimulatePlayer harvest =
				new SimulatePlayer.Builder(this.player, (WorldServer) this.player.world, this.blockPos).fromConfig()
						.noDrop().playerTool().create();

		EnumFacing facing = ModPlayerData.getDataByPlayer(this.player).getFacing();
		boolean faceOffset = ModPlayerData.getDataByPlayer(this.player).isChainMineFaceOffset();
		int offsetX = faceOffset ? -facing.getXOffset() : 0, offsetY = faceOffset ? -facing.getYOffset() : 0, offsetZ =
				faceOffset ? -facing.getZOffset() : 0;
		while (ChainMiningManager.canChainMiningContinue(this.uuid))
		{
			this.findTargetBlock(facing, offsetX, offsetY, offsetZ);
			if (!this.harvestBlockList.isEmpty())
				FMLCommonHandler.instance().getMinecraftServerInstance()
						.addScheduledTask(new MiningTask(this, harvest, this.uuid, this.harvestBlockList));
			else
			{
				break;
			}
			if (!this.waitMiningResult())
			{
				RandomPlugin.LOGGER.error("player:" + this.player.getDisplayName()
						+ ":server no response in 20 tick, maybe server lag, skip mining work!");
				break;
			}
			if (this.next_blocks.isEmpty() || this.totalBlockCount >= ModConfig.ChainMineConfig.MAX_VALUE)
			{
				break;
			}
		}
		this.stopTask(harvest);
	}

	private void stopTask(SimulatePlayer harvest)
	{
		this.fillDrops(harvest.getDropList());
		ChainMiningManager.stopPlayerMining(this.player);
	}

	private void findTargetBlock(EnumFacing facing, int offsetX, int offsetY, int offsetZ)
	{
		int checkCount = 0;
		while (this.miningDone && checkCount < ModConfig.ChainMineConfig.MAX_PER_TICK)
		{
			if (this.next_blocks.isEmpty() || this.totalBlockCount >= ModConfig.ChainMineConfig.MAX_VALUE)
				break;
			BlockPos next = this.next_blocks.removeFirst();
			for (int x = -1 + offsetX; x < 2 + offsetX; x++)
			{
				for (int y = -1 + offsetY; y < 2 + offsetY; y++)
				{
					if (next.getY() + y < 0 || next.getY() + y >= 255)
						continue;
					for (int z = -1 + offsetZ; z < 2 + offsetZ; z++)
					{
						if (x == 0 && y == 0 && z == 0)
							continue;
						BlockPos temp = next.add(x, y, z);
						if (!this.skipBlockList.contains(temp))
						{
							this.skipBlockList.add(temp);
							if (this.isBlockStateEqual(temp))
							{
								this.totalBlockCount++;
								this.next_blocks.addLast(temp);
								this.harvestBlockList.addLast(temp);
								checkCount++;
							}
						}
					}
				}
			}
		}
		this.miningDone = false;
	}

	private boolean isBlockStateEqual(BlockPos pos)
	{
		IBlockState blockState = this.player.world.getBlockState(pos);
		RayTraceResult result = new RayTraceResult(Vec3d.ZERO, EnumFacing.UP, pos);
		return Helper.isItemStackSame(this.targetItemBlock,
				blockState.getBlock().getPickBlock(blockState, result, this.player.world, pos, this.player));
	}

	private void findTargetItemResult(IBlockState state, BlockPos pos)
	{
		RayTraceResult result = new RayTraceResult(Vec3d.ZERO, EnumFacing.UP, this.blockPos);
		this.targetItemBlock =
				this.targetBlock.getBlock().getPickBlock(state, result, this.player.world, pos, this.player);
		if (this.targetItemBlock.isEmpty())
		{
			ChainMiningManager.stopPlayerMining(this.player);
		}

	}

	private boolean waitMiningResult()
	{
		int sleepCount = 0;
		try
		{
			while (!this.miningDone)
			{
				Thread.sleep(10);
				if (sleepCount++ > 100)
					return false;
			}
		} catch (InterruptedException ignored)
		{
		}
		return true;
	}

	@Override
	public void updateMiningResult(boolean miningDone)
	{
		this.miningDone = miningDone;
	}

	public void fillDrops(NonNullList<ItemStack> list)
	{
		if (list.isEmpty())
			return;
		ArrayList<LargeItemStack> drops = ChainMiningManager.getPlayerMiningDropHolder(this.uuid);
		for (ItemStack stack : list)
		{
			if (stack.isEmpty())
				continue;
			boolean add = false;
			for (LargeItemStack largeItemStack : drops)
				if (Helper.isItemStackSame(stack, largeItemStack.getStack()))
				{
					largeItemStack.increaseAmount(stack.getCount());
					add = true;
					break;
				}
			if (!add)
			{
				drops.add(new LargeItemStack(stack));
			}
		}
		for (LargeItemStack largeItemStack : drops)
		{
			if (!largeItemStack.canPickUp())
				continue;
			this.loopModModify(largeItemStack);
		}
	}

	private void loopItemPickUp(LargeItemStack largeItemStack)
	{
		if (largeItemStack.getAmount() <= 0)
			return;
		ItemStack origin = largeItemStack.getStack().copy();
		boolean canLoop = true;
		while (canLoop)
		{
			ItemStack itemStack = origin.copy();
			int count = Math.min(largeItemStack.getAmount(), 64);
			itemStack.setCount(count);
			canLoop = count > 0 && this.player.inventory.addItemStackToInventory(itemStack);
			largeItemStack.decreaseAmount(count - itemStack.getCount());
			if (!canLoop && count > 0)
				largeItemStack.setNoPickUp();
		}
	}

	private void loopModModify(LargeItemStack largeItemStack)
	{
		ItemStack origin = largeItemStack.getStack().copy();
		boolean hasOrb = Helper.findMaterialOrb(this.player);
		int empty = 0;
		if (!hasOrb)
		{
			empty = this.getPlayerEmptySlot();
			if (empty >= 0)
				this.player.inventory.mainInventory.set(empty, new ItemStack(ItemLoader.materialOrb));
		}
		boolean canLoop = true;
		while (canLoop)
		{
			ItemStack itemStack = origin.copy();
			int count = Math.min(largeItemStack.getAmount(), 64);
			itemStack.setCount(count);
			EntityItem entityItem =
					new EntityItem(this.player.world, this.player.posX, this.player.posY, this.player.posZ, itemStack);
			EntityItemPickupEvent event = new EntityItemPickupEvent(this.player, entityItem);
			MinecraftForge.EVENT_BUS.post(event);
			entityItem.setDead();
			int modify = count - itemStack.getCount();
			largeItemStack.decreaseAmount(modify);
			canLoop = modify != 0 && largeItemStack.getAmount() > 0;
			if (!canLoop)
				this.loopItemPickUp(largeItemStack);
		}
		if (!hasOrb && this.getPlayerEmptySlot() >= 0)
			this.player.inventory.mainInventory.set(empty, ItemStack.EMPTY);
	}

	private int getPlayerEmptySlot()
	{
		NonNullList<ItemStack> stacks = this.player.inventory.mainInventory;
		for (int i = 0; i < stacks.size(); i++)
		{
			if (stacks.get(i).isEmpty())
				return i;
		}
		return -1;
	}

}
