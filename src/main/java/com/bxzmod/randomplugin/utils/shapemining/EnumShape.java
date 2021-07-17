package com.bxzmod.randomplugin.utils.shapemining;

import com.bxzmod.randomplugin.proxy.client.ClientHelper;
import net.minecraft.client.resources.I18n;

public enum EnumShape
{
	RECTANGLE("rectangle", 0),
	UPSTAIRS("upstairs", 1),
	DOWNSTAIRS("downstairs", 2);
	private final String name;
	private final int order;
	public static final String SHAPE = ".shape.";

	EnumShape(String name, int order)
	{
		this.name = name;
		this.order = order;
	}

	public String getName()
	{
		return name;
	}

	public String getLocalName()
	{
		return I18n.format(ClientHelper.MESSAGE + ShapeMiningManager.SHAPE_MINE + SHAPE + this.name);
	}

	public int getOrder()
	{
		return order;
	}

	public static EnumShape fromOrder(int order)
	{
		for (EnumShape shape : values())
			if (shape.order == order)
				return shape;
		return RECTANGLE;
	}
}
