package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.ModInfo;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModConfigGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}

	@Override
	public boolean hasConfigGui()
	{
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		List<IConfigElement> list = Lists.newArrayList();
		Configuration config = ModConfig.getModConfig();
		for (String name : config.getCategoryNames())
			list.add(new ConfigElement(config.getCategory(name)));
		if (list.isEmpty())
			list.add(ConfigElement.from(ModConfig.ChainMineConfig.class));
		return new GuiConfig(parentScreen, list, ModInfo.MODID, false, false,
				I18n.format("config.gui." + ModInfo.MODID + ".title"));
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return Collections.emptySet();
	}

}
