package com.bxzmod.randomplugin.utils.chainmining;

import com.bxzmod.randomplugin.Main;
import com.bxzmod.randomplugin.config.ChainMineConfig;
import com.bxzmod.randomplugin.events.MiningTickEventHandler;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.utils.*;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import javax.vecmath.Point3i;
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
	private final Point3i blockPos;
	private final Block targetBlock;
	private final ArrayDeque<Point3i> next_blocks = new ArrayDeque<Point3i>();
	private final ArrayDeque<Point3i> harvestBlockList = new ArrayDeque<Point3i>();
	private final HashSet<Point3i> skipBlockList = Sets.newHashSet();
	private ItemStack targetItemBlock = null;

	public PlayerChainMining(EntityPlayerMP player, Point3i blockPos)
	{
		this.player = player;
		this.uuid = player.getGameProfile().getId();
		this.targetBlock = player.worldObj.getBlock(blockPos.x, blockPos.y, blockPos.z);
		this.blockPos = blockPos;
		this.initBlockList(this.blockPos);
		this.findTargetItemResult(this.targetBlock, this.blockPos);
	}

	private void initBlockList(Point3i pos)
	{
		this.next_blocks.addLast(pos);
		this.harvestBlockList.addLast(pos);
		this.skipBlockList.add(pos);
	}

	@Override
	public void run()
	{
		try
		{
			SimulatePlayer harvest =
					new SimulatePlayer.Builder(this.player, (WorldServer) this.player.worldObj, this.blockPos.x,
							this.blockPos.y, this.blockPos.z).fromConfig().noDrop().playerTool().create();

			EnumFacing facing = EnumFacing.getFront(ModPlayerData.getDataByPlayer(this.player).getFacing());
			boolean faceOffset = ModPlayerData.getDataByPlayer(this.player).isChainMineFaceOffset();
			int offsetX = faceOffset ? -facing.getFrontOffsetX() : 0, offsetY =
					faceOffset ? -facing.getFrontOffsetY() : 0, offsetZ = faceOffset ? -facing.getFrontOffsetZ() : 0;
			int yLimit = this.blockPos.y;
			while (ChainMiningManager.canChainMiningContinue(this.uuid))
			{
				this.findTargetBlock(facing, offsetX, offsetY, offsetZ, yLimit);
				if (!this.harvestBlockList.isEmpty())
					MiningTickEventHandler.addTask(new MiningTask(this, harvest, this.uuid, this.harvestBlockList));
				else
				{
					break;
				}
				if (!this.waitMiningResult())
				{
					Main.LOGGER.error("player:" + this.player.getDisplayName()
							+ ":server no response in 20 tick, maybe server lag, skip mining work!");
					break;
				}
				if (this.next_blocks.isEmpty() || this.totalBlockCount >= ChainMineConfig.MAX_VALUE)
				{
					break;
				}
			}
			this.stopTask(harvest);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void stopTask(SimulatePlayer harvest)
	{
		this.fillDrops(harvest.getDropList());
		ChainMiningManager.stopPlayerMining(this.player);
	}

	private void findTargetBlock(EnumFacing facing, int offsetX, int offsetY, int offsetZ, int yLimit)
	{
		int checkCount = 0;
		boolean limitY = facing.getFrontOffsetY() == 0;
		while (this.miningDone && checkCount < ChainMineConfig.MAX_PER_TICK)
		{
			if (this.next_blocks.isEmpty() || this.totalBlockCount >= ChainMineConfig.MAX_VALUE)
				break;
			Point3i next = this.next_blocks.removeFirst();
			for (int x = -1 + offsetX; x < 2 + offsetX; x++)
			{
				for (int y = -1 + offsetY; y < 2 + offsetY; y++)
				{
					if (next.y + y < 0 || next.y + y >= 255)
						continue;
					if (limitY && next.y < yLimit)
						continue;
					for (int z = -1 + offsetZ; z < 2 + offsetZ; z++)
					{
						if (x == 0 && y == 0 && z == 0)
							continue;
						Point3i temp = Helper.offsetPos(next, x, y, z);
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

	private boolean isBlockStateEqual(Point3i pos)
	{
		Block block = this.player.worldObj.getBlock(pos.x, pos.y, pos.z);
		MovingObjectPosition result = new MovingObjectPosition(0, 0, 0, EnumFacing.UP.ordinal(),
				Vec3.createVectorHelper(pos.x, pos.y, pos.z));
		return Helper.isItemStackSame(this.targetItemBlock,
				this.getPickBlock(this.player.worldObj, pos.x, pos.y, pos.z, block));
	}

	private void findTargetItemResult(Block block, Point3i pos)
	{
		MovingObjectPosition result = new MovingObjectPosition(0, 0, 0, EnumFacing.UP.ordinal(),
				Vec3.createVectorHelper(pos.x, pos.y, pos.z));
		this.targetItemBlock = this.getPickBlock(this.player.worldObj, pos.x, pos.y, pos.z, this.targetBlock);
		if (this.targetItemBlock == null)
		{
			ChainMiningManager.stopPlayerMining(this.player);
		}

	}

	private ItemStack getPickBlock(World world, int x, int y, int z, Block block)
	{
		Item item = Item.getItemFromBlock(block);
		if (block instanceof BlockRedstoneOre)
			item = Item.getItemFromBlock(Blocks.redstone_ore);
		return new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
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

	public void fillDrops(ArrayList<ItemStack> list)
	{
		if (list.isEmpty())
			return;
		ArrayList<LargeItemStack> drops = ChainMiningManager.getPlayerMiningDropHolder(this.uuid);
		for (ItemStack stack : list)
		{
			if (stack == null)
				continue;
			boolean add = false;
			for (LargeItemStack largeItemStack : drops)
				if (Helper.isItemStackSame(stack, largeItemStack.getStack()))
				{
					largeItemStack.increaseAmount(stack.stackSize);
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
			itemStack.stackSize = count;
			canLoop = count > 0 && this.player.inventory.addItemStackToInventory(itemStack);
			largeItemStack.decreaseAmount(count - itemStack.stackSize);
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
				this.player.inventory.mainInventory[empty] = new ItemStack(ItemLoader.materialOrb);
		}
		boolean canLoop = true;
		while (canLoop)
		{
			ItemStack itemStack = origin.copy();
			int count = Math.min(largeItemStack.getAmount(), 64);
			itemStack.stackSize = count;
			EntityItem entityItem =
					new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ,
							itemStack);
			EntityItemPickupEvent event = new EntityItemPickupEvent(this.player, entityItem);
			MinecraftForge.EVENT_BUS.post(event);
			entityItem.setDead();
			int modify = count - itemStack.stackSize;
			largeItemStack.decreaseAmount(modify);
			canLoop = modify != 0 && largeItemStack.getAmount() > 0;
			if (!canLoop)
				this.loopItemPickUp(largeItemStack);
		}
		if (!hasOrb && this.getPlayerEmptySlot() >= 0)
			this.player.inventory.mainInventory[empty] = null;
	}

	private int getPlayerEmptySlot()
	{
		ItemStack[] stacks = this.player.inventory.mainInventory;
		for (int i = 0; i < stacks.length; i++)
		{
			if (stacks[i] == null)
				return i;
		}
		return -1;
	}

}
