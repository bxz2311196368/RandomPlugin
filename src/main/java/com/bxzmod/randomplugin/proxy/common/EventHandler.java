package com.bxzmod.randomplugin.proxy.common;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.item.Ring;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.bxzmod.randomplugin.utils.shapemining.ShapeMiningManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class EventHandler
{
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ModPlayerData.initPlayerFlag(event.player);
	}

	@SubscribeEvent
	public static void onPlayerLogoff(PlayerEvent.PlayerLoggedOutEvent event)
	{

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerBreakBlock(BlockEvent.BreakEvent event)
	{
		World world = event.getWorld();
		if (!world.isRemote)
		{
			if (ChainMiningManager.onPlayerBreakBlock(event))
				return;
			if (ShapeMiningManager.onPlayerBreakBlock(event))
				return;
		}
	}

	@SubscribeEvent
	public static void onPlayerHurt(LivingHurtEvent event)
	{
		if (event.isCanceled())
			return;
		if (event.getEntity() instanceof EntityPlayer)
		{
			if (Ring.hasWorn((EntityPlayer) event.getEntity()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.isCanceled())
			return;
		if (event.getEntity() instanceof EntityPlayer)
		{
			if (Ring.hasWorn((EntityPlayer) event.getEntity()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		ModPlayerData.addRingMark(event.player);
	}

	//@SubscribeEvent
	public static void onPlayerPickup(EntityItemPickupEvent event)
	{

	}
}
