# MultiInventoryManager
Bukkit plugin to manage a player's inventory though worlds. Built for Minecraft 1.12.

## Requirements:

- Bukkit or Spigot v1.12
- A .yml file "MIMconfig.yml" in the /plugin/ directory.
### Contents:
```
basePath: {insert_path}
bookPath: {insert_path}

adventure: {true/false}
spectator: {true/false}
```
- basePath: Defines the save directory of the player's saves. [Default: plugins/MultiInventoryManager/playerdata]
- bookPath: Defines the save directory of the player's books. [Default: plugins/MultiInventoryManager/books]
- adventure: (De)activate adventure gamemode logging. [Default: false]
- spectator: (De)activate spectator gamemode logging. [Default: false]

## Features
:white_check_mark: Saves player's inventory and data (Health, Saturation, Level and Experience Points etc.) upon leave and loads them when the player rejoins. The same is also done when the player changes his/her gamemode.

:white_check_mark: Keeps track of item's enchantments, meta data such as custom names or colour as well as armour and offhand items.

:white_check_mark: Saves player's ender chest inventory.

:white_check_mark: Saves player's shulker box inventory.


## Upcoming Features
- Command `/mim delete [playerName]` to remove specific player data file.
  - __Is not working properly__

