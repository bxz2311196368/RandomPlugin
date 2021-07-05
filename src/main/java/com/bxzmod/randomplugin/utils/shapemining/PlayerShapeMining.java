package com.bxzmod.randomplugin.utils.shapemining;

import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.bxzmod.randomplugin.utils.SimulatePlayer;
import com.bxzmod.randomplugin.utils.modinterface.IMiningControl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;

import javax.vecmath.Point3i;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerShapeMining implements IMiningControl
{
	private boolean miningDone = true;
	private final EntityPlayerMP player;
	private final UUID uuid;
	private final Point3i startBlockPos;

	public PlayerShapeMining(EntityPlayerMP player, UUID uuid, Point3i startBlockPos, EnumFacing facing)
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
				new SimulatePlayer.Builder(this.player, (WorldServer) this.player.worldObj, this.startBlockPos.x,
						this.startBlockPos.y, this.startBlockPos.z).fromConfig().noDrop().playerTool().create();
		EnumFacing facing = EnumFacing.getFront(ModPlayerData.getDataByPlayer(this.player).getFacing());
		EnumShape shape = ModPlayerData.getDataByUUID(this.uuid).getShape();
		List<Point3i> shapeList = this.generateShape(shape);
	}

	private List<Point3i> generateShape(EnumShape shape)
	{

		return Collections.EMPTY_LIST;
	}
}
