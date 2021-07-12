package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.item.ItemLoader;
import com.mojang.util.UUIDTypeAdapter;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.vecmath.Point3i;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Helper
{
	public static boolean isItemStackSame(ItemStack stack1, ItemStack stack2)
	{
		return isItemStackConditionalityEqual(stack1, stack2, false, false);
	}

	public static boolean isItemStackConditionalityEqual(ItemStack stack1, ItemStack stack2, boolean ignoreMeta,
			boolean ignoreNBT)
	{
		if ((stack1 == null) != (stack2 == null))
			return false;
		if (stack1 == null)
			return true;
		if (!stack1.isItemEqual(stack2))
			return false;
		if (ignoreMeta)
			return true;
		if (stack1.getItemDamage() != stack2.getItemDamage())
			return false;
		if (ignoreNBT)
			return true;
		if (stack1.hasTagCompound() != stack2.hasTagCompound())
			return false;
		if (!stack1.hasTagCompound())
			return true;
		return Objects.equals(stack1.getTagCompound(), stack2.getTagCompound());

	}

	public static void dropXp(World worldIn, Point3i pos, int amount)
	{
		dropXp(worldIn, (double) pos.x + 0.5D, (double) pos.y + 0.5D, (double) pos.z + 0.5D, amount);
	}

	public static void dropXp(World worldIn, double x, double y, double z, int amount)
	{
		if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops"))
			worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, x, y, z, amount));
	}

	public static ItemStack fromNBT(NBTTagCompound compound)
	{
		int id = compound.getShort("id");
		Item item = Item.getItemById(id);
		ItemStack itemStack = new ItemStack(item);
		itemStack.readFromNBT(compound);
		return itemStack;
	}

	public static EntityPlayer findPlayerByUUID(UUID uuid)
	{
		List<EntityPlayer> playerList =
				FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
		for (EntityPlayer entityPlayer : playerList)
			if (entityPlayer.getGameProfile().getId().equals(uuid))
				return entityPlayer;
		return null;
	}

	public static EntityPlayer findPlayerByUUID(String uuid)
	{
		UUID id = UUIDTypeAdapter.fromString(uuid);
		return findPlayerByUUID(id);
	}

	public static Point3i offsetPos(Point3i pos, int x, int y, int z)
	{
		return new Point3i(pos.x + x, pos.y + y, pos.z + z);
	}

	public static boolean findMaterialOrb(EntityPlayer player)
	{
		for (ItemStack stack : player.inventory.mainInventory)
			if (stack != null && stack.getItem() == ItemLoader.materialOrb)
				return true;
		return false;
	}

	public static EnumFacing getPlayerRayTraceBlock(EntityPlayer player)
	{
		MovingObjectPosition position =
				getRayTraceResult(player.worldObj, 5.0, getEyePosition(player), player.getLookVec(), false, false,
						true);
		if (position != null)
			return EnumFacing.getFront(position.sideHit);
		return EnumFacing.NORTH;
	}

	private static EnumFacing fixRayTraceResult(int sideHit)
	{
		switch (sideHit % EnumFacing.values().length)
		{
			case 0:
				return EnumFacing.DOWN;
			case 1:
				return EnumFacing.UP;
			case 2:
				return EnumFacing.EAST;
			case 3:
				return EnumFacing.WEST;
			case 4:
				return EnumFacing.NORTH;
			case 5:
				return EnumFacing.SOUTH;
		}
		return EnumFacing.NORTH;
	}

	public static MovingObjectPosition getRayTraceResult(World world, double distance, Vec3 position, Vec3 look,
			boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
	{
		Vec3 range = Vec3.createVectorHelper(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
		return world.func_147447_a(position, range, stopOnLiquid, ignoreBlockWithoutBoundingBox,
				returnLastUncollidableBlock);
	}

	public static Vec3 getPosition(double x, double y, double z)
	{
		return Vec3.createVectorHelper(x, y, z);
	}

	public static Vec3 getPosition(EntityLivingBase entity)
	{
		return getPosition(entity.posX, entity.posY, entity.posZ);
	}

	public static Vec3 getEyePosition(EntityLivingBase entity)
	{
		return getPosition(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
	}

	public static boolean canPlayerHarvest(EntityPlayer player, World world, int x, int y, int z)
	{
		ItemStack tool = player.getHeldItem();
		Block block = world.getBlock(x, y, z);
		if (tool == null)
		{
			return block.getMaterial().isToolNotRequired();
		} else
			return ForgeHooks.canToolHarvestBlock(block, world.getBlockMetadata(x, y, z), tool);
	}

	public static EnumFacing getEntityHorizontalFacing(Entity entity)
	{
		int face = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch (face)
		{
			case 0:
				return EnumFacing.SOUTH;
			case 1:
				return EnumFacing.WEST;
			case 3:
				return EnumFacing.EAST;
			case 2:
			default:
				return EnumFacing.NORTH;
		}
	}
}
