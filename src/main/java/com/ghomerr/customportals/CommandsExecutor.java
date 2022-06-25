package com.ghomerr.customportals;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsExecutor implements CommandExecutor
{
	public CustomPortals plugin = null;

	public CommandsExecutor (final CustomPortals plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] split)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			
			if (split.length > 0)
			{
				final String param1 = split[0];
				final PlayerCommand command = PlayerCommand.getPlayerCommand(param1);
				
				if (plugin.usepermissions && player.hasPermission(command.getPerm()) || !plugin.usepermissions && player.isOp())
				{
					final String name = player.getName();
					String param2 = null;
					if (split.length > 1)
					{
						param2 = split[1];
					}
					
					switch (command)
					{
						case ON:
							if (plugin.portalCrafters.contains(name))
							{
								player.sendMessage(ChatColor.YELLOW + "Already ON");
							}
							else
							{
								plugin.portalCrafters.add(name);
								player.sendMessage(ChatColor.GREEN + "CustomPortals ON");
							}
							break;
							
						case OFF:
							if (!plugin.portalCrafters.contains(name))
							{
								player.sendMessage(ChatColor.YELLOW + "Already OFF");
							}
							else
							{
								plugin.portalCrafters.remove(name);
								player.sendMessage(ChatColor.GREEN + "CustomPortals OFF");
							}
							break;
							
						case ENDERFRAMEBLOCK:
							if (param2 != null)
							{
								final Material mat = BlockUtils.getBlockType(param2);
								if (mat != null)
								{
									plugin.enderFrameBlock = mat;
									plugin.getConfig().set(ConfigParam.ENDER_FRAME_BLOCK.getValue(), mat.name());
									plugin.saveConfig();
									player.sendMessage(ChatColor.YELLOW + "Ender Frame block new value: " + mat.name());
								}
								else
								{
									plugin.enderFrameBlock = null;
									plugin.getConfig().set(ConfigParam.ENDER_FRAME_BLOCK.getValue(), "");
									plugin.saveConfig();
									player.sendMessage(ChatColor.YELLOW + "Ender Frame block disabled.");
								}
							}
							else
							{
								player.sendMessage(ChatColor.YELLOW + "Ender Frame block: " + ((plugin.enderFrameBlock == null)? "disabled" : plugin.enderFrameBlock));
							}
							break;
							
						case NETHERFRAMEBLOCK:
							if (param2 != null)
							{
								final Material mat = BlockUtils.getBlockType(param2);
								if (mat != null)
								{
									plugin.netherFrameBlock = mat;
									plugin.getConfig().set(ConfigParam.NETHER_FRAME_BLOCK.getValue(), mat.name());
									plugin.saveConfig();
									player.sendMessage(ChatColor.YELLOW + "Nether Frame block new value: " + mat.name());
								}
								else
								{
									plugin.netherFrameBlock = null;
									plugin.getConfig().set(ConfigParam.NETHER_FRAME_BLOCK.getValue(), "");
									plugin.saveConfig();
									player.sendMessage(ChatColor.YELLOW + "Nether Frame block disabled.");
								}
							}
							else
							{
								player.sendMessage(ChatColor.YELLOW + "Nether Frame block: " + ((plugin.netherFrameBlock == null)? "disabled" : plugin.netherFrameBlock));
							}
							break;
							
						case USEPERMISSIONS:
							if (param2 != null)
							{
								final boolean newState = Boolean.parseBoolean(param2);
								plugin.usepermissions = newState;
								plugin.getConfig().set(ConfigParam.USE_PERMISSIONS.getValue(), newState);
								plugin.saveConfig();
								player.sendMessage(ChatColor.YELLOW + "Use permissions new value: " + newState);
							}
							else
							{
								player.sendMessage(ChatColor.YELLOW + "Use permissions: " + plugin.usepermissions);
							}
							break;
							
						case MAXPORTALBLOCKS:
							if (param2 != null)
							{
								try
								{
									final int newValue = Integer.parseInt(param2);
									if (newValue > 0)
									{
										plugin.maxPortalBlocs = newValue;
										plugin.getConfig().set(ConfigParam.MAX_PORTAL_BLOCKS.getValue(), newValue);
										plugin.saveConfig();
										player.sendMessage(ChatColor.YELLOW + "Max portal blocks new value: " + newValue);
									}
									else
									{
										player.sendMessage(ChatColor.RED + "Only positive values for maxportalblocks: " + param2);
									}
								}
								catch (final Throwable th)
								{
									player.sendMessage(ChatColor.RED + "Wrong value for maxportalblocks: " + param2);
								}
							}
							else
							{
								player.sendMessage(ChatColor.YELLOW + "Max portal blocks: " + plugin.maxPortalBlocs);
							}
							break;
							
						default: 
							player.sendMessage(ChatColor.RED + "Wrong command: " + param1);
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "No permissions!");
				}
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "Usage: /cp ON|OFF to enable Custom Portal for you.");
				player.sendMessage(ChatColor.YELLOW + "/cp userpermissions true|false to update permissions usage.");
				player.sendMessage(ChatColor.YELLOW + "/cp maxportalblocks [positive number] to update the max portal block you can fill at once.");
				player.sendMessage(ChatColor.YELLOW + "/cp netherframeblock [BlockType|other] to enable or disable globally the Nether frame block allowed.");
				player.sendMessage(ChatColor.YELLOW + "/cp enderframeblock [BlockType|other] to enable or disable globally the Ender frame block allowed.");
			}
		}
		else
		{
			sender.sendMessage("This command is only usable by Players");
		}
		
		return true;
	}
}
