package com.bxzmod.randomplugin.utils.sidecontrol;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class SideFluidTank implements INBTSerializable<NBTTagCompound>
{
	protected ArrayList<FluidTank> fluidTanks = Lists.newArrayList();

	@Override
	public NBTTagCompound serializeNBT()
	{
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

	public static class SideFluidProxy implements IFluidHandler
	{
		protected EnumFacing side;
		protected SideFluidTank storage;

		public SideFluidProxy(EnumFacing side, SideFluidTank storage)
		{
			this.side = side;
			this.storage = storage;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return new IFluidTankProperties[0];
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return null;
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return null;
		}
	}
}
