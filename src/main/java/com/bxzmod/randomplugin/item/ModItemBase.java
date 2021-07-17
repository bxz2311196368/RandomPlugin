package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.creativetab.CreativeTabLoader;
import com.bxzmod.randomplugin.registry.RegistryHandler;
import com.bxzmod.randomplugin.utils.IRecipeRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ModItemBase extends Item implements IRecipeRegistry
{

	public ModItemBase(String registryName)
	{
		this(registryName, registryName);
	}

	public ModItemBase(String registryName, String unlocalizedName)
	{
		super();
		this.addRegistryInfo(registryName, unlocalizedName);
		this.setCreativeTab(CreativeTabLoader.creativetab);
		if (this.hasRecipe())
			RegistryHandler.RECIPES.addAll(this.getRecipes());
	}

	public void addRegistryInfo(String registryName, String unlocalizedName)
	{
		RegistryHandler.ITEMS.add(this);
		this.setRegistryName(ModInfo.MODID, registryName);
		this.setTranslationKey(unlocalizedName);
	}

	@Override
	public String getTranslationKey()
	{
		return ModInfo.MODID + "." + super.getTranslationKey();
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return ModInfo.MODID + "." + super.getTranslationKey(stack);
	}
}
