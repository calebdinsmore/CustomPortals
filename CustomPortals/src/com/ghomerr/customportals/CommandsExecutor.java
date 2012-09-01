package com.ghomerr.customportals;

import org.bukkit.ChatColor;
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
				player.sendMessage(ChatColor.YELLOW + "Usage: /cp ON|OFF or /cp userpermissions true|false or /cp maxportalblocks <positive number>");
			}
		}
		else
		{
			sender.sendMessage("This command is only usable by Players");
		}
		
		return true;
	}
}
