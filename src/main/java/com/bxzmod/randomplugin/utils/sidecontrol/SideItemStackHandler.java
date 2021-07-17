package com.bxzmod.randomplugin.utils.sidecontrol;

import com.bxzmod.randomplugin.utils.slotcontrol.SlotControl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class SideItemStackHandler implements INBTSerializable<NBTTagCompound>
{
	protected NonNullList<ItemStack> stacks;
	protected SideControl sideControl;
	protected SlotControl slotControl;//TODO: 添加物品检测

	public SideItemStackHandler()
	{
		this(1);
	}

	public SideItemStackHandler(int size)
	{
		this(size, new SideControl(), new SlotControl());
	}

	public SideItemStackHandler(int size, SideControl sideControl)
	{
		this(size, sideControl, new SlotControl());
	}

	public SideItemStackHandler(int size, SlotControl slotControl)
	{
		this(size, new SideControl(), slotControl);
	}

	public SideItemStackHandler(int size, SideControl sideControl, SlotControl slotControl)
	{
		this(NonNullList.withSize(size, ItemStack.EMPTY), sideControl, slotControl);
	}

	public SideItemStackHandler(NonNullList<ItemStack> stacks)
	{
		this(stacks, new SideControl(), new SlotControl());
	}

	public SideItemStackHandler(NonNullList<ItemStack> stacks, SideControl sideControl, SlotControl slotControl)
	{
		this.stacks = stacks;
		this.sideControl = sideControl;
		this.slotControl = slotControl;
	}

	public IItemHandler getSideProxy(EnumFacing side)
	{
		return new SideItemStackProxy(side, this);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
		validateSlotIndex(slot);
		this.stacks.set(slot, stack);
	}

	public int getSlots()
	{
		return this.stacks.size();
	}

	@Nonnull
	public ItemStack getStackInSlot(int slot)
	{
		validateSlotIndex(slot);
		return this.stacks.get(slot);
	}

	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate, EnumFacing side)
	{
		if (!this.sideControl.canReceive(side))
			return stack;
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		this.validateSlotIndex(slot);
		ItemStack existing = this.stacks.get(slot);
		int limit = Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
		if (!existing.isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
				return stack;
			limit -= existing.getCount();
		}
		boolean reachedLimit = stack.getCount() > limit;
		if (!simulate)
		{
			if (existing.isEmpty())
			{
				this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else
			{
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
		}
		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate, EnumFacing side)
	{
		if (!this.sideControl.canExtract(side))
			return ItemStack.EMPTY;
		if (amount == 0)
			return ItemStack.EMPTY;
		validateSlotIndex(slot);
		ItemStack existing = this.stacks.get(slot);
		if (existing.isEmpty())
			return ItemStack.EMPTY;
		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemStack.EMPTY);
			}
			return existing;
		} else
		{
			if (!simulate)
			{
				this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
			}
			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	protected void validateSlotIndex(int slot)
	{
		if (slot < 0 || slot >= stacks.size())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
	}

	public int getSlotLimit(int slot)
	{
		return 64;
	}

	public static class SideItemStackProxy implements IItemHandler, IItemHandlerModifiable
	{
		protected EnumFacing side;
		SideItemStackHandler storage;

		public SideItemStackProxy(EnumFacing side, SideItemStackHandler storage)
		{
			this.side = side;
			this.storage = storage;
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack)
		{
			this.storage.setStackInSlot(slot, stack);
		}

		@Override
		public int getSlots()
		{
			return this.storage.getSlots();
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int slot)
		{
			return this.storage.getStackInSlot(slot);
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			return this.storage.insertItem(slot, stack, simulate, this.side);
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return this.storage.extractItem(slot, amount, simulate, this.side);
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return this.storage.getSlotLimit(slot);
		}
	}
}
