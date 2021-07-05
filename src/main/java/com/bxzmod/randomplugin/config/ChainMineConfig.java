package com.bxzmod.randomplugin.config;

import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import net.minecraftforge.common.config.Configuration;

public class ChainMineConfig
{
	public static final String CATEGORY_CHAIN_MINE = ChainMiningManager.CHAIN_MINE;
	public static int MAX_VALUE = 10000;
	public static int MAX_PER_TICK = 64;
	public static int MAX_Radius = 64;
	public static boolean SHOULD_DAMAGE_ITEM = false;
	public static boolean CHECK_TOOL = false;
	public static boolean EXHAUST_PLAYER = false;
	public static boolean OFFSET_FACING = false;

	public static void load(Configuration config)
	{
		MAX_VALUE = config.get(CATEGORY_CHAIN_MINE, "max_value", 10000, "Max block to mine", 16, Integer.MAX_VALUE)
				.getInt(1000);
		MAX_PER_TICK = config.get(CATEGORY_CHAIN_MINE, "max_per_tick", 64, "Max per tick mine", 1, 640).getInt(64);
		SHOULD_DAMAGE_ITEM =
				config.get(CATEGORY_CHAIN_MINE, "damage_tool", false, "Should make item damage").getBoolean();
		CHECK_TOOL = config.get(CATEGORY_CHAIN_MINE, "check_tool", false, "Check Tool").getBoolean();
		EXHAUST_PLAYER =
				config.get(CATEGORY_CHAIN_MINE, "exhaust", false, "Should exhaust player on mining").getBoolean();
		OFFSET_FACING = config.get(CATEGORY_CHAIN_MINE, "offset_facing", false, "Should Mining follow player facing")
				.getBoolean();
	}
}
