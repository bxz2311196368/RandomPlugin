package com.bxzmod.randomplugin.capability;

import com.bxzmod.randomplugin.capability.capinterface.ISideEnergy;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SideEnergyHolder
{
	public static class Storage implements Capability.IStorage<ISideEnergy>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<ISideEnergy> capability, ISideEnergy instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ISideEnergy> capability, ISideEnergy instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
