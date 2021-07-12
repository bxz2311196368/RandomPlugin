package com.bxzmod.randomplugin.utils.shapemining;

import com.bxzmod.randomplugin.utils.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import javax.vecmath.Point3i;
import java.util.Collections;
import java.util.List;

public class ShapeGenerator
{
	public static List<Point3i> generateRectangle(Point3i startPos, EnumFacing blockFacing, EntityPlayer player,
			int width, int height)
	{
		int startX, startY, startZ, endX, endY, endZ;
		EnumFacing playerFacing = Helper.getEntityHorizontalFacing(player);
		if (blockFacing == EnumFacing.DOWN || blockFacing == EnumFacing.UP)
		{
			startY = 0;
			endY = 1;
		} else
		{
			startY = -1;
			endY = height - 1;
		}
		if (blockFacing == EnumFacing.NORTH)
		{

		}

		return Collections.emptyList();
	}

}
