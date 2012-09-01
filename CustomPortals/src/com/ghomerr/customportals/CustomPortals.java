package com.ghomerr.customportals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.logging.Logger;

import net.minecraft.server.WorldServer;

import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPortals extends JavaPlugin
{
	public int maxPortalBlocs = 255;
	public boolean usepermissions = true;

	public HashMap<String, WorldServer> lazyWorldServerMap = new HashMap<String, WorldServer>();
	public HashSet<String> portalCrafters = new HashSet<String>();
	public EventsListener eventsListener = null;

	public static final Logger LOGGER = Logger.getLogger("Minecraft");

	@Override
	public void onEnable()
	{
		super.onEnable();

		maxPortalBlocs = getConfig().getInt(ConfigParam.MAX_PORTAL_BLOCKS.getValue());
		usepermissions = getConfig().getBoolean(ConfigParam.USE_PERMISSIONS.getValue());

		LOGGER.info("[CustomPortals] Configuration loaded: " + ConfigParam.MAX_PORTAL_BLOCKS.getValue() + "=" + maxPortalBlocs + ", "
				+ ConfigParam.USE_PERMISSIONS.getValue() + "=" + usepermissions);

		eventsListener = new EventsListener(this);
		this.getCommand("customportals").setExecutor(new CommandsExecutor(this));

		getConfig().set(ConfigParam.MAX_PORTAL_BLOCKS.getValue(), maxPortalBlocs);
		getConfig().set(ConfigParam.USE_PERMISSIONS.getValue(), usepermissions);
		saveConfig();
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		portalCrafters.clear();
		lazyWorldServerMap.clear();
	}

	public BlockFace getYawDirection(final float inputYaw)
	{
		final float yaw = (inputYaw < 0) ? inputYaw + 360 : inputYaw;

		if (yaw >= 0f && yaw < 45f || yaw >= 315f)
		{
			return BlockFace.WEST;
		}
		else if (yaw >= 45f && yaw < 135f)
		{
			return BlockFace.NORTH;
		}
		else if (yaw >= 135f && yaw < 225f)
		{
			return BlockFace.EAST;
		}
		else if (yaw >= 225f && yaw < 315f)
		{
			return BlockFace.SOUTH;
		}
		else
		{
			throw new IllegalArgumentException("Bad parameter for getYawDirection: " + inputYaw);
		}
	}

	/**
	 * http://en.wikipedia.org/wiki/Flood_fill#Alternative_implementations
	 */
	private void floodFill(final String player, final Block node, final Material portalMaterial, final Material targetMaterial,
			final PortalType portalType, final BlockFace dir1, final BlockFace dir2, final BlockFace dir3, final BlockFace dir4)
	{
		try
		{
			final String worldName = node.getWorld().getName();
			if (!lazyWorldServerMap.containsKey(worldName))
			{
				lazyWorldServerMap.put(worldName, ((CraftWorld) node.getWorld()).getHandle());
			}

			final WorldServer worldServer = lazyWorldServerMap.get(worldName);
			worldServer.suppressPhysics = true;

			final Stack<Block> queue = new Stack<Block>();
			final Stack<Block> treatedBlocks = new Stack<Block>();

			queue.push(node);
			Block n = null;
			while (!queue.empty() && treatedBlocks.size() < maxPortalBlocs)
			{
				n = queue.pop();
				final Material type = n.getType();

				if (type == targetMaterial)
				{
					treatedBlocks.push(n);
					worldServer.setTypeId(n.getX(), n.getY(), n.getZ(), portalMaterial.getId());

					queue.push(n.getRelative(dir1));
					queue.push(n.getRelative(dir2));
					queue.push(n.getRelative(dir3));
					queue.push(n.getRelative(dir4));
				}
			}
			if (targetMaterial == Material.AIR)
			{
				LOGGER.info("[CustomPortals] " + player + " has made the " + portalType + " portal of " + treatedBlocks.size()
						+ " blocks from " + node.getWorld() + " " + node.getX() + "," + node.getY() + "," + node.getZ());
			}
			else
			{
				LOGGER.info("[CustomPortals] " + player + " has cleaned the " + portalType + " portal of " + treatedBlocks.size()
						+ " blocks from " + node.getWorld() + " " + node.getX() + "," + node.getY() + "," + node.getZ());
			}

			treatedBlocks.clear();
			queue.clear();
			worldServer.suppressPhysics = false;
		}
		catch (final Throwable th)
		{
			LOGGER.severe("[CustomPortals] Error while creating a portal: ");
			th.printStackTrace();
		}
	}

	public void fillNetherPortal(final String player, final Block node, final BlockFace right, final BlockFace left)
	{
		floodFill(player, node, Material.PORTAL, Material.AIR, PortalType.NETHER, BlockFace.UP, right, BlockFace.DOWN, left);
	}

	public void fillEnderPortal(final String player, final Block node)
	{
		floodFill(player, node, Material.ENDER_PORTAL, Material.AIR, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
				BlockFace.WEST);
	}

	public void cleanEnderPortal(final String player, final Block node)
	{
		floodFill(player, node, Material.AIR, Material.ENDER_PORTAL, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
				BlockFace.WEST);
	}
}
