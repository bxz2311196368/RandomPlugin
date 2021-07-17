package com.bxzmod.randomplugin.utils;

import static com.bxzmod.randomplugin.registry.ModObjectHolder.*;

public class ModModify
{
	public static void modifyMaxStack()
	{
		bucket.maxStackSize = 64;
		lava_bucket.maxStackSize = 64;
		milk_bucket.maxStackSize = 64;
		water_bucket.maxStackSize = 64;
	}
}
