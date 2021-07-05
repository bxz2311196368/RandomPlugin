package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.Info;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import java.util.List;
import java.util.Set;

public class ModGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return ModConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}

	public static class ModConfigGui extends GuiConfig
	{

		public ModConfigGui(GuiScreen parentScreen)
		{
			super(parentScreen, getConfigElements(), Info.MODID, false, false,
					I18n.format("config.gui." + Info.MODID + ".title"));
		}

		private static List<IConfigElement> getConfigElements()
		{
			List<IConfigElement> list = Lists.newArrayList();
			for (String name : ConfigLoader.config.getCategoryNames())
				list.add(new ConfigElement(ConfigLoader.config.getCategory(name)));
			return list;
		}
	}
}
