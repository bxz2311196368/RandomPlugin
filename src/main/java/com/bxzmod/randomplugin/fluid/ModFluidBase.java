package com.bxzmod.randomplugin.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.awt.*;

public class ModFluidBase extends Fluid
{
	public ModFluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing,
			@Nullable ResourceLocation overlay, Color color)
	{
		super(fluidName, still, flowing, overlay, color);
	}
}
