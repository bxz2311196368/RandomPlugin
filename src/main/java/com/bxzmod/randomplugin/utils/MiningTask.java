package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayDeque;
import java.util.UUID;

public class MiningTask implements Runnable
{
	private final IMiningControl control;
	private final SimulatePlayer harvest;
	private final UUID uuid;
	private final ArrayDeque<BlockPos> blockList;

	public MiningTask(IMiningControl control, SimulatePlayer harvest, UUID uuid, ArrayDeque<BlockPos> blockList)
	{
		this.control = control;
		this.harvest = harvest;
		this.uuid = uuid;
		this.blockList = blockList;
	}

	@Override
	public void run()
	{
		int exp = this.harvest.startListMining(this.blockList);
		if (exp > 0)
			ChainMiningManager.addPlayerMiningXP(this.uuid, exp);
		this.control.updateMiningResult(true);
	}
}
