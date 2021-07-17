package com.bxzmod.randomplugin.utils;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface IRecipeRegistry
{
	boolean hasRecipe();

	List<IRecipe> getRecipes();

}
