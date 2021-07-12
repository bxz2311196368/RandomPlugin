package com.bxzmod.randomplugin.item;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemLoader
{
	public static Item ring = new Ring();
	public static Item materialOrb = new MaterialOrb();
	public static Item sync_data = new SyncData();
	public static Item limitless_tool = new UniversalTool();

	public ItemLoader(FMLPreInitializationEvent event)
	{
		register(ring, "ring");
		register(materialOrb, "material_orb");
		register(sync_data, "sync_data");
		register(limitless_tool, "limitless_tool");
	}

	private static void register(Item item, String name)
	{
		GameRegistry.registerItem(item, name);
	}

}
