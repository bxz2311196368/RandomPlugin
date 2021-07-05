package com.bxzmod.randomplugin.utils.chainmining;

import com.bxzmod.randomplugin.capability.MaterialOrbHolder;
import com.bxzmod.randomplugin.capability.capabilityinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.config.ChainMineConfig;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.proxy.client.ClientHelper;
import com.bxzmod.randomplugin.utils.Helper;
import com.bxzmod.randomplugin.utils.LargeItemStack;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Point3i;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChainMiningManager
{
	public static final String CHAIN_MINE = "chain_mine";
	public static final String CHAIN_MINE_FACE = "facing";

	/**
	 * 用于检查连锁采掘是否可以继续
	 *
	 * @param uuid 玩家UUID
	 * @return 返回连锁采掘是否可以继续
	 */
	public static boolean canChainMiningContinue(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).isChainMine();
	}

	/**
	 * 用于检查玩家是否可以开始一个连锁采掘
	 *
	 * @param uuid 玩家UUID
	 * @return 返回是否可以开始连锁采掘
	 */
	public static boolean canPlayerChainMining(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).canChainMine();
	}

	public static void setPlayerMiningFlag(UUID uuid, boolean flag)
	{
		ModPlayerData.getDataByUUID(uuid).setMineTask(flag);
	}

	public static ArrayList<LargeItemStack> getPlayerMiningDropHolder(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).getMineItemHolder();
	}

	public static void stopPlayerMining(EntityPlayer player)
	{
		UUID uuid = player.getGameProfile().getId();
		setPlayerMiningFlag(uuid, false);
		int xp = popPlayerMiningXP(uuid);
		if (xp > 0)
			Helper.dropXp(player.worldObj, player.posX, player.posY, player.posZ, xp);
		ArrayList<LargeItemStack> dropList = ChainMiningManager.getPlayerMiningDropHolder(uuid);
		if (!dropList.isEmpty())
		{

			List<LargeItemStack> remove = Lists.newArrayList();
			for (LargeItemStack largeItemStack : dropList)
				if (largeItemStack.getAmount() == 0)
					remove.add(largeItemStack);
			dropList.removeAll(remove);
			if (dropList.isEmpty())
				return;

			ItemStack[] inventory = player.inventory.mainInventory;
			for (int i = 0; i < inventory.length; i++)
			{
				if (inventory[i] != null && inventory[i].getItem() == ItemLoader.materialOrb)
				{
					IMaterialOrbHolder holder = MaterialOrbHolder.fromNBT(inventory[i]);
					holder.addItems(dropList);
					dropList.clear();
					inventory[i].setTagCompound(holder.serializeNBT());
					ItemStack newStack = inventory[i].copy();
					inventory[i] = newStack;
					return;
				}
			}

			ItemStack itemStack = new ItemStack(ItemLoader.materialOrb);
			IMaterialOrbHolder holder = MaterialOrbHolder.fromNBT(itemStack);
			holder.addItems(dropList);
			itemStack.setTagCompound(holder.serializeNBT());
			EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, itemStack);
			entityItem.delayBeforeCanPickup = 0;
			//entityItem.setEntityInvulnerable(true);
			player.worldObj.spawnEntityInWorld(entityItem);

		}
		dropList.clear();
	}

	public static void setPlayerKeyFlag(UUID uuid, boolean flag)
	{
		ModPlayerData.getDataByUUID(uuid).setChainMine(flag);
	}

	public static void addPlayerMiningXP(UUID uuid, int xp)
	{
		ModPlayerData.getDataByUUID(uuid).addXP(xp);
	}

	public static int popPlayerMiningXP(UUID uuid)
	{
		return ModPlayerData.getDataByUUID(uuid).popXP();
	}

	public static void startPlayerChainMining(EntityPlayer player, Point3i blockPos)
	{
		setPlayerMiningFlag(player.getGameProfile().getId(), true);
		new Thread(new PlayerChainMining((EntityPlayerMP) player, blockPos), player.getDisplayName() + "-chainMining")
				.start();
	}

	public static boolean onPlayerBreakBlock(BlockEvent.BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = event.world;
		UUID uuid = player.getGameProfile().getId();
		if (canPlayerChainMining(uuid))
		{
			if (ChainMineConfig.CHECK_TOOL)
			{
				if (!Helper.canPlayerHarvest(player, world, event.x, event.y, event.z))
					return false;
			}
			startPlayerChainMining(player, new Point3i(event.x, event.y, event.z));
			event.setCanceled(true);
			return true;
		}
		return false;
	}

	public static void onServerDataPacket(UUID uuid, NBTTagCompound compound)
	{
		ModPlayerData.getDataByUUID(uuid).setChainMineFaceOffset(compound.getBoolean(CHAIN_MINE_FACE));
	}

	public static NBTTagCompound onClientPress()
	{
		NBTTagCompound compound = new NBTTagCompound();
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			ModPlayerData.ModPlayerClientData.cycleChainMineFaceOffset();
			ClientHelper.addChatMessage(
					new ChatComponentTranslation(ClientHelper.fixMessageI18n(ChainMiningManager.CHAIN_MINE),
							ModPlayerData.ModPlayerClientData.chainMainFaceOffset), 0xabcd);
			compound.setBoolean(CHAIN_MINE_FACE, ModPlayerData.ModPlayerClientData.chainMainFaceOffset);
		}
		return compound;
	}

}
