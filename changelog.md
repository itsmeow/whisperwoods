# 2.1.1

- 1.19 Forge: Fix for Forge 41.0.94 and higher

# 2.1.0

**Architectury is now REQUIRED for Forge and Fabric versions of the mod.**

- Ported to Architectury API / Fabric
- Updated mod icon
- Added new moth variants and slightly tweaked model
- Tweaked zotzpyre model
- Added new albino zotzpyre variant
- Reworked Zotzpyre AI entirely, it now flies and swoops at the player
- Hirschgeist flame AoE attack is now projectile-based and uses soul flames
- Added new sounds for the Hirschgeist
- Greatly buffed the Hirschgeist (added armor, health)
- 1/4 of all attacks will now "pass through" the Hirschgeist's ectoplasm layer, indicated by a liquid-like sloshing sound
- Fixed bug where Hirschgeist would sit still for several seconds when attacking with flames
- Added new sounds for the Hidebehind
- Optimized moth pathing, prevent getting stuck inside blocks
- Added a tag for moths targeting held light items
- Added a tag for light sources breakable by moths (torches and soul tourches by default)
- Moths will now target all holdable common light sources in the game when held by the player at night
- Tweaked wisp lantern model
- Cleaned up mod data fields
- 1.17: The Hidebehind and Hirschgeist are immune to freezing and can walk on powdered snow
- Fabric: Added Cloth Config API and Mod Menu compatibility

# 2.0.2

- Add Russian Translation (Credit: Magnaderra)
- Add English (Upside Down) support
- Improve lang support for Hand of Fate recipes
- Fix pack format being incorrect causing datapack error
- Fix moths randomly suffocating during flight and when taking off
- Add check so Hirschgeist won't spawn within 300 blocks of each other naturally
- Improve hidebehind targeting behavior and consistency
- Fix hidebehind targeting players in creative
- Fix hidebehind spawning during the day time
- Make hidebehind retaliate even if it's night
- Make hidebehind further transparent during the day
- Make hidebehind 25% damage during the day instead of invincible
- Reduce hidebehind blindness time during retaliation if it's night to ~4 seconds (still 15 during day)
- Change hidebehind blindness level to 1 instead of 2
- 1.16: Fix mobs (except wisps) spawning in nether forests

# 2.0.1

- Rewrite config system and changed field names to make more sense. YOU MUST DELETE YOUR CONFIG OR THINGS WILL BREAK
- Make configs reload live, in-game (from main menu only)
- Change wisp hostile chance config field from 1/x to a percentage
- Make hidebehinds retaliate when attacked while unable to attack back due to light
- Make hidebehinds invulnerable to your attacks when you are in the light / holding a torch
- Make hidebehinds despawn during the daytime
- Make it so you can now tame Better Animals Plus feral wolves by wearing a hirschgeist skull
- Fix "failed to set serializable" crash on newer Java versions
- Fix Zotzpyres not properly serializing types when used with other mods like CarryOn
- Fix hidebehinds not spawning naturally (please delete configs to fix)
- Fix hidebehinds suffocating in logs and trees
- Fixed configuration using last loaded biomes as defaults instead of the actual defaults
- Improved shader support with eye glow on hidebehind and zotzpyre (sometimes rendered completely black) 
- 1.15: Fixed duplicate spawn entries when the world is reloaded
- 1.16: Fixed GlobalEntityTypeAttributes.put warnings on newer Forge versions
- 1.16: Fixed config loading issues
- 1.16: Fixed entities not spawning in modded biomes properly
- 1.16: Fixed not using actual Biome Dictionary for default spawn biome generation
- 1.16: Removed "spinning" bug fix (it was patched by Forge long enough ago, most should have it)

# 2.0.0

- Added Hirschgeist from Better Animals Plus (improved version)
- Added Zotzpyre from Better Animals Plus
- Added Hand of Fate from Better Animals Plus (improved version)
- Added bottled moth
- Added wisp lanterns
- Fixed hidebehind not really detecting if you are looking at it properly
- Fixed hidebehind spawning too often and during the day
- Fixed hidebehind not detecting light levels correctly and barreling into the light
- Hidebehinds will not attack players holding torches due to the light
- Fixed wisp being ultra-rare (despawning)
- Fixed intense lag with wisp soul rendering
- Moths will now detect more light sources and prioritize sources by their light value
- Wisps will now be friendly in peaceful
- Wisps will stop trying to attack creative players

# 1.0.1

- Fixed crash "Redundant texture list for particle whisperwoods:wisp"