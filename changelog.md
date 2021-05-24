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