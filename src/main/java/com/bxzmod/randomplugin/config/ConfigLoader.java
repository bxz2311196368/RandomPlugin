package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.capability.capabilityinterface.INBTSerializable;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.List;

public class ConfigLoader implements INBTSerializable<NBTTagCompound>
{
	public static Configuration config;

	private static final String CATEGORY_I18N_PREFIX = "config.category.", PROPERTY_I18N_PREFIX = ".property.",
			LANGUAGE_KEY_APPENDIX = ".name", COMMENT_APPENDIX = ".comment";
	public static ConfigLoader INSTANCE;

	public ConfigLoader(FMLPreInitializationEvent event)
	{
		INSTANCE = this;
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		this.load();
	}

	private void load()
	{
		ChainMineConfig.load(config);
		config.save();
		this.initConfigLanguageKey();
	}

	private void initConfigLanguageKey()
	{
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			this.initConfigCategoryLanguageKey(category);
		}
	}

	private void initConfigCategoryLanguageKey(ConfigCategory category)
	{
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				this.initConfigCategoryLanguageKey(category_children);
		}
		category.setLanguageKey(CATEGORY_I18N_PREFIX + category.getName() + LANGUAGE_KEY_APPENDIX);
		for (Property property : category.getOrderedValues())
			this.initConfigPropertyLanguageKey(category, property);
	}

	private void initConfigPropertyLanguageKey(ConfigCategory category, Property property)
	{
		property.setLanguageKey(CATEGORY_I18N_PREFIX + category.getName() + PROPERTY_I18N_PREFIX + property.getName()
				+ LANGUAGE_KEY_APPENDIX);
	}

	public void initConfigComment()
	{
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			this.initConfigCategoryComment(category);
		}
	}

	private void initConfigCategoryComment(ConfigCategory category)
	{
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				this.initConfigCategoryComment(category_children);
		}
		category.setComment(I18n.format(CATEGORY_I18N_PREFIX + category.getName() + COMMENT_APPENDIX));
		for (Property property : category.getOrderedValues())
			this.initConfigPropertyComment(category, property);
	}

	private void initConfigPropertyComment(ConfigCategory category, Property property)
	{
		property.comment = I18n.format(
				CATEGORY_I18N_PREFIX + category.getName() + PROPERTY_I18N_PREFIX + property.getName()
						+ COMMENT_APPENDIX);
	}

	public void reload()
	{
		if (config.hasChanged())
			this.load();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
			compound.setTag(category.getName(), this.fromCategory(category));
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound)
	{
		List<ConfigCategory> categories = Lists.newArrayList();
		for (String name : config.getCategoryNames())
			categories.add(config.getCategory(name));
		for (ConfigCategory category : categories)
		{
			String name = category.getName();
			if (compound.hasKey(name))
			{
				this.toCategory(category, compound.getCompoundTag(name));
			}
		}
		this.load();
	}

	private NBTTagCompound fromCategory(ConfigCategory category)
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound categories = new NBTTagCompound();
		NBTTagCompound properties = new NBTTagCompound();
		if (!category.getChildren().isEmpty())
		{
			for (ConfigCategory category_children : category.getChildren())
				categories.setTag(category_children.getName(), this.fromCategory(category));
		}

		compound.setTag("categories", categories);
		for (Property property : category.getOrderedValues())
		{
			properties.setTag(property.getName(), this.fromProperty(property));
		}
		compound.setTag("properties", properties);
		return compound;
	}

	private NBTTagCompound fromProperty(Property property)
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

	private void toCategory(ConfigCategory category, NBTTagCompound compound)
	{
		if (!category.getChildren().isEmpty())
		{
			NBTTagCompound categories = compound.getCompoundTag("categories");
			for (ConfigCategory category_children : category.getChildren())
			{
				String name = category_children.getName();
				if (categories.hasKey(name))
				{
					this.toCategory(category_children, categories.getCompoundTag(name));
				}
			}
		}

		NBTTagCompound properties = compound.getCompoundTag("properties");
		for (Property property : category.getOrderedValues())
		{
			String name = property.getName();
			if (properties.hasKey(name))
			{
				this.toProperty(property, properties.getCompoundTag(name));
			}
		}
	}

	private void toProperty(Property property, NBTTagCompound compound)
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
}
