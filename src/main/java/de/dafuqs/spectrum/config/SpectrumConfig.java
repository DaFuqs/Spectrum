package de.dafuqs.spectrum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = "Spectrum")
public class SpectrumConfig implements ConfigData {
	
	@Comment("""
			The heights where citrine and topaz geodes will spawn
			By default citrine will generate slightly below sea level (y=32-60)
			while topaz will generate at the top of mountains (everywhere from y=82+)
			if the worldgen has lots of high mountains consider raising the TopazGeodeMinFixedGenerationHeight""")
	public int CitrineGeodeMinAboveBottomGenerationHeight = 96;
	public int CitrineGeodeFixedMaxGenerationHeight = 55;
	public int TopazGeodeMinFixedGenerationHeight = 82;
	public int TopazGeodeMaxBelowTopGenerationHeight = 0;
	
	@Comment("Every x chunks there is a chance for a geode to generate, Moonstone geodes do not spawn in the Overworld")
	public int TopazGeodeChunkChance = 7;
	public int CitrineGeodeChunkChance = 50;
	public int MoonstoneGeodeChunkChance = 35;
	
	@Comment("The amount of colored tree patches to generate every X chunks")
	public int ColoredTreePatchChanceChunk = 75;
	
	@Comment("""
			The chance that an Enderman is holding a special treasure block on spawn
			Separate value for Endermen spawning in the end, since there are LOTS of them there
			Those blocks do not gate progression, so it is not that drastic not finding any right away.
			Better to let players stumble about them organically instead of forcing it.""")
	public float EndermanHoldingEnderTreasureChance = 0.08F;
	public float EndermanHoldingEnderTreasureInEndChance = 0.005F;
	
	@Comment("Worlds where shooting stars spawn for players. Shooting Stars will only spawn for players with sufficient progress in the mod")
	public List<String> ShootingStarWorlds = new ArrayList<>();
	
	@Comment("Worlds where lightning strikes can spawn Storm Stones")
	public List<String> LightningStonesWorlds = new ArrayList<>();
	
	@Comment("chance for a lightning strike to spawn a Storm Stone")
	public float LightningStonesChance = 0.4F;
	
	@Comment("""
			Shooting star spawns are checked every night between time 13000 and 22000, every 100 ticks (so 90 chances per night).
			By default, there is a 0.004 ^= 0.4 % chance at each of those check times. Making it ~1 shooting star spawn
			per night per player that unlocked the required progression.""")
	public float ShootingStarChance = 0.004F;
	
	@Comment("The biomes where the biome specific plants are growing")
	public List<String> MermaidsBrushGenerationBiomes = new ArrayList<>();
	public List<String> QuitoxicReedsGenerationBiomes = new ArrayList<>();
	
	@Comment("The time in ticks it takes a Pigment Pedestal to autocraft a vanilla crafting table recipe without upgrades")
	public int VanillaRecipeCraftingTimeTicks = 40;
	
	@Comment("""
			How fast decay will be spreading on random tick
			can be used to slow down propagation speed of decay in the worlds
			decay does use very few resources, but if your fear of someone letting decay
			spread free or using higher random tick rates than vanilla you can limit the spreading rate here
			
			Fading and Failing do no real harm to the world. If you turn up these values too high players
			may lack the feedback they need that what they are doing is correct
			
			1.0: every random tick (default)
			0.5: Every second random tick
			0.0: never (forbidden - players would be unable to progress)""")
	public float FadingDecayTickRate = 1.0F;
	public float FailingDecayTickRate = 1.0F;
	public float RuinDecayTickRate = 1.0F;
	public float TerrorDecayTickRate = 1.0F;
	
	@Comment("The audio volume for Spectrums crafting blocks. Set to 0.0 to turn those sounds off, completely.")
	public float BlockSoundVolume = 1.0F;
	
	@Comment("""
			Enable or disable specific enchantments. Resonance and Voiding can not be disabled.
			This does only disable the registration of said Enchantments, not all recipes based on them (except for Enchantment Upgrade Recipes)
			""")
	public boolean AutoSmeltEnchantmentEnabled = true;
	public boolean ExuberanceEnchantmentEnabled = true;
	public boolean InventoryInsertionEnchantmentEnabled = true;
	public boolean PestControlEnchantmentEnabled = true;
	public boolean TreasureHunterEnchantmentEnabled = true;
	public boolean DisarmingEnchantmentEnabled = true;
	public boolean FirstStrikeEnchantmentEnabled = true;
	public boolean ImprovedCriticalEnchantmentEnabled = true;
	public boolean InertiaEnchantmentEnabled = true;
	public boolean CloversFavorEnchantmentEnabled = true;
	public boolean SniperEnchantmentEnabled = true;
	public boolean TightGripEnchantmentEnabled = true;
	public boolean SteadfastEnchantmentEnabled = true;
	public boolean IndestructibleEnchantmentEnabled = true;
	
