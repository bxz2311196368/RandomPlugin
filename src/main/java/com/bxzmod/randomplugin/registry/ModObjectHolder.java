package com.bxzmod.randomplugin.registry;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModObjectHolder
{
	@GameRegistry.ObjectHolder("minecraft:haste")
	public static Potion digSpeed;

	@GameRegistry.ObjectHolder("minecraft:regeneration")
	public static Potion regeneration;

	@GameRegistry.ObjectHolder("minecraft:resistance")
	public static Potion resistance;

	@GameRegistry.ObjectHolder("minecraft:fire_resistance")
	public static Potion fire_resistance;

	@GameRegistry.ObjectHolder("minecraft:water_breathing")
	public static Potion water_breathing;

	@GameRegistry.ObjectHolder("minecraft:saturation")
	public static Potion saturation;

	@GameRegistry.ObjectHolder("minecraft:water_bucket")
	public static Item water_bucket;

	@GameRegistry.ObjectHolder("minecraft:lava_bucket")
	public static Item lava_bucket;

	@GameRegistry.ObjectHolder("minecraft:bucket")
	public static Item bucket;

	@GameRegistry.ObjectHolder("minecraft:milk_bucket")
	public static Item milk_bucket;
}

