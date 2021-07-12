package com.bxzmod.randomplugin.recipes;

import com.bxzmod.randomplugin.item.ItemLoader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CraftingLoader
{
	public final static ItemStack limitless_tool_with_nbt = new ItemStack(ItemLoader.limitless_tool);
	public final static ItemStack limitless_tool_with_nbt1 = new ItemStack(ItemLoader.limitless_tool);

	static
	{
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		limitless_tool_with_nbt.addEnchantment(Enchantment.looting, 10);
		limitless_tool_with_nbt.addEnchantment(Enchantment.fortune, 10);
		limitless_tool_with_nbt1.addEnchantment(Enchantment.looting, 10);
		limitless_tool_with_nbt1.addEnchantment(Enchantment.silkTouch, 1);
		limitless_tool_with_nbt1.getTagCompound().setTag("dig_parameter", dig);
		limitless_tool_with_nbt1.getTagCompound().setTag("dig_parameter", dig);
	}

	public CraftingLoader(FMLInitializationEvent event)
	{
		registerRecipe();
		fixIngotToBlock();
	}

	private static void registerRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.ring), " # ", "#*#", " # ", '#', Items.nether_star, '*',
				Item.getItemFromBlock(Blocks.dragon_egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.sync_data), Items.book, Blocks.sand);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.materialOrb), ItemLoader.materialOrb);
		GameRegistry.addRecipe(
				new ShapedOreRecipe(limitless_tool_with_nbt, "PAS", "WDH", " # ", 'P', Items.diamond_pickaxe, 'A',
						Items.diamond_axe, 'S', Items.diamond_shovel, 'W', Items.diamond_sword, 'D', "blockDiamond",
						'H', Items.diamond_hoe, '#', Items.shears));
		GameRegistry.addRecipe(
				new ShapedOreRecipe(limitless_tool_with_nbt1, "SAP", "WDH", " # ", 'P', Items.diamond_pickaxe, 'A',
						Items.diamond_axe, 'S', Items.diamond_shovel, 'W', Items.diamond_sword, 'D', "blockDiamond",
						'H', Items.diamond_hoe, '#', Items.shears));
	}

	private static void fixIngotToBlock()
	{
		for (String ore : OreDictionary.getOreNames())
		{
			if (ore.startsWith("ingot"))
			{

				String oreName = StringUtils.substringAfter(ore, "ingot");
				String blockName = "block" + oreName;
				if (OreDictionary.doesOreNameExist(blockName))
				{
					List<ItemStack> ingots = OreDictionary.getOres(ore, false);
					List<ItemStack> blocks = OreDictionary.getOres(blockName, false);
					if (ingots.isEmpty() || blocks.isEmpty())
						continue;
					for (ItemStack block : blocks)
						GameRegistry
								.addRecipe(new ShapelessOreRecipe(block, ore, ore, ore, ore, ore, ore, ore, ore, ore));
					for (ItemStack ingot : ingots)
					{
						ItemStack result = ingot.copy();
						result.stackSize = 9;
						GameRegistry.addRecipe(new ShapelessOreRecipe(result, blockName));
					}
				}
			}
		}
	}

}
