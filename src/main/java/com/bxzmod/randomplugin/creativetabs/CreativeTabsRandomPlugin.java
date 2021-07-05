package com.bxzmod.randomplugin.creativetabs;

import com.bxzmod.randomplugin.item.ItemLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabsRandomPlugin extends CreativeTabs
{

	public CreativeTabsRandomPlugin(String lable)
	{
		super(lable);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return ItemLoader.ring;
	}

}
