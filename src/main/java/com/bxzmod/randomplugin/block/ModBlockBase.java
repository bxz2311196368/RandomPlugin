package com.bxzmod.randomplugin.block;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.block.itemblock.ItemBlockBase;
import com.bxzmod.randomplugin.creativetab.CreativeTabLoader;
import com.bxzmod.randomplugin.registry.RegistryHandler;
import com.bxzmod.randomplugin.utils.IRecipeRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public abstract class ModBlockBase extends BlockContainer implements IRecipeRegistry
{
	private ItemBlock itemBlock;

	public ModBlockBase(Material blockMaterialIn, MapColor blockMapColorIn, String registryName, String unlocalizedName)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.addRegistryInfo(registryName, unlocalizedName);
		this.setCreativeTab(CreativeTabLoader.creativetab);
	}

	public ModBlockBase(Material materialIn, String registryName, String unlocalizedName)
	{
		this(materialIn, materialIn.getMaterialMapColor(), registryName, unlocalizedName);
	}

	public ModBlockBase(String registryName, String unlocalizedName)
	{
		this(Material.GROUND, registryName, unlocalizedName);
	}

	public void addRegistryInfo(String registryName, String unlocalizedName)
	{
		RegistryHandler.BLOCKS.add(this);
		this.setRegistryName(ModInfo.MODID, registryName);
		this.setTranslationKey(unlocalizedName);
		this.setItemBlock(new ItemBlockBase(this));
	}

	public ItemBlock getItemBlock()
	{
		return itemBlock;
	}

	public void setItemBlock(ItemBlock itemBlock)
	{
		this.itemBlock = itemBlock;
	}

	@Override
	public String getTranslationKey()
	{
		return ModInfo.MODID + "." + super.getTranslationKey();
	}

}
