package com.bxzmod.randomplugin.events;

import com.bxzmod.randomplugin.item.Ring;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.bxzmod.randomplugin.utils.shapemining.ShapeMiningManager;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;

public class ServerEventHandler
{
	public static final ServerEventHandler INSTANCE = new ServerEventHandler();

	private ServerEventHandler()
	{

	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ModPlayerData.initPlayerFlag(event.player.getGameProfile().getId());
	}

	@SubscribeEvent
	public void onPlayerLogoff(PlayerEvent.PlayerLoggedOutEvent event)
	{

	}

	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event)
	{
		if (event.isCanceled())
			return;
		if (event.entity instanceof EntityPlayer)
		{
			if (Ring.hasWorn((EntityPlayer) event.entity))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.isCanceled())
			return;
		if (event.entity instanceof EntityPlayer)
		{
			if (Ring.hasWorn((EntityPlayer) event.entity))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		ModPlayerData.addRingMark(event.player);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerBreakBlock(BlockEvent.BreakEvent event)
	{
		World world = event.world;
		if (!world.isRemote)
		{
			if (ChainMiningManager.onPlayerBreakBlock(event))
				return;
			if (ShapeMiningManager.onPlayerBreakBlock(event))
				return;
		}
	}
}
