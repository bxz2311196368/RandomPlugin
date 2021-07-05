package com.bxzmod.randomplugin.capability;

import com.bxzmod.randomplugin.capability.capabilityinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.utils.Helper;
import com.bxzmod.randomplugin.utils.LargeItemStack;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MaterialOrbHolder implements IMaterialOrbHolder
{
	private final LinkedList<LargeItemStack> itemStacks = Lists.newLinkedList();

	@Override
	public void addItems(List<LargeItemStack> itemStacks)
	{
		if (this.itemStacks.isEmpty())
			this.itemStacks.addAll(itemStacks);
		else
		{
			List<LargeItemStack> temp = Lists.newArrayList();
			for (LargeItemStack newStack : itemStacks)
			{
				boolean edit = false;
				for (LargeItemStack oldStack : this.itemStacks)
				{
					if (Helper.isItemStackSame(oldStack.getStack(), newStack.getStack()))
					{
						oldStack.increaseAmount(newStack.decreaseAmount(newStack.getAmount()));
						edit = true;
						break;
					}
				}
				if (!edit)
					temp.add(newStack);
			}
			if (!temp.isEmpty())
				this.itemStacks.addAll(temp);
		}
	}

	@Override
	public void dropStack(World world, double x, double y, double z, ItemStack itemStack, boolean dropAll)
	{
		this.reSyncData(itemStack);
		while (!this.itemStacks.isEmpty())
		{
			LargeItemStack largeItemStack = this.itemStacks.getFirst();
			ItemStack stack = largeItemStack.getStack().copy();
			stack.stackSize = largeItemStack.decreaseAmount(Math.min(64, largeItemStack.getAmount()));
			EntityItem entityItem = new EntityItem(world, x, y, z, stack);
			entityItem.delayBeforeCanPickup = 0;
			if (!world.isRemote)
				world.spawnEntityInWorld(entityItem);
			if (largeItemStack.getAmount() <= 0)
				this.itemStacks.removeFirst();
			if (!dropAll)
				break;
		}
		this.reSyncNBT(itemStack);
		if (this.itemStacks.isEmpty())
			itemStack.stackSize = 0;
	}

	@Nonnull
	@Override
	public List getTooltip(ItemStack stack)
	{
		this.reSyncData(stack);
		if (this.itemStacks.isEmpty())
			return Collections.EMPTY_LIST;

		List<String> toolTip = Lists.newArrayList();
		for (LargeItemStack largeItemStack : this.itemStacks)
			toolTip.add(largeItemStack.getStack().getDisplayName() + ":" + largeItemStack.getAmount());
		return toolTip;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		NBTTagList nbtTagListItem = new NBTTagList();
		int[] counts = new int[this.itemStacks.size()];
		int i = 0;
		for (LargeItemStack largeItemStack : this.itemStacks)
		{
			nbtTagListItem.appendTag(largeItemStack.getStack().writeToNBT(new NBTTagCompound()));
			counts[i++] = largeItemStack.getAmount();
		}
		NBTTagIntArray nbtTagIntArray = new NBTTagIntArray(counts);
		nbtTagCompound.setTag("Item", nbtTagListItem);
		nbtTagCompound.setTag("Count", nbtTagIntArray);
		return nbtTagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if (nbt == null)
			return;
		NBTTagList nbtTagListItem = (NBTTagList) nbt.getTag("Item");
		NBTTagIntArray nbtTagIntArray = (NBTTagIntArray) nbt.getTag("Count");
		int[] counts = nbtTagIntArray.func_150302_c();
		int size = nbtTagListItem.tagCount();
		this.itemStacks.clear();
		for (int i = 0; i < size; i++)
			this.itemStacks.addLast(new LargeItemStack(Helper.fromNBT(nbtTagListItem.getCompoundTagAt(i)), counts[i]));
	}

	private void reSyncData(ItemStack stack)
	{
		if (stack.hasTagCompound())
			this.deserializeNBT(stack.getTagCompound());
	}

	private void reSyncNBT(ItemStack itemStack)
	{
		itemStack.setTagCompound(this.serializeNBT());
	}

	public static IMaterialOrbHolder fromNBT(ItemStack itemStack)
	{
		IMaterialOrbHolder holder = new MaterialOrbHolder();
		if (itemStack.hasTagCompound())
			holder.deserializeNBT(itemStack.stackTagCompound);
		return holder;

	}
}
