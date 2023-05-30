package de.dafuqs.spectrum.config;

import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.annotation.*;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.*;

import java.util.*;

@Config(name = "Spectrum")
public class SpectrumConfig implements ConfigData {
	
	@Comment("The duration in milliseconds ingame recipe/unlock popups stay on the screen")
	public final long ToastTimeMilliseconds = 7500;
	
	@Comment("The reverb decay time for sound effects in Spectrum's dimension")
	public final float DimensionReverbDecayTime = 8.0F;
	
	@Comment("The reverb density for sound effects in Spectrum's dimension")
	public final float DimensionReverbDensity = 0.5F;
	
	@Comment("Integration Packs to skip loading on startup (in case of mod compat errors)")
	public final Set<String> IntegrationPacksToSkipLoading = new HashSet<>();
	
	@Comment("""
			The vanilla anvil caps enchantment levels at the max level for the enchantment
			So enchanted books that exceed the enchantments natural max level get capped
			If true the bedrock anvil will not cap the enchantments level to it's natural max level""")
	public final boolean BedrockAnvilCanExceedMaxVanillaEnchantmentLevel = false;
	
	@Comment("The amount of colored tree patches to generate every X chunks")
	public final int ColoredTreePatchChanceChunk = 75;
	
	@Comment("""
			The chance that an Enderman is holding a special treasure block on spawn
			Separate value for Endermen spawning in the end, since there are LOTS of them there
			Those blocks do not gate progression, so it is not that drastic not finding any right away.
			Better to let players stumble about them organically instead of forcing it.""")
	public float EndermanHoldingEnderTreasureChance = 0.1F;
	public final float EndermanHoldingEnderTreasureInEndChance = 0.0075F;
	
	@Comment("Worlds where shooting stars spawn for players. Shooting Stars will only spawn for players with sufficient progress in the mod")
	public final List<String> ShootingStarWorlds = new ArrayList<>();
	
	@Comment("Worlds where lightning strikes can spawn Storm Stones")
	public final List<String> StormStonesWorlds = new ArrayList<>();
	
	@Comment("chance for a lightning strike to spawn a Storm Stone")
	public float StormStonesChance = 0.4F;
	
	@Comment("""
			Shooting star spawns are checked every night between time 13000 and 22000, every 100 ticks (so 100 chances per night).
			By default, there is a 0.075 ^= 0.75 % chance at each of those check times. Making it ~1 shooting star spawn
			per night per player that unlocked the required progression.""")
	public float ShootingStarChance = 0.075F;
	
	@Comment("The time in ticks it takes a Pigment Pedestal to autocraft a vanilla crafting table recipe without upgrades")
	public int VanillaRecipeCraftingTimeTicks = 40;
	
	@Comment("""
			How fast decay will be spreading on random tick
			can be used to slow down propagation speed of decay in the worlds
			decay does use very few resources, but if your fear of someone letting decay
			spread free or using higher random tick rates than vanilla you can limit the spreading rate here
			
			Fading and Failing do no real harm to the world. If you turn up these values too high players
			may lack the feedback they need that what they are doing is correct
			
			1.0: Every random tick (default)
			0.5: Every second random tick
			0.0: Never (forbidden - players would be unable to progress)""")
	public float FadingDecayTickRate = 1.0F;
	public final float FailingDecayTickRate = 1.0F;
	public float RuinDecayTickRate = 1.0F;
	public final float ForfeitureDecayTickRate = 1.0F;
	
	@Comment("Whether bottles can be used to pick up decay. Default is true.")
	public final boolean CanPickUpFading = true;
	public final boolean CanPickUpFailing = true;
	public final boolean CanPickUpRuin = true;
	public final boolean CanPickUpForfeiture = true;
	
	@Comment("Whether decay can take over block entities. Defaults to true.")
	public final boolean FadingCanDestroyBlockEntities = true;
	public final boolean FailingCanDestroyBlockEntities = true;
	public final boolean RuinCanDestroyBlockEntities = true;
	public final boolean ForfeitureCanDestroyBlockEntities = true;
	
	@Comment("The audio volume for Spectrums crafting blocks. Set to 0.0 to turn those sounds off completely.")
	public final float BlockSoundVolume = 0.5F;
	
	@Comment("""
			Enable or disable specific enchantments. Resonance and Voiding can not be disabled.
			This does only disable the registration of said Enchantments, not all recipes based on them (except for Enchantment Upgrade Recipes)
			""")
	public final boolean FoundryEnchantmentEnabled = true;
	public final boolean ExuberanceEnchantmentEnabled = true;
	public final boolean InventoryInsertionEnchantmentEnabled = true;
	public final boolean PestControlEnchantmentEnabled = true;
	public final boolean TreasureHunterEnchantmentEnabled = true;
	public final boolean DisarmingEnchantmentEnabled = true;
	public final boolean FirstStrikeEnchantmentEnabled = true;
	public final boolean ImprovedCriticalEnchantmentEnabled = true;
	public final boolean InertiaEnchantmentEnabled = true;
	public final boolean CloversFavorEnchantmentEnabled = true;
	public final boolean SniperEnchantmentEnabled = true;
	public final boolean TightGripEnchantmentEnabled = true;
	public final boolean SteadfastEnchantmentEnabled = true;
	public final boolean IndestructibleEnchantmentEnabled = true;
	public final boolean BigCatchEnchantmentEnabled = true;
	public final boolean RazingEnchantmentEnabled = true;
	public final boolean InexorableEnchantmentEnabled = true;
	
