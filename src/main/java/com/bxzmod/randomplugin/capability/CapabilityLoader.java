package com.bxzmod.randomplugin.capability;

import com.bxzmod.randomplugin.capability.capinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.capability.capinterface.ISideEnergy;
import com.bxzmod.randomplugin.utils.sidecontrol.SideEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityLoader
{
	@CapabilityInject(IMaterialOrbHolder.class)
	public static Capability<IMaterialOrbHolder> MATERIAL_ORB_HOLDER = null;

	@CapabilityInject(ISideEnergy.class)
	public static Capability<ISideEnergy> SIDE_ENERGY = null;

	public CapabilityLoader()
	{
		CapabilityManager.INSTANCE.register(IMaterialOrbHolder.class, new MaterialOrbHolder.Storage(),
				MaterialOrbHolder.Implementation::new);
		CapabilityManager.INSTANCE.register(ISideEnergy.class, new SideEnergyHolder.Storage(), SideEnergyStorage::new);
	}
}
