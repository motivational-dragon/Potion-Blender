# Potion Blender

## Overview
Potion Blender is a small mod to merge potion effects into one item, allowing for interesting combinations.

<details open>
<summary>Gif overview</summary>
<img src="https://github.com/motivational-dragon/Potion-Blender/blob/b889de864c5940b860df42366c82e25ca3d16908/img/potion_craft.gif"  alt=""/>
</details>

## How to use

### 1 - Cauldron

First, you need to make a special cauldron. Do so by right-clicking a campfire with a vanilla cauldron.

### 2 - Starting to blend

Then, throw a potion into the cauldron. It can be any vanilla potion that is not Splash or Lingering. The potion effects will be the base of your combined brew. You can then add up to two more potions (neither splash nor lingering) to add their effects to the final product.

### 3 - The final ingredient

Now it is time to finish the brewing and choose the type of potion you desire. You may choose one of three of these ingredients :

-  Nether wart ; Normal potion
-  Gunpowder ; Splash potion
-  Dragon Breath ; Lingering potion


### 4 - Using arrows

The combined potions can be used with arrows using the vanilla craft to add the combined effects to the arrows.

### 5 - Cancelling the blend

You can reset the cauldron anytime by right-clicking on the cauldron to get back your ingredients.

### Effect stacking
Adding the same effect to a potion multiple times results in effects adding. It, however, comes with a price as with each stacked effect gain decay. The first stacked effect adds 50% of its duration, and the 2nd one 25%.

Instant Effects does not stack as duplicated effects are lost.

## Configuration
The configuration file is in your .minecraft folder at potionblender/potion_blender_config.json

The configurable options are:

- max_effects: The maximum number of potions in a cauldron. Be careful when updating this value in an already existing world, as it may cause desync on an already placed cauldron.


## Known issues
Some rendering mods (like sodium) can sometimes cause the cauldron to appear with water instead of the potion's colors.
## License
This mod is available under the GPLv3 license.
