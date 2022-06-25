package com.ghomerr.customportals;

import java.util.HashSet;
import java.util.Stack;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPortals extends JavaPlugin
{
	public int maxPortalBlocs = 255;
	public boolean usepermissions = true;
	public Material netherFrameBlock = null;
	public Material enderFrameBlock = null; 

	//public HashMap<String, WorldServer> lazyWorldServerMap = new HashMap<String, WorldServer>();
	public HashSet<String> portalCrafters = new HashSet<String>();
	public EventsListener eventsListener = null;

	public static final Logger LOGGER = Logger.getLogger("Minecraft");

	@Override
	public void onEnable()
	{
		super.onEnable();
		this.saveDefaultConfig();
		
		maxPortalBlocs = getConfig().getInt(ConfigParam.MAX_PORTAL_BLOCKS.getValue());
		usepermissions = getConfig().getBoolean(ConfigParam.USE_PERMISSIONS.getValue());
		
		final String netherFrameBlockName = getConfig().getString(ConfigParam.NETHER_FRAME_BLOCK.getValue());
		Material matTmp = BlockUtils.getBlockType(netherFrameBlockName);
		if (matTmp != null)
		{
			netherFrameBlock = matTmp;
		}
		
		final String enderFrameBlockName = getConfig().getString(ConfigParam.ENDER_FRAME_BLOCK.getValue());
		matTmp = BlockUtils.getBlockType(enderFrameBlockName);
		if (matTmp != null)
		{
			enderFrameBlock = matTmp;
		}

		LOGGER.info("[CustomPortals] Configuration loaded: " + ConfigParam.NETHER_FRAME_BLOCK.getValue() + "=" + netherFrameBlock
				+ ", " + ConfigParam.ENDER_FRAME_BLOCK.getValue() + "=" + enderFrameBlock
				+ ", " + ConfigParam.MAX_PORTAL_BLOCKS.getValue() + "=" + maxPortalBlocs
				+ ", " + ConfigParam.USE_PERMISSIONS.getValue() + "=" + usepermissions);

		eventsListener = new EventsListener(this);
		this.getCommand("customportals").setExecutor(new CommandsExecutor(this));
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		portalCrafters.clear();
		//lazyWorldServerMap.clear();
	}

	public BlockFace getYawDirection(final float inputYaw)
	{
		LOGGER.info("[CP-Debug] InputYaw = " + inputYaw);
		final float yaw = (inputYaw < 0) ? inputYaw + 360 : inputYaw;

		if (yaw >= 0f && yaw < 45f || yaw >= 315f)
		{
			return BlockFace.SOUTH;
		}
		else if (yaw >= 45f && yaw < 135f)
		{
			return BlockFace.WEST;
		}
		else if (yaw >= 135f && yaw < 225f)
		{
			return BlockFace.NORTH;
		}
		else if (yaw >= 225f && yaw < 315f)
		{
			return BlockFace.EAST;
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
			final PortalType portalType, final BlockFace dir1, final BlockFace dir2, final BlockFace dir3, final BlockFace dir4, final boolean isCleaning)
		throws Throwable
	{
		//final String worldName = node.getWorld().getName();
//		if (!lazyWorldServerMap.containsKey(worldName))
//		{
//			lazyWorldServerMap.put(worldName, ((CraftWorld) node.getWorld()).getHandle());
//		}

		//final WorldServer worldServer = lazyWorldServerMap.get(worldName);
		//worldServer.suppressPhysics = true;

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
				
				final BlockState state = n.getState();
				state.setType(portalMaterial);
				state.update(true, false); // force = true, applyPhysics = false
				//worldServer.setTypeId(n.getX(), n.getY(), n.getZ(), portalMaterial.getId());

				queue.push(n.getRelative(dir1));
				queue.push(n.getRelative(dir2));
				queue.push(n.getRelative(dir3));
				queue.push(n.getRelative(dir4));
			}
			else if (!isCleaning)
			{
				switch (portalType)
				{
					case NETHER:
						if (netherFrameBlock != null && type != portalMaterial && type != netherFrameBlock)
						{
							throw new IllegalFrameBlock("Unauthorized block " + type + " used to build " + portalType + " portal. Expected: " + netherFrameBlock);
						}
						break;
						
					case ENDER:
						if (enderFrameBlock != null && type != portalMaterial && type != enderFrameBlock)
						{
							throw new IllegalFrameBlock("Unauthorized block " + type + " used to build " + portalType + " portal. Expexted: " + enderFrameBlock);
						}
						break;
						
					default:
						LOGGER.warning("[CustomPortals] Unhandled portal type on FILLING: " + portalType);
				}
			}
		}
		if (!isCleaning)
		{
			LOGGER.info("[CustomPortals] " + player + " has made the " + portalType + " portal of " + treatedBlocks.size()
					+ " blocks from " + node.getWorld().getName() + " at " + node.getX() + "," + node.getY() + "," + node.getZ());
		}
		else
		{
			LOGGER.info("[CustomPortals] " + player + " has cleaned the " + portalType + " portal of " + treatedBlocks.size()
					+ " blocks from " + node.getWorld().getName() + " at " + node.getX() + "," + node.getY() + "," + node.getZ());
		}

		treatedBlocks.clear();
		queue.clear();
		//worldServer.suppressPhysics = false;
	}

	public void fillNetherPortal(final String player, final Block node, final BlockFace right, final BlockFace left) throws Throwable
	{
		floodFill(player, node, Material.NETHER_PORTAL, Material.AIR, PortalType.NETHER, BlockFace.DOWN, left, BlockFace.UP, right, false);
	}

	public void fillEnderPortal(final String player, final Block node) throws Throwable
	{
		floodFill(player, node, Material.END_PORTAL, Material.AIR, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
				BlockFace.WEST, false);
	}

	public void cleanEnderPortal(final String player, final Block node) throws Throwable
	{
		floodFill(player, node, Material.AIR, Material.END_PORTAL, PortalType.ENDER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
				BlockFace.WEST, true);
	}
	
	public void cleanNetherPortal(final String player, final Block node, final BlockFace right, final BlockFace left) throws Throwable
	{
		floodFill(player, node, Material.AIR, Material.NETHER_PORTAL, PortalType.NETHER, BlockFace.DOWN, left, BlockFace.UP, right, true);
	}
	
	public void cleanNetherPortal(final String player, final Block node) throws Throwable
	{
		floodFill(player, node, Material.AIR, Material.NETHER_PORTAL, PortalType.NETHER, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
				BlockFace.WEST, true);
	}
}
