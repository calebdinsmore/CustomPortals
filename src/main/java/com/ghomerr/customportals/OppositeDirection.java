package com.ghomerr.customportals;

import java.util.HashMap;

import org.bukkit.block.BlockFace;

public enum OppositeDirection
{
	UP (BlockFace.UP, BlockFace.DOWN), 
	DOWN (BlockFace.DOWN, BlockFace.UP), 
	NORTH (BlockFace.NORTH, BlockFace.SOUTH), 
	SOUTH (BlockFace.SOUTH, BlockFace.NORTH), 
	WEST (BlockFace.WEST, BlockFace.EAST), 
	EAST (BlockFace.EAST, BlockFace.WEST);

	public BlockFace dir;
	public BlockFace opposite;
	public static HashMap<BlockFace, BlockFace> mapping = new HashMap<BlockFace, BlockFace>();

	OppositeDirection (final BlockFace dir, final BlockFace opp)
	{
		this.dir = dir;
		this.opposite = opp;
	}

	static
	{
		for (final OppositeDirection dir : OppositeDirection.values())
		{
			mapping.put(dir.dir, dir.opposite);
		}
	}

	public static BlockFace get(final BlockFace dir)
	{
		return mapping.get(dir);
	}
}
