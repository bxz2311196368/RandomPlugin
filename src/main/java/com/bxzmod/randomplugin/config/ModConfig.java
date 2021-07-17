package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModConfig
{
	public static final String CATEGORY_I18N_PREFIX = "config.category.", PROPERTY_I18N_PREFIX = ".property.",
			LANGUAGE_KEY_APPENDIX = ".name", COMMENT_APPENDIX = ".comment";
	public static final String CATEGORY_CHAIN_MINE = ChainMiningManager.CHAIN_MINE;

	public static NBTTagCompound serializeNBT()
	{
		Configuration config = getModConfig();
		NBTTagCompound compound = new NBTTagCompound();
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
			compound.setTag(category.getName(), fromCategory(category));
		return compound;
	}

	public static void deserializeNBT(NBTTagCompound compound)
	{
		Configuration config = getModConfig();
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			String name = category.getName();
			if (compound.hasKey(name))
			{
				toCategory(category, compound.getCompoundTag(name));
			}
		}
		ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
		initConfigLanguageKey();
	}

	private static NBTTagCompound fromCategory(ConfigCategory category)
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound categories = new NBTTagCompound();
		NBTTagCompound properties = new NBTTagCompound();
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				categories.setTag(category_children.getName(), fromCategory(category));
		}

		compound.setTag("categories", categories);
		for (Property property : category.getOrderedValues())
		{
			properties.setTag(property.getName(), fromProperty(property));
		}
		compound.setTag("properties", properties);
		return compound;
	}

	private static NBTTagCompound fromProperty(Property property)
	{
		NBTTagCompound compound = new NBTTagCompound();
		if (property.isList())
		{
			NBTTagList list = new NBTTagList();
			for (String value : property.getStringList())
				list.appendTag(new NBTTagString(value));
			compound.setTag(property.getType().name(), list);
		} else
		{
			compound.setString(property.getType().name(), property.getString());
		}
		return compound;
	}

	private static void toCategory(ConfigCategory category, NBTTagCompound compound)
	{
		if (!category.getChildren().isEmpty())
		{
			NBTTagCompound categories = compound.getCompoundTag("categories");
			for (ConfigCategory category_children : category.getChildren())
			{
				String name = category_children.getName();
				if (categories.hasKey(name))
				{
					toCategory(category_children, categories.getCompoundTag(name));
				}
			}
		}

		NBTTagCompound properties = compound.getCompoundTag("properties");
		for (Property property : category.getOrderedValues())
		{
			String name = property.getName();
			if (properties.hasKey(name))
			{
				toProperty(property, properties.getCompoundTag(name));
			}
		}
	}

	private static void toProperty(Property property, NBTTagCompound compound)
	{

		if (property.isList())
		{
			NBTTagList list = (NBTTagList) compound.getTag(property.getType().name());
			String[] values = new String[list.tagCount()];
			for (int i = 0; i < list.tagCount(); i++)
			{
				values[i] = list.getStringTagAt(i);
			}
			property.setValues(values);
		} else
		{
			property.setValue(compound.getString(property.getType().name()));
		}

	}

	public static void initConfigLanguageKey()
	{
		Configuration config = getModConfig();
		if (config == null)
			return;
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			initConfigCategoryLanguageKey(category);
		}
		initConfigComment(config);
		config.save();
	}

	private static void initConfigCategoryLanguageKey(ConfigCategory category)
	{
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				initConfigCategoryLanguageKey(category_children);
		}
		category.setLanguageKey(CATEGORY_I18N_PREFIX + category.getName() + LANGUAGE_KEY_APPENDIX);
		for (Property property : category.getOrderedValues())
			initConfigPropertyLanguageKey(category, property);
	}

	private static void initConfigPropertyLanguageKey(ConfigCategory category, Property property)
	{
		property.setLanguageKey(CATEGORY_I18N_PREFIX + category.getName() + PROPERTY_I18N_PREFIX + property.getName()
				+ LANGUAGE_KEY_APPENDIX);
	}

	public static void initConfigComment(Configuration config)
	{
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			initConfigCategoryComment(category);
		}
	}

	private static void initConfigCategoryComment(ConfigCategory category)
	{
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				initConfigCategoryComment(category_children);
		}
		category.setComment(I18n.format(CATEGORY_I18N_PREFIX + category.getName() + COMMENT_APPENDIX));
		for (Property property : category.getOrderedValues())
			initConfigPropertyComment(category, property);
	}

	private static void initConfigPropertyComment(ConfigCategory category, Property property)
	{
		property.setComment(I18n.format(
				CATEGORY_I18N_PREFIX + category.getName() + PROPERTY_I18N_PREFIX + property.getName()
						+ COMMENT_APPENDIX));
	}

	public static Configuration getModConfig()
	{
		Configuration config = null;
		try
		{
			Method m = ConfigManager.class.getDeclaredMethod("getConfiguration", String.class, String.class);
			m.setAccessible(true);
			config = (Configuration) m.invoke(null, ModInfo.MODID, ModInfo.MODID);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return config;
	}

	@SubscribeEvent
	public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ModInfo.MODID))
		{
			ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
			initConfigLanguageKey();
		}
	}

	@Config(modid = ModInfo.MODID, category = CATEGORY_CHAIN_MINE)
	public static class ChainMineConfig
	{
		@Config.Name("max_value")
		@Config.RangeInt(min = 16, max = 1000000)
		public static int MAX_VALUE = 10000;
		@Config.Name("max_per_tick")
		@Config.RangeInt(min = 1, max = 640)
		public static int MAX_PER_TICK = 64;
		//@Config.Name("")
		//@Config.RangeInt(min = 1, max = 128)
		//public static int MAX_Radius = 64;
		@Config.Name("damage_tool")
		public static boolean SHOULD_DAMAGE_ITEM = false;
		@Config.Name("check_tool")
		public static boolean CHECK_TOOL = false;
		@Config.Name("exhaust")
		public static boolean EXHAUST_PLAYER = false;
	}

}
