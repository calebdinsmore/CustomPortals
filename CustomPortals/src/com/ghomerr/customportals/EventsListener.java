package com.ghomerr.customportals;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListener implements Listener
{
	public static final Logger LOGGER = Logger.getLogger("Minecraft");
	public CustomPortals plugin = null;

	public EventsListener (final CustomPortals plugin)
	{
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	private boolean playerHasPermission(final Player player, final String node)
	{
		return plugin.usepermissions && player.hasPermission(node) 
				|| !plugin.usepermissions && player.isOp();
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		final String playerName = player.getName();

		if (plugin.portalCrafters.contains(playerName))
		{
			PortalType portalType = null;

			if (event.getMaterial() == Material.FLINT_AND_STEEL)
			{
				portalType = PortalType.NETHER;
			}
			else if (event.getMaterial() == Material.EYE_OF_ENDER)
			{
				portalType = PortalType.ENDER;
			}
			else if (event.getMaterial() == Material.WOOL)
			{
				portalType = PortalType.CUSTOM;
			}

			boolean errorDetected = false;
			if (portalType != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				final Block clickedBlock = event.getClickedBlock();
				Block firstNode = null;

				switch (portalType)
				{
					case NETHER:
						if (event.getBlockFace() != BlockFace.UP)
						{
							return;
						}
						else
						{
							firstNode = clickedBlock.getRelative(BlockFace.UP);
						}
						break;

					case ENDER:
						if (event.getBlockFace() != BlockFace.NORTH && event.getBlockFace() != BlockFace.EAST
								&& event.getBlockFace() != BlockFace.SOUTH && event.getBlockFace() != BlockFace.WEST)
						{
							return;
						}
						else
						{
							firstNode = clickedBlock.getRelative(event.getBlockFace());
						}
						break;
						
					case CUSTOM:
						firstNode = clickedBlock;
						break;
						
					default:
						LOGGER.warning("[CustomPortals] Unhandled portal type on CLICKING: " + portalType);
				}

				try
				{
					if (firstNode != null && firstNode.getType() == Material.AIR)
					{
						switch (portalType)
						{
							case NETHER:
								
								if (playerHasPermission(player, "customportals.nether"))
								{
									final BlockFace eyesDirection = plugin.getYawDirection(player.getEyeLocation().getYaw());
									LOGGER.info("[CP-Debug] eyesDirection = " + eyesDirection);
									if (eyesDirection != null)
									{
										event.setCancelled(true);
										final BlockFace portalDirection = NextDirection.get(eyesDirection);
										LOGGER.info("[CP-Debug] portalDirection = " + portalDirection);
										plugin.fillNetherPortal(playerName, firstNode, portalDirection, OppositeDirection.get(portalDirection));
									}
								}
								break;
	
							case ENDER:
								if (playerHasPermission(player, "customportals.ender"))
								{
									final World world = player.getWorld();
									if (world.getEnvironment() != Environment.THE_END)
									{
										event.setCancelled(true);
										plugin.fillEnderPortal(playerName, firstNode);
									}
								}
								break;
								
							default:
								LOGGER.warning("[CustomPortals] Unhandled portal type on CREATION: " + portalType);
						}
					}
					else if (firstNode != null && portalType == PortalType.CUSTOM)
					{
						switch(firstNode.getType())
						{
							case ENDER_PORTAL:
								if (playerHasPermission(player, "customportals.clean"))
								{
									event.setCancelled(true);
									plugin.cleanEnderPortal(playerName, clickedBlock);
								}
								break;
						}
					}
				}
				catch (final IllegalFrameBlock ex)
				{
					player.sendMessage(ChatColor.RED + ex.getMessage());
					errorDetected = true;
					
				}
				catch (final Throwable th)
				{
					player.sendMessage(ChatColor.RED + "Error while filling custom portal. Contact an admin.");
					errorDetected = true;
					
					LOGGER.severe("[CustomPortals] Error while creating a portal: ");
					th.printStackTrace();
				}
				
				if (errorDetected)
				{
					try
					{
						switch (portalType)
						{
							case NETHER:
									plugin.cleanNetherPortal(playerName, firstNode);
								break;
							
							case ENDER:
								plugin.cleanEnderPortal(playerName, firstNode);
								break;
								
							default:
								LOGGER.warning("[CustomPortals] Unhandled portal type on CLEANING after error: " + portalType);
						}
					}
					catch (final Throwable th)
					{
						LOGGER.severe("[CustomPortals] Error while cleaning a portal: ");
						th.printStackTrace();
					}
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuitEvent(final PlayerQuitEvent event)
	{
		final String name = event.getPlayer().getName();
		if (plugin.portalCrafters.contains(name))
		{
			plugin.portalCrafters.remove(name);
		}
	}
}
