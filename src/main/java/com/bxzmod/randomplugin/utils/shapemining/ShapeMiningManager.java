package com.bxzmod.randomplugin.utils.shapemining;

import com.bxzmod.randomplugin.utils.ModPlayerData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class ShapeMiningManager
{
	public static final String SHAPE_MINE = "shape_mine";

	public static boolean canShapeMiningContinue(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).isShapeMine();
	}

	public static boolean canPlayerShapeMining(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).canShapeMine();
	}

	public static void setPlayerKeyFlag(UUID uuid, boolean flag)
	{
		ModPlayerData.getDataByUUID(uuid).setShapeMine(flag);
	}

	public static boolean onPlayerBreakBlock(BlockEvent.BreakEvent event)
	{
		return false;
	}

	public static void onServerDataPacket(UUID uuid, NBTTagCompound compound)
	{

	}

	public static NBTTagCompound onClientPress()
	{
		NBTTagCompound compound = new NBTTagCompound();
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{

		}
		return compound;
	}
}
