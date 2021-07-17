package com.bxzmod.randomplugin.utils.sidecontrol;

import com.bxzmod.randomplugin.capability.capinterface.ISideEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class SideEnergyStorage implements ISideEnergy, INBTSerializable<NBTTagCompound>
{
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	protected SideControl sideControl;

	public SideEnergyStorage()
	{
		this(1000000);
	}

	public SideEnergyStorage(int capacity)
	{
		this(0, capacity);
	}

	public SideEnergyStorage(int energy, int capacity)
	{
		this(energy, capacity, new SideControl());
	}

	public SideEnergyStorage(int energy, int capacity, SideControl sideControl)
	{
		this(energy, capacity, capacity, capacity, sideControl);
	}

	public SideEnergyStorage(int energy, int capacity, int maxReceive, int maxExtract, SideControl sideControl)
	{
		this.energy = energy;
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.sideControl = sideControl;
	}

	public IEnergyStorage getSideProxy(EnumFacing side)
	{
		if (side != null)
			return new SideEnergyProxy(this, side);
		return new SideEnergyProxy(this, EnumFacing.NORTH);
	}

	@Override
	public int receiveEnergy(EnumFacing side, int maxReceive, boolean simulate)
	{
		if (!canReceive(side))
			return 0;

		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if (!simulate)
			energy += energyReceived;
		return energyReceived;
	}

	@Override
	public int extractEnergy(EnumFacing side, int maxExtract, boolean simulate)
	{
		if (!canExtract(side))
			return 0;

		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate)
			energy -= energyExtracted;
		return energyExtracted;
	}

	@Override
	public boolean canExtract(EnumFacing side)
	{
		return this.sideControl.canExtract(side);
	}

	@Override
	public boolean canReceive(EnumFacing side)
	{
		return this.sideControl.canReceive(side);
	}

	@Override
	public int getEnergyStored()
	{
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return this.capacity;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("energy", this.energy);
		nbt.setInteger("capacity", this.capacity);
		nbt.setTag("IO", this.sideControl.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.energy = nbt.getInteger("energy");
		this.capacity = nbt.getInteger("capacity");
		this.sideControl.deserializeNBT(nbt.getCompoundTag("IO"));
	}

	public static class SideEnergyProxy implements IEnergyStorage
	{
		SideEnergyStorage storage;
		EnumFacing side;

		public SideEnergyProxy(SideEnergyStorage storage, EnumFacing side)
		{
			this.storage = storage;
			this.side = side;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			return this.storage.receiveEnergy(this.side, maxReceive, simulate);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			return this.storage.extractEnergy(this.side, maxExtract, simulate);
		}

		@Override
		public int getEnergyStored()
		{
			return this.storage.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored()
		{
			return this.storage.getMaxEnergyStored();
		}

		@Override
		public boolean canExtract()
		{
			return this.storage.canExtract(this.side);
		}

		@Override
		public boolean canReceive()
		{
			return this.storage.canReceive(this.side);
		}
	}
}
