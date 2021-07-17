package com.bxzmod.randomplugin.utils.slotcontrol;

import com.bxzmod.randomplugin.utils.Helper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemFilters implements INBTSerializable<NBTTagCompound>
{
	protected Set<ItemStackChecker> listFilter = Sets.newHashSet();
	protected boolean whiteList = true;
	protected boolean ignoreMeta = false;
	protected boolean ignoreNBT = false;

	private ItemFilters()
	{
	}

	public ItemFilters(ItemStack... stacks)
	{
		this(true, stacks);
	}

	public ItemFilters(boolean whiteList, ItemStack... stacks)
	{
		this(whiteList, ImmutableList.copyOf(stacks));
	}

	public ItemFilters(Collection<ItemStack> itemStackList)
	{
		this(true, itemStackList);
	}

	public ItemFilters(boolean whiteList, Collection<ItemStack> itemStackList)
	{
		this.whiteList = whiteList;
		this.listFilter.addAll(this.getCheckerList(itemStackList));
	}

	public boolean canAccept(ItemStack itemStack)
	{
		for (ItemStackChecker checker : this.listFilter)
		{
			if (checker.isEqual(itemStack, this.ignoreMeta, this.ignoreNBT))
				return this.whiteList;
		}
		return !this.whiteList;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (ItemStackChecker checker : this.listFilter)
			list.appendTag(checker.stack.serializeNBT());
		compound.setTag("stacks", list);
		compound.setBoolean("whiteList", this.whiteList);
		compound.setBoolean("ignoreMeta", this.ignoreMeta);
		compound.setBoolean("ignoreNBT", this.ignoreNBT);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.whiteList = nbt.getBoolean("whiteList");
		this.ignoreMeta = nbt.getBoolean("ignoreMeta");
		this.ignoreNBT = nbt.getBoolean("ignoreNBT");
		NBTTagList list = nbt.getTagList("stacks", 9);
		for (int i = 0; i < list.tagCount(); i++)
		{
			this.listFilter.add(new ItemStackChecker(list.getCompoundTagAt(i)));
		}
	}

	public List<ItemStackChecker> getCheckerList(Collection<ItemStack> stacks)
	{
		if (stacks.isEmpty())
			return Collections.EMPTY_LIST;
		List<ItemStackChecker> list = Lists.newArrayList();
		for (ItemStack stack : stacks)
			list.add(new ItemStackChecker(stack));
		return list;
	}

	public static ItemFilters getFilterFromNBT(NBTTagCompound nbt)
	{
		ItemFilters checker = new ItemFilters();
		checker.deserializeNBT(nbt);
		return checker;
	}

	public class ItemStackChecker
	{

		ItemStack stack;

		public ItemStackChecker(NBTTagCompound nbt)
		{
			this(new ItemStack(nbt));
		}

		public ItemStackChecker(ItemStack stack)
		{
			this.stack = stack;
		}

		public ItemStack getStack()
		{
			return stack;
		}

		public boolean isEqual(ItemStack stack, boolean ignoreMeta, boolean ignoreNBT)
		{
			return Helper.isItemStackConditionalityEqual(stack, this.stack, ignoreMeta, ignoreNBT);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof ItemStackChecker)
			{
				ItemStackChecker checker = (ItemStackChecker) obj;
				return this.stack == checker.stack || Helper.isItemStackSame(this.stack, checker.stack);
			}
			return this == obj;
		}

	}
}
