package com.bxzmod.randomplugin.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleportManager
{

	public static void teleportEntityPlayer(EntityPlayerMP player, double x, double y, double z, int targetDim)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		WorldServer toDim = server.worldServerForDimension(targetDim);
		if (toDim == null)
			return;
		toDim.getChunkProvider().provideChunk((int) x >> 4, (int) z >> 4);
		boolean flying = player.capabilities.isFlying;
		int from = player.dimension;
		if (player.dimension != targetDim)
		{
			Teleporter teleporter = new ModTeleport(toDim);
			server.getConfigurationManager().transferPlayerToDimension(player, targetDim, teleporter);
			if (from == 1 && player.isEntityAlive())
			{
				toDim.spawnEntityInWorld(player);
				toDim.updateEntityWithOptionalForce(player, false);
			}
		}

		player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
		player.addExperienceLevel(0);
		player.fallDistance = 0F;
		player.capabilities.isFlying = flying;
	}

	public static class ModTeleport extends Teleporter
	{
		public ModTeleport(WorldServer worldServer)
		{
			super(worldServer);
		}

		@Override
		public void placeInPortal(Entity entityIn, double x, double y, double z, float rotationYaw)
		{
			entityIn.motionX = 0D;
			entityIn.motionY = 0D;
			entityIn.motionZ = 0D;
			entityIn.fallDistance = 0F;
		}

		@Override
		public boolean placeInExistingPortal(Entity entityIn, double x, double y, double z, float rotationYaw)
		{
			return true;
		}

		@Override
		public boolean makePortal(Entity entityIn)
		{
			return true;
		}

		@Override
		public void removeStalePortalLocations(long worldTime)
		{

		}
	}
}
