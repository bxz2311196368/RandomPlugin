package com.bxzmod.randomplugin.utils;

import net.minecraft.item.ItemStack;

public class LargeItemStack
{
	private ItemStack stack;
	private int amount;

	private boolean pickUp = true;

	public LargeItemStack(ItemStack stack, int amount)
	{
		this.stack = stack.copy();
		this.stack.setCount(1);
		this.amount = amount;
	}

	public LargeItemStack(ItemStack stack)
	{
		this(stack, stack.getCount());
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	public int getAmount()
	{
		return amount;
	}

	public int increaseAmount(int amount)
	{
		if (amount < 0)
			return 0;
		this.amount += amount;
		return amount;
	}

	public int decreaseAmount(int amount)
	{
		if (amount < 0)
			return 0;
		int temp = this.amount;
		this.amount -= amount;
		if (this.amount < 0)
			this.amount = 0;
		return this.amount == 0 ? temp : amount;
	}

	public boolean canPickUp()
	{
		return this.pickUp;
	}

	public void setNoPickUp()
	{
		this.pickUp = false;
	}
}
