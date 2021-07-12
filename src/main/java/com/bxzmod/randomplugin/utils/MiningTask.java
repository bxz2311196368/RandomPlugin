package com.bxzmod.randomplugin.utils;

import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;

import javax.vecmath.Point3i;
import java.util.ArrayDeque;
import java.util.UUID;

public class MiningTask implements Runnable
{
	private final IMiningControl control;
	private final SimulatePlayer harvest;
	private final UUID uuid;
	private final ArrayDeque<Point3i> blockList;

	public MiningTask(IMiningControl control, SimulatePlayer harvest, UUID uuid, ArrayDeque<Point3i> blockList)
	{
		this.control = control;
		this.harvest = harvest;
		this.uuid = uuid;
		this.blockList = blockList;
	}

	@Override
	public void run()
	{
		try
		{
			int exp = this.harvest.startListMining(this.blockList);
			if (exp > 0)
				ChainMiningManager.addPlayerMiningXP(this.uuid, exp);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.control.updateMiningResult(true);
	}
}
