[CUSTOM PORTALS]

Description:
This plugin allows you to create custom Nether and Ender portals.
It means you can create these portals using any blocks for the frame and any shapes.

Configuration:
Just run the plugin once and the config file will be created with default values. They are modifiable in game and automatically saved.
* usepermissions: [true|false] -> enable the internal Bukkit permissions system or use the OP statuts
* enderframeblock: [BlockType|other] -> enable the use of a block type for building Ender portals or disable if another value is used
* netherframeblock: [BlockType|other] -> enable the use of a block type for building Nether portals or disable if another value is used
* maxportalblocks: [positive number] -> define the maximum portal blocks the plugins is allowed to create

Commands:
Main command is /customportals (with some convenient aliases: cp, custportals, customportal, cportal).
/cp on : your player is able to create custom portals
/cp off : your player is no more able to create custom portals
/cp enderframeblock [BlockType|other] : configure the ender frame block to use in the portals or disable if other value used
/cp netherframeblock [BlockType|other] : configure the nether frame block to use in the portals or disable if other value used
/cp maxportalblocks [positive number] : configure the maximum portal blocks the plugin is allowed to create (auto saved)
/cp usepermissions [true|false] : configure if the plugin uses permissions (auto saved)


How to create portals:
* Nether: enable the custom portals for your player with /cp on. Then, get a flint and steel in your hand and click on the upper face of the lower block
of the portal frame. In case of wrong portal shape, just break one block, and all the portal will disappear. Try to be in front of the portal 
when you create it, otherwise, your direction will be wrong and the creation of the portal may fail.
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

Changelogs:
Version 1.1.3 on 2013-07-20:
* Support Dev Build 1.6.2-R0.1

Version 1.1.2 on 2013-04-08:
* Support Dev Build 1.5.1-R0.2
* Changed the 'update block to portal' method to use only the Bukkit API. (1 block wide nether portals may not generate properly)

Version 1.1.1 on 2013-02-11:
* Support Recommanded Build 1.4.7-R1.0
* Added a check of the type of the clicked block for Nether Portal to avoid ability to build portal on wrong frames.
* Fixed the clean nether portal method to work as the fill nether portal method (up-right-down-left, instead of coordinates)

Version 1.1.0 on 2012-24-12:
* Support Bukkit Dev Build 1.4.6
* Added new configuration parameters (netherframeblock and enderframeblock) with respective commands to choose one block for each kind of portal.
* Fixed directions support 
* Added auto-cleaning when error is detected while creating