package com.ghomerr.customportals;

public enum PlayerCommand
{
	ON("customportals.on"),
	OFF("customportals.off"),
	ENDERFRAMEBLOCK("customportals.conf"),
	NETHERFRAMEBLOCK("customportals.conf"),
	MAXPORTALBLOCKS("customportals.conf"),
	USEPERMISSIONS("customportals.conf"),
	UNKNOWN;
	
	private String permNode;
	
	PlayerCommand()
	{
		permNode = "";
	}
	
	PlayerCommand(final String permNode)
	{
		this.permNode = permNode;
	}
	
	final String getPerm()
	{
		return permNode;
	}
	
	static public PlayerCommand getPlayerCommand(final String value)
	{
		try
		{
			return PlayerCommand.valueOf(value.toUpperCase());
		}
		catch (final Throwable th)
		{
			return UNKNOWN;
		}
	}
}
