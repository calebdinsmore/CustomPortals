package com.ghomerr.customportals;

import java.util.logging.Logger;

import org.bukkit.Material;

public class BlockUtils
{
	public static final Logger LOGGER = Logger.getLogger("Minecraft");
	
	static public Material getBlockType(final String material)
	{
		Material mat = null;
		try
		{
			if (material != null && !material.isEmpty())
			{
				mat = Material.valueOf(material.toUpperCase());
			}
			else
			{
				LOGGER.severe("[CustomPortals] Invalid material: " +  material);
			}
		}
		catch (final Throwable th)
		{
			LOGGER.warning("[CustomPortals] Unknown material: " +  material);
		}
		return mat;
	}
}
