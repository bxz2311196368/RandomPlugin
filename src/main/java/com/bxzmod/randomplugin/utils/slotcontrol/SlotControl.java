package com.bxzmod.randomplugin.utils.slotcontrol;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public class SlotControl implements ISlotControl, INBTSerializable<NBTTagCompound>
{
	protected Map<Integer, ItemFilters> limitSlot = Maps.newLinkedHashMap();

	private boolean builtin = true;

	public void setBuiltin(boolean builtin)
	{
		this.builtin = builtin;
	}

	public boolean isBuiltin()
	{
		return builtin;
	}

	@Override
	public ISlotControl setSlotFilter(int slot, ItemFilters itemFilters)
	{
		this.limitSlot.put(slot, itemFilters);
		return this;
	}

	@Override
	public boolean canSlotAccept(int slot, ItemStack stack)
	{
		if (this.limitSlot.containsKey(slot))
			return this.limitSlot.get(slot).canAccept(stack);
		return true;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (Integer integer : this.limitSlot.keySet())
		{
			NBTTagCompound filter = new NBTTagCompound();
			filter.setInteger("slot", integer);
			filter.setTag("filter", this.limitSlot.get(integer).serializeNBT());
		}
		nbt.setTag("slots", list);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("slots", 9);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound filter = list.getCompoundTagAt(i);
			this.limitSlot
					.put(filter.getInteger("slot"), ItemFilters.getFilterFromNBT(filter.getCompoundTag("filter")));
		}
	}
}