	@Comment("The max levels for all Enchantments")
	public int TreasureHunterMaxLevel = 3;
	public final int DisarmingMaxLevel = 2;
	public final int FirstStrikeMaxLevel = 2;
	public final int ImprovedCriticalMaxLevel = 2;
	public final int InertiaMaxLevel = 3;
	public final int CloversFavorMaxLevel = 3;
	public final int TightGripMaxLevel = 2;
	public int BigCatchMaxLevel = 3;
	
	@Comment("Exuberance increases experience gained when killing mobs. With 25% bonus XP and 5 levels this would mean 2,25x XP on max level")
	public int ExuberanceMaxLevel = 5;
	public float ExuberanceBonusExperiencePercentPerLevel = 0.25F;
	
	@Comment("In vanilla, crits are a flat 50 % damage bonus. Improved Critical increases this damage by additional 50 % per level by default")
	public float ImprovedCriticalExtraDamageMultiplierPerLevel = 0.5F;
	
	@Comment("Flat additional damage dealt with each level of the First Strike enchantment")
	public float FirstStrikeDamagePerLevel = 2.0F;
	
	@Comment("The percentile a mobs armor/hand stacks are being dropped when hit with a Disarming enchanted weapon per the enchantments level")
	public final float DisarmingChancePerLevelMobs = 0.01F;
	
	@Comment("If > 0 the Disarming Enchantment is able to remove armor and hand tools from a hit player. Should be a far smaller chance than for mobs")
	public final float DisarmingChancePerLevelPlayers = 0.001F;
	
	@Comment("The % attack speed boost each level of Tight Grip gives to a tool")
	public final float TightGripAttackSpeedBonusPercentPerLevel = 0.0625F;
	
	@Comment("The duration a glow ink sac gives night vision when wearing a glow vision helmet in seconds")
	public final int GlowVisionGogglesDuration = 240;
	
	public final int GemstoneArmorHelmetProtection = 3;
	public final int GemstoneArmorChestplateProtection = 7;
	public final int GemstoneArmorLeggingsProtection = 5;
	public final int GemstoneArmorBootsProtection = 3;
	public final float GemstoneArmorToughness = 0.0F;
	public final float GemstoneArmorKnockbackResistance = 0.0F;
	
	public final int GemstoneArmorWeaknessAmplifier = 1;
	public final int GemstoneArmorSlownessAmplifier = 1;
	public final int GemstoneArmorAbsorptionAmplifier = 0;
	public final int GemstoneArmorResistanceAmplifier = 1;
	public final int GemstoneArmorRegenerationAmplifier = 1;
	public final int GemstoneArmorSpeedAmplifier = 2;
	
	public final int BedrockArmorHelmetProtection = 5;
	public final int BedrockArmorLeggingsProtection = 9;
	public final int BedrockArmorChestplateProtection = 13;
	public final int BedrockArmorBootsProtection = 5;
	public final float BedrockArmorToughness = 3.0F;
	public final float BedrockArmorKnockbackResistance = 0.3F;
	
	public final int MaxLevelForEffectsInLesserPotionPendant = 3;
	public final int MaxLevelForEffectsInGreaterPotionPendant = 1;
	
	@Comment("""
			True will prevent the spread of Decay blocks in claims.
			Only enable when necessary and communicate to your players that those blocks will not work in their claims.
			If any player comes to the Spectrum devs claiming that decay does not spread for them and therefore they could not progress
			without them gotten told that, your will get put on the 'bad pack devs' list and this config setting removed again
			""")
	public final boolean DecayIsStoppedByClaimMods = false;
	
	@Comment("""
			By Default, Roughly Enough Items will show a 'recipe not unlocked yet' screen for not yet unlocked recipes.
			Setting this value to false will instead not show this screen, showing no recipes whatsoever, until unlocked
			""")
	public final boolean REIListsRecipesAsNotUnlocked = true;
	
	@Comment("""
			If the player has Azure Dike Charges: Where should they be rendered on the screen. Default: Over the food bar
			Only touch those values if you have other mods that render GUI overlays!
			""")
	public final int AzureDikeHudOffsetX = 0;
	public final int AzureDikeHudOffsetY = 0;
	public final int AzureDikeHudOffsetYWithArmor = -10;
	public final int AzureDikeHudOffsetYForEachRowOfExtraHearts = -10;
	
	@Override
	public void validatePostLoad() {
		if (VanillaRecipeCraftingTimeTicks <= 0) {
			VanillaRecipeCraftingTimeTicks = 40;
		}
		if (FadingDecayTickRate <= 0.1) {
			FadingDecayTickRate = 1.0F;
		}
		if (FailingDecayTickRate <= 0.1) {
			FadingDecayTickRate = 1.0F;
		}
		if (RuinDecayTickRate <= 0.1) {
			RuinDecayTickRate = 1.0F;
		}
		if (ShootingStarChance <= 0.001) {
			ShootingStarChance = 0.01F;
		}
		if (StormStonesChance <= 0.03) {
			StormStonesChance = 0.3F;
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
		if (BigCatchMaxLevel <= 0) {
			BigCatchMaxLevel = 3;
		}
		
		if (ShootingStarWorlds.isEmpty()) {
			ShootingStarWorlds.add("minecraft:overworld");
			ShootingStarWorlds.add("starry_sky:starry_sky");
			ShootingStarWorlds.add("paradise_lost:paradise_lost");
		}
		if (StormStonesWorlds.isEmpty()) {
			StormStonesWorlds.add("minecraft:overworld");
			StormStonesWorlds.add("starry_sky:starry_sky");
			StormStonesWorlds.add("paradise_lost:paradise_lost");
		}
	}
	
}
