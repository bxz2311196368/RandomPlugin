package com.bxzmod.randomplugin.utils;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.bxzmod.randomplugin.item.ItemLoader;
import com.bxzmod.randomplugin.utils.shapemining.EnumShape;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ModPlayerData
{
	/**
	 * 存储玩家数据
	 */
	private static final HashMap<UUID, ModPlayerServerData> PLAYER_DATA = Maps.newHashMap();
	private static final ModPlayerServerData EMPTY_DATA = new ModPlayerServerData();

	public static void initPlayerFlag(UUID uuid)
	{
		PLAYER_DATA.put(uuid, new ModPlayerServerData());
		if (FMLCommonHandler.instance().getSide().isServer())
		{
			addRingMark(Helper.findPlayerByUUID(uuid));
		}

	}

	public static ModPlayerServerData getDataByPlayer(EntityPlayer player)
	{
		return getDataByUUID(player.getGameProfile().getId());
	}

	public static ModPlayerServerData getDataByUUID(UUID uuid)
	{
		if (!PLAYER_DATA.containsKey(uuid))
			return EMPTY_DATA;
		return PLAYER_DATA.get(uuid);
	}

	public static void addRingMark(EntityPlayer player)
	{
		if (player == null)
			return;
		InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
		boolean mark = false;
		for (int i = 0; i < baubles.getSizeInventory(); i++)
		{
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack != null && stack.getItem() == ItemLoader.ring)
				mark = true;
		}
		PLAYER_DATA.get(player.getGameProfile().getId()).setRingWear(mark);
	}

	/**
	 * 仅 {@link cpw.mods.fml.relauncher.Side#SERVER}
	 */
	public static class ModPlayerServerData
	{
		/**
		 * 检测玩家是否按住连锁快捷键
		 */
		private boolean chainMine = false;
		/**
		 * 检测是否按照面朝方向挖掘
		 */
		private boolean chainMineFaceOffset = false;
		/**
		 * 检测玩家是否按住形状采掘快捷键
		 */
		private boolean shapeMine = false;
		/**
		 * 检测玩家是否正处于一个多方块采掘作业中
		 */
		private boolean mineTask = false;
		/**
		 * 存储玩家多方块采掘作业获得的经验
		 */
		private int storeXp = 0;
		/**
		 * 存储玩家多方块采掘作业的掉落物
		 */
		private final ArrayList<LargeItemStack> mineItemHolder = Lists.newArrayList();
		/**
		 * 标记玩家是否装备了指环
		 */
		private boolean ringWear = false;

		private int facing = 0;

		private EnumShape shape = EnumShape.RECTANGLE;

		public boolean isChainMine()
		{
			return chainMine;
		}

		public void setChainMine(boolean chainMine)
		{
			this.chainMine = chainMine;
		}

		public boolean isShapeMine()
		{
			return shapeMine;
		}

		public void setShapeMine(boolean shapeMine)
		{
			this.shapeMine = shapeMine;
		}

		public boolean isMineTask()
		{
			return mineTask;
		}

		public void setMineTask(boolean mineTask)
		{
			this.mineTask = mineTask;
		}

		public int popXP()
		{
			int temp = this.storeXp;
			this.storeXp = 0;
			return temp;
		}

		public void addXP(int xp)
		{
			this.storeXp += xp;
		}

		public ArrayList<LargeItemStack> getMineItemHolder()
		{
			return mineItemHolder;
		}

		public boolean isRingWear()
		{
			return ringWear;
		}

		public void setRingWear(boolean ringWear)
		{
			this.ringWear = ringWear;
		}

		public boolean canChainMine()
		{
			return this.chainMine && !this.mineTask;
		}

		public boolean canShapeMine()
		{
			return this.shapeMine && !this.mineTask;
		}

		public int getFacing()
		{
			return facing;
		}

		public void setFacing(int facing)
		{
			this.facing = facing;
		}

		public boolean isChainMineFaceOffset()
		{
			return chainMineFaceOffset;
		}

		public void setChainMineFaceOffset(boolean chainMineFaceOffset)
		{
			this.chainMineFaceOffset = chainMineFaceOffset;
		}

		public EnumShape getShape()
		{
			return shape;
		}

		public void setShape(EnumShape shape)
		{
			this.shape = shape;
		}
	}

	/**
	 * 仅 {@link cpw.mods.fml.relauncher.Side#CLIENT}
	 */
	public static class ModPlayerClientData
	{

		/**
		 * 存储指环穿戴状态检测
		 */
		public static boolean ringMark;

		public static boolean chainMainFaceOffset;

		public static EnumShape shape;

		static
		{
			reset();
		}

		public static void reset()
		{
			ringMark = false;
			chainMainFaceOffset = false;
			shape = EnumShape.RECTANGLE;
		}

		public static void cycleChainMineFaceOffset()
		{
			chainMainFaceOffset = !chainMainFaceOffset;
		}

		public static void cycleShape()
		{
			shape = EnumShape.fromOrder((shape.getOrder() + 1) % EnumShape.values().length);
		}
	}

}
