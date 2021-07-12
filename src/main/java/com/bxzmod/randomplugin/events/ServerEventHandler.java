package com.bxzmod.randomplugin.events;

import com.bxzmod.randomplugin.capability.MaterialOrbHolder;
import com.bxzmod.randomplugin.capability.capabilityinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.item.Ring;
import com.bxzmod.randomplugin.utils.LargeItemStack;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.bxzmod.randomplugin.utils.shapemining.ShapeMiningManager;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Iterator;
import java.util.List;

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

	@SubscribeEvent
	public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event)
	{
		ItemStack stack = event.crafting;
		IInventory inventory = event.craftMatrix;
		EntityPlayer player = event.player;
		;
		if (stack.getItem() == ItemLoader.materialOrb)
		{
			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack old = inventory.getStackInSlot(i);
				if (old != null && old.getItem() == ItemLoader.materialOrb)
				{
					IMaterialOrbHolder oldHolder = MaterialOrbHolder.fromNBT(old);
					List<LargeItemStack> stacks = oldHolder.getStacks();
					if (!stacks.isEmpty())
					{
						Iterator<LargeItemStack> iterator = stacks.iterator();
						while (true)
						{
							LargeItemStack largeItemStack = iterator.next();
							IMaterialOrbHolder holder = new MaterialOrbHolder();
							holder.addItems(Lists.newArrayList(largeItemStack));
							if (iterator.hasNext())
							{
								ItemStack drop = new ItemStack(ItemLoader.materialOrb);
								drop.setTagCompound(holder.serializeNBT());
								if (!player.inventory.addItemStackToInventory(drop))
								{
									if (player.worldObj.isRemote)
										continue;
									EntityItem entityItem =
											new EntityItem(player.worldObj, player.posX, player.posY, player.posZ,
													drop);
									entityItem.delayBeforeCanPickup = 0;
									player.worldObj.spawnEntityInWorld(entityItem);
								}
							} else
							{
								stack.setTagCompound(holder.serializeNBT());
								break;
							}
						}

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemUse(PlayerInteractEvent event)
	{
		if (event.world.isRemote)
			return;
		ItemStack hand = event.entityPlayer.getHeldItem();
		if (hand != null && hand.getItem() == ItemLoader.materialOrb)
		{
			TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
			if (te instanceof IInventory)
			{
				IMaterialOrbHolder holder = MaterialOrbHolder.fromNBT(hand);
				holder.insertToInventory(hand, (IInventory) te);
				event.setCanceled(true);
			}
		}
	}
}
