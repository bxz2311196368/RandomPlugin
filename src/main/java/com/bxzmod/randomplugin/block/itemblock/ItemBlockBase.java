package com.bxzmod.randomplugin.block.itemblock;

import com.bxzmod.randomplugin.block.ModBlockBase;
import com.bxzmod.randomplugin.registry.RegistryHandler;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock
{
	public ModBlockBase blockIn;

	public ItemBlockBase(ModBlockBase block)
	{
		super(block);
		this.blockIn = block;
		this.addRegistryInfo(block.getRegistryName().toString());
	}

	public void addRegistryInfo(String registryName)
	{
		RegistryHandler.ITEMS.add(this);
		this.setRegistryName(this.blockIn.getRegistryName());
		if (this.blockIn.hasRecipe())
			RegistryHandler.RECIPES.addAll(this.blockIn.getRecipes());
	}
}
