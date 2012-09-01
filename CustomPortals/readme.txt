[CUSTOM PORTALS]

Description:
This little plugin allows you to create custom Nether and Ender portals.
It means you can create these portals using any blocks for the frame and any shapes.

Configuration:
Just run the plugin once and the config file will be created with default values. They are modifiable in game and automatically saved.
* usepermissions: [true|false] -> enable the internal Bukkit permissions system or use the OP statuts
* maxportalblocks: <positive number> -> define the maximum portal blocks the plugins is allowed to create

Commands:
Main command is /customportals (with some convenient aliases: cp, custportals, customportal, cportal).
/cp on : your player is able to create custom portals
/cp off : your player is no more able to create custom portals
/cp usepermissions [true|false] : configure if the plugin uses permissions (auto saved)
/cp maxportalblocks [positive number] : configure the maximum portal blocks the plugin is allowed to create (auto saved)

How to create portals:
* Nether: enable the custom portals for your player with /cp on. Then, get a flint and steel in your hand and click on the upper face of the lower block
of the portal frame. In case of wrong portal shape, just break one block, and all the portal will disappear. 
* Ender: enable the custom portals for your player with /cp on. Then, get an eye of ender in your hand and click on the inside face of the portal
frame block. In case of wrong portal shape, you can clean the ender portal by placing a wool block (any color) on the ender portal block. If
you have enable the custom portals (/cp on), the plugin will clean the entire ender portal. You cannot create ender portal in Ender worlds.

Permissions:
customportals.on # allows to enable the custom portals mode
customportals.off # allows to disable the custom portals mode
customportals.conf # allows to modify the configuration
customportals.nether # allows to create a custom nether portal
customportals.ender # allows to create a custom ender portal
customportals.clean # allows to clean ender portals