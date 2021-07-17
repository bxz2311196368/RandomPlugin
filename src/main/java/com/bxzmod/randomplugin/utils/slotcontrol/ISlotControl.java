package com.bxzmod.randomplugin.utils.slotcontrol;

import net.minecraft.item.ItemStack;

public interface ISlotControl
{
	ISlotControl setSlotFilter(int slot, ItemFilters itemChecker);

	boolean canSlotAccept(int slot, ItemStack stack);
}
