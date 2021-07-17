package com.bxzmod.randomplugin.asm;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "com.bxzmod.randomplugin.asm" })
public class ModFMLLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean isDev = false;

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}

	@Override
	public String getModContainerClass()
	{
		return CoreModContainer.class.getName();
	}

	@Nullable
	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		isDev = !(boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	public static class CoreModContainer extends DummyModContainer
	{
		public CoreModContainer()
		{
			super(new ModMetadata());
			ModMetadata meta = getMetadata();
			meta.modId = "bxzasm";
			meta.name = "BXZ ASM";
			meta.description = "does nothing";
			meta.version = "1.12.2-1.0";
			meta.authorList = Arrays.asList("BXZ");
		}

		@Override
		public boolean registerBus(EventBus bus, LoadController controller)
		{
			//bus.register(this);
			return true;
		}
	}
}
