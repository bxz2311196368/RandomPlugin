package com.bxzmod.randomplugin.utils.sidecontrol;

public enum EnumSideMode
{
	IN(true, false),
	OUT(false, true),
	ALL(true, true),
	NONE(false, false);

	private boolean receive, extract;

	EnumSideMode(boolean receive, boolean extract)
	{
		this.receive = receive;
		this.extract = extract;
	}

	public boolean canReceive()
	{
		return receive;
	}

	public boolean canExtract()
	{
		return extract;
	}

	public EnumSideMode next()
	{
		return EnumSideMode.values()[(this.ordinal() + 1) % 4];
	}
}
