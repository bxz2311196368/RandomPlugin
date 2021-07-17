package com.bxzmod.randomplugin.entity;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.registry.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class EntityLoader
{
	public static int ID = 1;

	public static void addEntityEntry(Class<? extends Entity> clazz, String registryName, String unlocalizedName)
	{
		RegistryHandler.ENTITIES.add(EntityEntryBuilder.create().entity(clazz)
				.id(new ResourceLocation(ModInfo.MODID, registryName), ID++).name(unlocalizedName).tracker(80, 3, false)
				.build());
	}

}
