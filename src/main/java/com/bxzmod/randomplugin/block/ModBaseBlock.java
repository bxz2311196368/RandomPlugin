package com.bxzmod.randomplugin.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBaseBlock extends Block
{
	public ModBaseBlock(Properties properties)
	{
		super(properties);
	}

	public static class ModBlockProperty
	{
		public static final Properties DEFAULT = Properties.create(Material.ROCK);
	}
}
