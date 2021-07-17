package com.bxzmod.randomplugin.utils.sidecontrol;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class SideControl implements INBTSerializable<NBTTagCompound>
{
	protected HashMap<EnumFacing, EnumSideMode> sideMap = Maps.newHashMap();

	public SideControl()
	{
		this.setAllSide(EnumSideMode.ALL);
	}

	public SideControl(EnumSideMode mode)
	{
		this.setAllSide(mode);
	}

	protected void setAllSide(EnumSideMode mode)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
			this.sideMap.put(facing, mode);
	}

	public void setSide(EnumFacing facing, EnumSideMode mode)
	{
		this.sideMap.put(facing, mode);
	}

	protected EnumSideMode getSide(EnumFacing facing)
	{
		return this.sideMap.get(facing);
	}

	public boolean canExtract(EnumFacing side)
	{
		return this.sideMap.get(side).canExtract();
	}

	public boolean canReceive(EnumFacing side)
	{
		return this.sideMap.get(side).canReceive();
	}

	public void cycleSide(EnumFacing side)
	{
		EnumSideMode mode = this.sideMap.get(side);
		this.sideMap.put(side, mode.next());
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		for (EnumFacing facing : EnumFacing.VALUES)
			compound.setInteger(facing.getName(), this.sideMap.get(facing).ordinal());
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
			this.sideMap.put(facing, EnumSideMode.values()[nbt.getInteger(facing.getName())]);
	}
}
