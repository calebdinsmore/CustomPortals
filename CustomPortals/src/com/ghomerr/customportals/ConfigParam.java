package com.ghomerr.customportals;

public enum ConfigParam
{
	NETHER_FRAME_BLOCK("netherframeblock"),
	ENDER_FRAME_BLOCK("enderframeblock"),
	MAX_PORTAL_BLOCKS("maxportalblocks"),
	USE_PERMISSIONS("usepermissions");
	
	public String value;
	
	ConfigParam(final String param)
	{
		value = param;
	}
	
	public String getValue()
	{
		return value;
	}
}
