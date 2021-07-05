package com.bxzmod.randomplugin.recipes;

import com.bxzmod.randomplugin.item.ItemLoader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftingLoader
{
	public CraftingLoader(FMLInitializationEvent event)
	{
		registerRecipe();
	}

	private static void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.ring), " # ", "#*#", " # ", '#', Items.nether_star, '*',
				Item.getItemFromBlock(Blocks.dragon_egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.sync_data), Items.book);
	}
}
