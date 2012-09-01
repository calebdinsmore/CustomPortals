package com.ghomerr.customportals;

import java.util.HashMap;

import org.bukkit.block.BlockFace;

public enum NextDirection
{
	NORTH (BlockFace.NORTH, BlockFace.EAST), 
	EAST (BlockFace.EAST, BlockFace.SOUTH),
	SOUTH (BlockFace.SOUTH, BlockFace.WEST), 
	WEST (BlockFace.WEST, BlockFace.NORTH);
	
	public BlockFace dir;
	public BlockFace next;
	public static HashMap<BlockFace, BlockFace> mapping = new HashMap<BlockFace, BlockFace>();

	NextDirection (final BlockFace dir, final BlockFace next)
	{
		this.dir = dir;
		this.next = next;
	}

	static
	{
		for (final NextDirection dir : NextDirection.values())
		{
			mapping.put(dir.dir, dir.next);
		}
	}

	public static BlockFace get(final BlockFace dir)
	{
		return mapping.get(dir);
	}
}
