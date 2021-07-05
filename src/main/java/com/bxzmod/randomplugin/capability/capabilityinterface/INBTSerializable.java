package com.bxzmod.randomplugin.capability.capabilityinterface;

import net.minecraft.nbt.NBTBase;

public interface INBTSerializable<T extends NBTBase>
{
	T serializeNBT();

	void deserializeNBT(T var1);
}
