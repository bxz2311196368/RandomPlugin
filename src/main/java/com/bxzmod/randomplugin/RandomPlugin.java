package com.bxzmod.randomplugin;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModInfo.MODID)
public class RandomPlugin
{
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

	public static RandomPlugin INSTANCE;

	public RandomPlugin()
	{
		INSTANCE = this;
		new ModRegister();
	}

}
