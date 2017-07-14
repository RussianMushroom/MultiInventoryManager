# MultiInventoryManager
Bukkit plugin to manage a player's inventory though worlds. Built for Minecraft 1.12.

## Requirements:

- Bukkit or Spigot v1.12
- A .yml file "MIMconfig.yml" in the /plugin/ directory.
### Contents:
```
basePath: {insert_path}

adventure: {true/false}
spectator: {true/false}
```
- basePath: Defines the save directory of the player's saves. [Default: plugins/MIM/playerdata]
- adventure: (De)activate adventure gamemode logging. [Default: false]
- spectator: (De)activate spectator gamemode logging. [Default: false]

## Features
- Saves player's inventory and data (Health, Saturation, Level and Experience Points etc.) upon leave and loads them when the player rejoins. The same is also done when the player changes his/her gamemode.
- Keeps track of item's enchantments, meta data such as custom names or colour. 

## Future Features
- Saves player's ender chest inventory.
