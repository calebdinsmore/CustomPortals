package com.ghomerr.customportals;

public enum ConfigParam
{
	USE_PERMISSIONS("usepermissions"),
	MAX_PORTAL_BLOCKS("maxportalblocks");
	
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