	@Comment("The max levels for all Enchantments")
	public int TreasureHunterMaxLevel = 3;
	public int DisarmingMaxLevel = 2;
	public int FirstStrikeMaxLevel = 2;
	public int ImprovedCriticalMaxLevel = 2;
	public int InertiaMaxLevel = 3;
	public int CloversFavorMaxLevel = 3;
	public int TightGripMaxLevel = 2;
	
	@Comment("Exuberance increases experience gained when killing mobs. With 25% bonus XP and 5 levels this would mean 2,25x XP on max level")
	public int ExuberanceMaxLevel = 5;
	public float ExuberanceBonusExperiencePercentPerLevel = 0.25F;
	
	@Comment("In vanilla, crits are a flat 50 % damage bonus. Improved Critical increases this damage by additional 50 % per level by default")
	public float ImprovedCriticalExtraDamageMultiplierPerLevel = 0.5F;
	
	@Comment("Flat additional damage dealt with each level of the First Strike enchantment")
	public float FirstStrikeDamagePerLevel = 2.0F;
	
	@Comment("The percentile a mobs armor/hand stacks are being dropped when hit with a Disarming enchanted weapon per the enchantments level")
	public float DisarmingChancePerLevelMobs = 0.01F;
	
	@Comment("If > 0 the Disarming Enchantment is able to remove armor and hand tools from a hit player. Should be a far smaller chance than for mobs")
	public float DisarmingChancePerLevelPlayers = 0.001F;
	
	@Comment("The duration a glow ink sac gives night vision when wearing a glow vision helmet in seconds")
	public int GlowVisionGogglesDuration = 240;
	
	@Comment("By Default, Roughly Enough Items will show a 'recipe not unlocked yet' screen for not yet unlocked recipes. Setting this value to false will instead not show this screen, showing no recipes whatsoever, until unlocked")
	public boolean REIListsRecipesAsNotUnlocked = true;
	
	@Comment("""
			If the player has Azure Dike Charges: Where should they be rendered on the screen. Default: Over the food bar
			Only touch those values if you have other mods that render GUI overlays!
			""")
	public int azureDikeHudOffsetX = 0;
	public int azureDikeHudOffsetY = 0;
	public int azureDikeHudOffsetXLackingAir = 0;
	public int azureDikeHudOffsetYLackingAir = -10;
	
	@Override
	public void validatePostLoad() {
		if (VanillaRecipeCraftingTimeTicks <= 0) {
			VanillaRecipeCraftingTimeTicks = 40;
		}
		if (FadingDecayTickRate <= 0) {
			FadingDecayTickRate = 1.0F;
		}
		if (FailingDecayTickRate <= 0) {
			FadingDecayTickRate = 1.0F;
		}
		if (RuinDecayTickRate <= 0) {
			RuinDecayTickRate = 1.0F;
		}
		if (ShootingStarChance <= 0) {
			ShootingStarChance = 0.01F;
		}
		if (LightningStonesChance <= 0) {
			ShootingStarChance = 0.3F;
		}
		if (EndermanHoldingEnderTreasureChance <= 0) {
			EndermanHoldingEnderTreasureChance = 0.05F;
		}
		if (TreasureHunterMaxLevel <= 0) {
			TreasureHunterMaxLevel = 3;
		}
		if (ExuberanceMaxLevel <= 0) {
			ExuberanceMaxLevel = 5;
		}
		if (ExuberanceBonusExperiencePercentPerLevel <= 0) {
			ExuberanceBonusExperiencePercentPerLevel = 0.2F;
		}
		if (ImprovedCriticalExtraDamageMultiplierPerLevel <= 0) {
			ImprovedCriticalExtraDamageMultiplierPerLevel = 0.5F;
		}
		if (FirstStrikeDamagePerLevel <= 0) {
			FirstStrikeDamagePerLevel = 3.0F;
		}
		
		if (ShootingStarWorlds.isEmpty()) {
			ShootingStarWorlds.add("minecraft:overworld");
			ShootingStarWorlds.add("starry_sky:starry_sky");
		}
		if (LightningStonesWorlds.isEmpty()) {
			LightningStonesWorlds.add("minecraft:overworld");
			LightningStonesWorlds.add("starry_sky:starry_sky");
		}
		if (MermaidsBrushGenerationBiomes.isEmpty()) {
			MermaidsBrushGenerationBiomes.add("minecraft:ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:cold_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:frozen_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:lukewarm_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:warm_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:deep_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:deep_cold_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:deep_frozen_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:deep_warm_ocean");
			MermaidsBrushGenerationBiomes.add("minecraft:deep_lukewarm_ocean");
		}
		if (QuitoxicReedsGenerationBiomes.isEmpty()) {
			QuitoxicReedsGenerationBiomes.add("minecraft:swamp");
		}
	}
	
}
