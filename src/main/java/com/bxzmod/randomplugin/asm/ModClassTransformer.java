package com.bxzmod.randomplugin.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class ModClassTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		return basicClass;
	}
}
