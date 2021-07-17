package com.bxzmod.randomplugin.utils.shapemining;

import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.bxzmod.randomplugin.utils.SimulatePlayer;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerShapeMining implements IMiningControl
{
	private boolean miningDone = true;
	private final EntityPlayerMP player;
	private final UUID uuid;
	private final BlockPos startBlockPos;

	public PlayerShapeMining(EntityPlayerMP player, UUID uuid, BlockPos startBlockPos, EnumFacing facing)
	{
		this.player = player;
		this.uuid = uuid;
		this.startBlockPos = startBlockPos;
	}

	@Override
	public void updateMiningResult(boolean miningDone)
	{
		this.miningDone = miningDone;
	}

	@Override
	public void run()
	{
		SimulatePlayer harvest =
				new SimulatePlayer.Builder(this.player, (WorldServer) this.player.getServerWorld(), this.startBlockPos)
						.fromConfig().noDrop().playerTool().create();
		EnumFacing facing = ModPlayerData.getDataByPlayer(this.player).getFacing();
	}

	private List<BlockPos> generateShape()
	{
		return Collections.EMPTY_LIST;
	}
}
