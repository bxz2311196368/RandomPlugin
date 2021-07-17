package com.bxzmod.randomplugin.capability.capinterface;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISideEnergy extends INBTSerializable<NBTTagCompound>
{
	int receiveEnergy(EnumFacing side, int maxReceive, boolean simulate);

	int extractEnergy(EnumFacing side, int maxExtract, boolean simulate);

	boolean canExtract(EnumFacing side);

	boolean canReceive(EnumFacing side);

	int getEnergyStored();

	int getMaxEnergyStored();
}
