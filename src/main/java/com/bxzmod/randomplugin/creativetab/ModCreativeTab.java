package com.bxzmod.randomplugin.creativetab;

import com.bxzmod.randomplugin.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTab extends CreativeTabs
{
	public ItemStack stack;

	public ModCreativeTab(String label, ItemStack icon)
	{
		super(ModInfo.MODID + "." + label);
		this.stack = icon;
	}

	@Override
	public ItemStack createIcon()
	{
		return this.stack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslationKey()
	{
		return this.getTabLabel();
	}
}
