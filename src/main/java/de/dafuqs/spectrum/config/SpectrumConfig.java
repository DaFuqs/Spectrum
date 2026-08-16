package de.dafuqs.spectrum.config;

import net.minecraft.resources.*;
import net.neoforged.neoforge.common.*;
import org.apache.commons.lang3.tuple.*;

import java.util.*;

// TODO: split into server/client config
public class SpectrumConfig {
	
	public static final SpectrumConfig CONFIG;
	public static final ModConfigSpec CONFIG_SPEC;
	
	static {
		Pair<SpectrumConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(SpectrumConfig::new);
		
		//Store the resulting values
		CONFIG = pair.getLeft();
		CONFIG_SPEC = pair.getRight();
	}
	
	public final ModConfigSpec.ConfigValue<Integer> ToastTimeMilliseconds;
	public final ModConfigSpec.ConfigValue<Double> DimensionReverbDecayTime;
	public final ModConfigSpec.ConfigValue<Double> DimensionReverbDensity;
	public final ModConfigSpec.ConfigValue<Boolean> WindSim;
	public final ModConfigSpec.ConfigValue<Boolean> ReducedParticles;
	public final ModConfigSpec.ConfigValue<Integer> WindSimInterval;
	public final ModConfigSpec.ConfigValue<Boolean> PostProcessShaders;
	public final ModConfigSpec.ConfigValue<Boolean> AlwaysSpawnLightBlockParticles;
	public final ModConfigSpec.ConfigValue<Boolean> PastelNetworkParticles;
	public final ModConfigSpec.ConfigValue<Double> DimensionBrightnessMod;
	public final ModConfigSpec.ConfigValue<List<? extends String>> IntegrationPacksToSkipLoading;
	public final ModConfigSpec.ConfigValue<Boolean> AddItemTooltips;
	public final ModConfigSpec.ConfigValue<Boolean> BedrockAnvilCanExceedMaxVanillaEnchantmentLevel;
	public final ModConfigSpec.ConfigValue<Double> EndermanHoldingTreasureChance;
	public final ModConfigSpec.ConfigValue<Double> EndermanHoldingTreasureInEndChance;
	public final ModConfigSpec.ConfigValue<List<? extends String>> ShootingStarDimensions;
	public final ModConfigSpec.ConfigValue<List<? extends String>> StormStoneDimensions;
	public final ModConfigSpec.ConfigValue<Double> StormStoneSpawnChance;
	public final ModConfigSpec.ConfigValue<Double> ShootingStarSpawnChance;
	public final ModConfigSpec.ConfigValue<Integer> VanillaRecipeCraftingTimeTicks;
	public final ModConfigSpec.ConfigValue<Double> RepairAnythingRecipeRepairPercentage;
	public final ModConfigSpec.ConfigValue<Boolean> ChargedCreepersDropSpectrumMobHeads;
	
	public final ModConfigSpec.ConfigValue<Double> FadingSpreadChanceOnRandomTick;
	public final ModConfigSpec.ConfigValue<Double> FailingDecayTickRate;
	public final ModConfigSpec.ConfigValue<Double> RuinDecayTickRate;
	public final ModConfigSpec.ConfigValue<Double> ForfeitureDecayTickRate;
	
	public final ModConfigSpec.ConfigValue<Boolean> CanBottleUpFading;
	public final ModConfigSpec.ConfigValue<Boolean> CanBottleUpFailing;
	public final ModConfigSpec.ConfigValue<Boolean> CanBottleUpRuin;
	public final ModConfigSpec.ConfigValue<Boolean> CanBottleUpForfeiture;
	
	public final ModConfigSpec.ConfigValue<Boolean> FadingCanDestroyBlockEntities;
	public final ModConfigSpec.ConfigValue<Boolean> FailingCanDestroyBlockEntities;
	public final ModConfigSpec.ConfigValue<Boolean> RuinCanDestroyBlockEntities;
	public final ModConfigSpec.ConfigValue<Boolean> ForfeitureCanDestroyBlockEntities;
	
	public final ModConfigSpec.ConfigValue<Boolean> LogPlacingOfDecay;
	public final ModConfigSpec.ConfigValue<Boolean> AllowDynamicEndPortalShape;
	public final ModConfigSpec.ConfigValue<Double> BlockSoundVolume;
	public final ModConfigSpec.ConfigValue<Double> OreAuraSoundVolume;
	public final ModConfigSpec.ConfigValue<String> NameForUnrevealedEnchantments;
	public final ModConfigSpec.ConfigValue<Double> ImprovedCriticalExtraDamageMultiplierPerLevel;
	public final ModConfigSpec.ConfigValue<Double> FirstStrikeDamagePerLevel;
	public final ModConfigSpec.ConfigValue<Double> DisarmingChancePerLevelMobs;
	public final ModConfigSpec.ConfigValue<Double> DisarmingChancePerLevelPlayers;
	public final ModConfigSpec.ConfigValue<Double> PestControlExperienceMultiplier;
	
	public final ModConfigSpec.ConfigValue<Integer> GlowVisionGogglesDuration;
	public final ModConfigSpec.ConfigValue<Boolean> OmniAcceleratorPvP;
	
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorHelmetProtection;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorChestplateProtection;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorLeggingsProtection;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorBootsProtection;
	public final ModConfigSpec.ConfigValue<Double> GemstoneArmorToughness;
	public final ModConfigSpec.ConfigValue<Double> GemstoneArmorKnockbackResistance;
	
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorWeaknessAmplifier;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorSlownessAmplifier;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorAbsorptionAmplifier;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorResistanceAmplifier;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorRegenerationAmplifier;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneArmorSpeedAmplifier;
	
	public final ModConfigSpec.ConfigValue<Integer> BedrockArmorHelmetProtection;
	public final ModConfigSpec.ConfigValue<Integer> BedrockArmorLeggingsProtection;
	public final ModConfigSpec.ConfigValue<Integer> BedrockArmorChestplateProtection;
	public final ModConfigSpec.ConfigValue<Integer> BedrockArmorBootsProtection;
	public final ModConfigSpec.ConfigValue<Double> BedrockArmorToughness;
	public final ModConfigSpec.ConfigValue<Double> BedrockArmorKnockbackResistance;
	
	public final ModConfigSpec.ConfigValue<Integer> GemstoneDurability;
	public final ModConfigSpec.ConfigValue<Double> GemstoneMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> GemstoneAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> GemstoneEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> OblivionDurability;
	public final ModConfigSpec.ConfigValue<Double> OblivionMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> OblivionAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> OblivionEnchantability;
	
	public final ModConfigSpec.ConfigValue<Double> BedrockMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> BedrockAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> BedrockEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> DraconicDurability;
	public final ModConfigSpec.ConfigValue<Double> DraconicMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> DraconicAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> DraconicEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> MalachiteDurability;
	public final ModConfigSpec.ConfigValue<Double> MalachiteMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> MalachiteAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> MalachiteEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> GlassCrestDurability;
	public final ModConfigSpec.ConfigValue<Double> GlassCrestMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> GlassCrestAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> GlassCrestEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> KnottedSwordDurability;
	public final ModConfigSpec.ConfigValue<Double> KnottedSwordMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> KnottedSwordAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> KnottedSwordEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> NectarLanceDurability;
	public final ModConfigSpec.ConfigValue<Double> NectarLanceMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> NectarLanceAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> NectarLanceEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> DreamflayerDurability;
	public final ModConfigSpec.ConfigValue<Double> DreamflayerMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> DreamflayerAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> DreamflayerEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> NightfallsBladeDurability;
	public final ModConfigSpec.ConfigValue<Double> NightfallsBladeMiningSpeed;
	public final ModConfigSpec.ConfigValue<Double> NightfallsBladeAttackDamage;
	public final ModConfigSpec.ConfigValue<Integer> NightfallsBladeEnchantability;
	
	public final ModConfigSpec.ConfigValue<Integer> MaxLevelForEffectsInLesserPotionPendant;
	public final ModConfigSpec.ConfigValue<Integer> MaxLevelForEffectsInGreaterPotionPendant;
	public final ModConfigSpec.ConfigValue<Boolean> DecayIsStoppedBySupportedClaimMods;
	public final ModConfigSpec.ConfigValue<Boolean> REIListsUnrevealedRecipesAsNotUnlocked;
	
	public final ModConfigSpec.ConfigValue<Double> DragonrotDripstoneDripChance;
	public final ModConfigSpec.ConfigValue<Double> LiquidCrystalDripstoneDripChance;
	public final ModConfigSpec.ConfigValue<Double> MidnightSolutionDripstoneDripChance;
	
	public final ModConfigSpec.ConfigValue<DimensionPortalSetting> DimensionPortals;
	
	public final ModConfigSpec.ConfigValue<Integer> ConstructorsStaffInkCostPerBlock;
	public final ModConfigSpec.ConfigValue<Integer> ExchangingStaffInkCostPerBlock;
	
	public enum DimensionPortalSetting {
		CREATE_PORTALS,
		PROTECT_LOWEST_OVERWORLD_LEVEL,
		NO_PORTALS
	}
	
	private SpectrumConfig(ModConfigSpec.Builder builder) {
		ConstructorsStaffInkCostPerBlock = builder
				.translation("config.spectrum.constructors_staff_ink_cost_per_block")
				.comment("Required Ink for one Block placed by the Constructor's Staff")
				.define("constructors_staff_ink_cost_per_block", 1);
		
		ExchangingStaffInkCostPerBlock = builder
				.translation("config.spectrum.exchanging_staff_ink_cost_per_block")
				.comment("Required Ink for one Block placed by the Exchanging Staff")
				.define("exchanging_staff_ink_cost_per_block", 5);
		
		DimensionPortals = builder
				.translation("config.spectrum.dimension_portals")
				.comment("Allow the creation of portals using Ruin")
				.defineEnum("dimension_portals", DimensionPortalSetting.CREATE_PORTALS);
		
		ToastTimeMilliseconds = builder
				.translation("config.spectrum.toast_milliseconds")
				.comment("The duration in milliseconds ingame recipe/unlock popups stay on the screen")
				.define("toast_milliseconds", 7500);
		
		DimensionReverbDecayTime = builder
				.translation("config.spectrum.dimension_reverb_decay_time")
				.comment("The reverb decay time for sound effects in Spectrum's dimension")
				.define("dimension_reverb_decay_time", 8.0D);
		
		DimensionReverbDensity = builder
				.translation("config.spectrum.dimension_reverb_density")
				.comment("The reverb density for sound effects in Spectrum's dimension")
				.define("dimension_reverb_density", 0.5D);
		
		WindSim = builder
				.translation("config.spectrum.wind_sim")
				.define("wind_sim", false);
		
		WindSimInterval = builder
				.translation("config.spectrum.wind_sim_interval")
				.comment("Affects how often the wind simulation updates - A lower number makes the simulation smoother, but increases the performance impact significantly")
				.define("wind_sim_interval", 3);
		
		ReducedParticles = builder
				.translation("config.spectrum.reduced_wind_particles")
				.define("reduced_wind_particles", false);
		
		PostProcessShaders = builder
				.translation("config.spectrum.post_process_shaders")
				.comment("Post process shaders")
				.define("post_process_shaders", true);
		
		AlwaysSpawnLightBlockParticles = builder
				.translation("config.spectrum.always_spawn_light_block_particles")
				.comment("Light Blocks emit particles without holding a Radiance Staff")
				.define("always_spawn_light_block_particles", false);
		
		PastelNetworkParticles = builder
				.translation("config.spectrum.pastel_network_particles")
				.comment("Items transported by a Pastel Network spawn particles")
				.define("pastel_network_particles", true);
		
		DimensionBrightnessMod = builder
				.translation("config.spectrum.dimension_brightness_mod")
				.comment("Adjusts the general brightness of the dimension (0 = default; 1 = darkening disabled")
				.define("dimension_brightness_mod", 0.0D);
		
		IntegrationPacksToSkipLoading = builder
				.translation("config.spectrum.integration_packs_to_skip_loading")
				.comment("Mod Integration Packs to not load (in case of mod compat errors). Put the mod id of the mod with integration pack conflict here")
				.defineList("integration_packs_to_skip_loading", List.of(), () -> "mymod", o -> true);
		
		AddItemTooltips = builder
				.translation("config.spectrum.add_item_tooltips")
				.comment("Add some advanced tooltips to items, like if Sculk Shriekers are able to summon the Warden")
				.define("add_item_tooltips", true);
		
		BedrockAnvilCanExceedMaxVanillaEnchantmentLevel = builder
				.translation("config.spectrum.bedrock_anvil_can_exceed_max_vanilla_enchantment_level")
				.comment("""
			The vanilla anvil caps enchantment levels at the max level for the enchantment
			So enchanted books that exceed the enchantments natural max level get capped
			If true the bedrock anvil will not cap the enchantments level to it's natural max level""")
				.define("bedrock_anvil_can_exceed_max_vanilla_enchantment_level", false);
		
		EndermanHoldingTreasureChance = builder
				.translation("config.spectrum.enderman_holding_treasure_chance")
				.comment("""
			The chance that an Enderman is holding a special treasure block on spawn
			Separate value for Endermen spawning in the end, since there are LOTS of them there
			Those blocks do not gate progression, so it is not that drastic not finding any right away.
			Better to let players stumble about them organically instead of forcing it.""")
				.define("enderman_holding_treasure_chance", 0.1D);
		
		EndermanHoldingTreasureInEndChance = builder
				.translation("config.spectrum.enderman_holding_treasure_chance_in_end")
				.define("enderman_holding_treasure_chance_in_end", 0.0075D);
		
		ShootingStarDimensions = builder
				.translation("config.spectrum.shooting_star_dimensions")
				.comment("Dimensions where shooting stars spawn for players. Shooting Stars will only spawn for players with sufficient progress in the mod")
				.defineList("shooting_star_dimensions", List.of("minecraft:overworld", "starry_skies:overworld", "paradise_lost:paradise_lost", "tropicraft:tropics"), () -> "mymod:my_[regex_matched_]*_dimension_id", o -> true);
		
		ShootingStarSpawnChance = builder
				.translation("config.spectrum.shooting_star_spawn_chance")
				.comment("""
			Shooting star spawns are checked every night between time 13000 and 22000, every 100 ticks (so 100 chances per night).
			By default, there is a 0.0075 ^= 0.75 % chance at each of those check times. Making it ~1 shooting star spawn
			per night per player that unlocked the required progression.""")
				.define("shooting_star_spawn_chance", 0.0075D);
		
		StormStoneDimensions = builder
				.translation("config.spectrum.storm_stone_dimensions")
				.comment("Dimensions where lightning strikes can spawn Storm Stones")
				.defineList("storm_stone_dimensions", List.of("minecraft:overworld", "starry_skies:overworld", "paradise_lost:paradise_lost", "tropicraft:tropics"), () -> "mymod:my_[regex_matched_]*_dimension_id", o -> true);
		
		StormStoneSpawnChance = builder
				.translation("config.spectrum.storm_stone_spawn_chance")
				.comment("Chance for a lightning strike to spawn a Storm Stone")
				.define("storm_stone_spawn_chance", 0.4D);
		
		VanillaRecipeCraftingTimeTicks = builder
				.translation("config.spectrum.vanilla_recipe_crafting_time_ticks")
				.comment("""
			The time in ticks it takes a Pigment Pedestal to craft a vanilla Crafting Table recipe without upgrades
			Setting this to <=0 will make the Pedestal not able to be used for crafting Crafting Table recipes.
			""")
				.define("vanilla_recipe_crafting_time_ticks", 40);
		
		RepairAnythingRecipeRepairPercentage = builder
				.translation("config.spectrum.repair_anything_recipe_repair_percentage")
				.comment("How much an item gets repaired when crafting it with Moonstruck Nectar")
				.define("repair_anything_recipe_repair_percentage", 0.33D);
		
		ChargedCreepersDropSpectrumMobHeads = builder
				.translation("config.spectrum.charged_creepers_drop_spectrum_mob_heads")
				.comment("Charged Creepers will drop Spectrum exclusive mob head when killing creatures")
				.define("charged_creepers_drop_spectrum_mob_heads", false);
		
		FadingSpreadChanceOnRandomTick = builder
				.translation("config.spectrum.fading_spread_chance_on_random_tick")
				.comment("""
			How fast decay will be spreading on random tick
			can be used to slow down propagation speed of decay in the worlds
			decay does use very few resources, but if your fear of someone letting decay
			spread free or using higher random tick rates than vanilla you can limit the spreading rate here
			
			Fading and Failing do no real harm to the world. If you turn up these values too high players
			may lack the feedback they need that what they are doing is correct
			
			1.0: Every random tick (default)
			0.5: Every second random tick
			0.0: Never (forbidden - players would be unable to progress)""")
				.define("fading_spread_chance_on_random_tick", 1.0D, value -> value instanceof Double d && d > 0.0D && d <= 1.0D);
		
		FailingDecayTickRate = builder
				.translation("config.spectrum.failing_spread_chance_on_random_tick")
				.define("failing_spread_chance_on_random_tick", 1.0D, value -> value instanceof Double d && d > 0.0D && d <= 1.0D);
		
		RuinDecayTickRate = builder
				.translation("config.spectrum.ruin_spread_chance_on_random_tick")
				.define("ruin_spread_chance_on_random_tick", 1.0D, value -> value instanceof Double d && d > 0.0D && d <= 1.0D);
		
		ForfeitureDecayTickRate = builder
				.translation("config.spectrum.forfeiture_spread_chance_on_random_tick")
				.define("forfeiture_spread_chance_on_random_tick", 1.0D, value -> value instanceof Double d && d > 0.0D && d <= 1.0D);
		
		CanBottleUpFading = builder
				.translation("config.spectrum.can_bottle_up_fading")
				.comment("Whether bottles can be used to pick up decay. Default is true.")
				.define("can_bottle_up_fading", true);
		
		CanBottleUpFailing = builder
				.translation("config.spectrum.can_bottle_up_failing")
				.define("can_bottle_up_failing", true);
		
		CanBottleUpRuin = builder
				.translation("config.spectrum.can_bottle_up_ruin")
				.define("can_bottle_up_ruin", true);
		
		CanBottleUpForfeiture = builder
				.translation("config.spectrum.can_bottle_up_forfeiture")
				.define("can_bottle_up_forfeiture", true);
		
		FadingCanDestroyBlockEntities = builder
				.translation("config.spectrum.fading_can_destroy_block_entities")
				.comment("Whether decay can take over block entities. Defaults to false.")
				.define("fading_can_destroy_block_entities", false);
		
		FailingCanDestroyBlockEntities = builder
				.translation("config.spectrum.failing_can_destroy_block_entities")
				.define("failing_can_destroy_block_entities", false);
		
		RuinCanDestroyBlockEntities = builder
				.translation("config.spectrum.ruin_can_destroy_block_entities")
				.define("ruin_can_destroy_block_entities", false);
		
		ForfeitureCanDestroyBlockEntities = builder
				.translation("config.spectrum.forfeiture_can_destroy_block_entities")
				.define("forfeiture_can_destroy_block_entities", true);
		
		LogPlacingOfDecay = builder
				.translation("config.spectrum.log_placing_of_decay")
				.comment("When a player places decay, add an entry to the server log")
				.define("log_placing_of_decay", true);
		
		AllowDynamicEndPortalShape = builder
				.translation("config.spectrum.allow_dynamic_end_portal_shape")
				.comment("Allow End Portals to be aligned in any shape, not only 3x3")
				.define("allow_dynamic_end_portal_shape", true);
		
		BlockSoundVolume = builder
				.translation("config.spectrum.block_sound_volume")
				.comment("The audio volume for Spectrums crafting blocks. Set to 0.0 to turn those sounds off completely.")
				.define("block_sound_volume", 0.5D);
		
		OreAuraSoundVolume = builder
				.translation("config.spectrum.ore_aura_sound_volume")
				.comment("The volume for audio that plays when close to certain ores")
				.define("ore_aura_sound_volume", 0.5D);
		
		NameForUnrevealedEnchantments = builder
				.translation("config.spectrum.name_for_unrevealed_enchantments")
				.comment("When empty, enchantments that the player has not unlocked show up with a scattered name. You can use a different name here")
				.define("name_for_unrevealed_enchantments", "");
		
		ImprovedCriticalExtraDamageMultiplierPerLevel = builder
				.translation("config.spectrum.improved_critical_extra_damage_multiplier_per_level")
				.comment("In vanilla, crits are a flat 50 % damage bonus. Improved Critical increases this damage by additional 50 % per level by default")
				.define("improved_critical_extra_damage_multiplier_per_level", 0.5D);
		
		FirstStrikeDamagePerLevel = builder
				.translation("config.spectrum.first_strike_damage_per_level")
				.comment("Flat additional damage dealt with each level of the First Strike enchantment")
				.define("first_strike_damage_per_level", 2.0D);
		
		DisarmingChancePerLevelMobs = builder
				.translation("config.spectrum.disarming_chance_per_level_mobs")
				.comment("The percentile a mobs armor/hand stacks are being dropped when hit with a Disarming enchanted weapon per the enchantments level")
				.define("disarming_chance_per_level_mobs", 0.01D);
		
		DisarmingChancePerLevelPlayers = builder
				.translation("config.spectrum.disarming_chance_per_level_players")
				.comment("If > 0 the Disarming Enchantment is able to remove armor and hand tools from a hit player. Should be a far smaller chance than for mobs")
				.define("disarming_chance_per_level_players", 0.0025D);
		
		PestControlExperienceMultiplier = builder
				.translation("config.spectrum.pest_control_experience_multiplier")
				.comment("Experience dropped my mined infested blocks is multiplied by this value")
				.define("pest_control_experience_multiplier", 2.0D);
		
		GlowVisionGogglesDuration = builder
				.translation("config.spectrum.glow_vision_goggles_duration")
				.comment("The duration a glow ink sac gives night vision when wearing a glow vision helmet in seconds")
				.define("glow_vision_goggles_duration", 240);
		
		OmniAcceleratorPvP = builder
				.translation("config.spectrum.omni_accelerator_pvp")
				.comment("If the Omni Accelerator should be able to have interactions in PvP that can drain the targets XP, modify their equipment, ... (configured via the requires_omni_accelerator_pvp_enabled item tag)")
				.define("omni_accelerator_pvp", false);
		
		GemstoneArmorHelmetProtection = builder
				.translation("config.spectrum.gemstone_armor_helmet_protection")
				.define("gemstone_armor_helmet_protection", 3);
		
		GemstoneArmorChestplateProtection = builder
				.translation("config.spectrum.gemstone_armor_chestplate_protection")
				.define("gemstone_armor_chestplate_protection", 7);
		
		GemstoneArmorLeggingsProtection = builder
				.translation("config.spectrum.gemstone_armor_leggings_protection")
				.define("gemstone_armor_leggings_protection", 5);
		
		GemstoneArmorBootsProtection = builder
				.translation("config.spectrum.gemstone_armor_boots_protection")
				.define("gemstone_armor_boots_protection", 4);
		
		GemstoneArmorToughness = builder
				.translation("config.spectrum.gemstone_armor_toughness")
				.define("gemstone_armor_toughness", 0.0D);
		
		GemstoneArmorKnockbackResistance = builder
				.translation("config.spectrum.gemstone_armor_knockback_resistance")
				.define("gemstone_armor_knockback_resistance", 0.0D);
		
		GemstoneArmorWeaknessAmplifier = builder
				.translation("config.spectrum.gemstone_armor_weakness_amplifier")
				.define("gemstone_armor_weakness_amplifier", 1);
		
		GemstoneArmorSlownessAmplifier = builder
				.translation("config.spectrum.gemstone_armor_slowness_amplifier")
				.define("gemstone_armor_slowness_amplifier", 1);
		
		GemstoneArmorAbsorptionAmplifier = builder
				.translation("config.spectrum.gemstone_armor_absorption_amplifier")
				.define("gemstone_armor_absorption_amplifier", 0);
		
		GemstoneArmorResistanceAmplifier = builder
				.translation("config.spectrum.gemstone_armor_resistance_amplifier")
				.define("gemstone_armor_resistance_amplifier", 0);
		
		GemstoneArmorRegenerationAmplifier = builder
				.translation("config.spectrum.gemstone_armor_regeneration_amplifier")
				.define("gemstone_armor_regeneration_amplifier", 0);
		
		GemstoneArmorSpeedAmplifier = builder
				.translation("config.spectrum.gemstone_armor_speed_amplifier")
				.define("gemstone_armor_speed_amplifier", 1);
		
		BedrockArmorHelmetProtection = builder
				.translation("config.spectrum.bedrock_armor_helmet_protection")
				.define("bedrock_armor_helmet_protection", 5);
		
		BedrockArmorLeggingsProtection = builder
				.translation("config.spectrum.bedrock_armor_leggings_protection")
				.define("bedrock_armor_leggings_protection", 9);
		
		BedrockArmorChestplateProtection = builder
				.translation("config.spectrum.bedrock_armor_chestplate_protection")
				.define("bedrock_armor_chestplate_protection", 12);
		
		BedrockArmorBootsProtection = builder
				.translation("config.spectrum.bedrock_armor_boots_protection")
				.define("bedrock_armor_boots_protection", 5);
		
		BedrockArmorToughness = builder
				.translation("config.spectrum.bedrock_armor_toughness")
				.define("bedrock_armor_toughness", 3.0D);
		
		BedrockArmorKnockbackResistance = builder
				.translation("config.spectrum.bedrock_armor_knockback_resistance")
				.define("bedrock_armor_knockback_resistance", 0.3D);
		
		GemstoneDurability = builder
				.translation("config.spectrum.gemstone_durability")
				.comment("Tool Material Stats")
				.define("gemstone_durability", 16);
		
		GemstoneMiningSpeed = builder
				.translation("config.spectrum.gemstone_mining_speed")
				.define("gemstone_mining_speed", 4.0D);
		
		GemstoneAttackDamage = builder
				.translation("config.spectrum.gemstone_attack_damage")
				.define("gemstone_attack_damage", 2.0D);
		
		GemstoneEnchantability = builder
				.translation("config.spectrum.gemstone_enchantability")
				.define("gemstone_enchantability", 10);
		
		OblivionDurability = builder
				.translation("config.spectrum.oblivion_durability")
				.define("oblivion_durability", 1143);
		
		OblivionMiningSpeed = builder
				.translation("config.spectrum.oblivion_mining_speed")
				.define("oblivion_mining_speed", 20.0D);
		
		OblivionAttackDamage = builder
				.translation("config.spectrum.oblivion_attack_damage")
				.define("oblivion_attack_damage", 1.0D);
		
		OblivionEnchantability = builder
				.translation("config.spectrum.oblivion_enchantability")
				.define("oblivion_enchantability", 5);
		
		BedrockMiningSpeed = builder
				.translation("config.spectrum.bedrock_mining_speed")
				.define("bedrock_mining_speed", 15.0D);
		
		BedrockAttackDamage = builder
				.translation("config.spectrum.bedrock_attack_damage")
				.define("bedrock_attack_damage", 5.0D);
		
		BedrockEnchantability = builder
				.translation("config.spectrum.bedrock_enchantability")
				.define("bedrock_enchantability", 3);
		
		DraconicDurability = builder
				.translation("config.spectrum.draconic_durability")
				.define("draconic_durability", 10000);
		
		DraconicMiningSpeed = builder
				.translation("config.spectrum.draconic_mining_speed")
				.define("draconic_mining_speed", 11.5D);
		
		DraconicAttackDamage = builder
				.translation("config.spectrum.draconic_attack_damage")
				.define("draconic_attack_damage", 7.0D);
		
		DraconicEnchantability = builder
				.translation("config.spectrum.draconic_enchantability")
				.define("draconic_enchantability", 1);
		
		MalachiteDurability = builder
				.translation("config.spectrum.malachite_durability")
				.define("malachite_durability", 1536);
		
		MalachiteMiningSpeed = builder
				.translation("config.spectrum.malachite_mining_speed")
				.define("malachite_mining_speed", 14.0D);
		
		MalachiteAttackDamage = builder
				.translation("config.spectrum.malachite_attack_damage")
				.define("malachite_attack_damage", 5.0D);
		
		MalachiteEnchantability = builder
				.translation("config.spectrum.malachite_enchantability")
				.define("malachite_enchantability", 20);
		
		GlassCrestDurability = builder
				.translation("config.spectrum.glass_crest_durability")
				.define("glass_crest_durability", 6144);
		
		GlassCrestMiningSpeed = builder
				.translation("config.spectrum.glass_crest_mining_speed")
				.define("glass_crest_mining_speed", 18.0D);
		
		GlassCrestAttackDamage = builder
				.translation("config.spectrum.glass_crest_attack_damage")
				.define("glass_crest_attack_damage", 10.0D);
		
		GlassCrestEnchantability = builder
				.translation("config.spectrum.glass_crest_enchantability")
				.define("glass_crest_enchantability", 5);

		KnottedSwordDurability = builder
				.translation("config.spectrum.knotted_sword_durability")
				.define("knotted_sword_durability", 1536);
		
		KnottedSwordMiningSpeed = builder
				.translation("config.spectrum.knotted_sword_mining_speed")
				.define("knotted_sword_mining_speed", 7.0D);
		
		KnottedSwordAttackDamage = builder
				.translation("config.spectrum.knotted_sword_attack_damage")
				.define("knotted_sword_attack_damage", 2.5D);
		
		KnottedSwordEnchantability = builder
				.translation("config.spectrum.knotted_sword_enchantability")
				.define("knotted_sword_enchantability", 14);
		
		NectarLanceDurability = builder
				.translation("config.spectrum.nectar_lance_durability")
				.define("nectar_lance_durability", 6144);
		
		NectarLanceMiningSpeed = builder
				.translation("config.spectrum.nectar_lance_mining_speed")
				.define("nectar_lance_mining_speed", 9.5D);
		
		NectarLanceAttackDamage = builder
				.translation("config.spectrum.nectar_lance_attack_damage")
				.define("nectar_lance_attack_damage", 9.0D);
		
		NectarLanceEnchantability = builder
				.translation("config.spectrum.nectar_lance_enchantability")
				.define("nectar_lance_enchantability", 30);
		
		DreamflayerDurability = builder
				.translation("config.spectrum.dreamflayer_durability")
				.define("dreamflayer_durability", 650);
		
		DreamflayerMiningSpeed = builder
				.translation("config.spectrum.dreamflayer_mining_speed")
				.define("dreamflayer_mining_speed", 5.0D);
		
		DreamflayerAttackDamage = builder
				.translation("config.spectrum.dreamflayer_attack_damage")
				.define("dreamflayer_attack_damage", 2.0D);
		
		DreamflayerEnchantability = builder
				.translation("config.spectrum.dreamflayer_enchantability")
				.define("dreamflayer_enchantability", 20);
		
		NightfallsBladeDurability = builder
				.translation("config.spectrum.nightfalls_blade_durability")
				.define("nightfalls_blade_durability", 650);
		
		NightfallsBladeMiningSpeed = builder
				.translation("config.spectrum.nightfalls_blade_mining_speed")
				.define("nightfalls_blade_mining_speed", 2.0D);
		
		NightfallsBladeAttackDamage = builder
				.translation("config.spectrum.nightfalls_blade_attack_damage")
				.define("nightfalls_blade_attack_damage", 1.0D);
		
		NightfallsBladeEnchantability = builder
				.translation("config.spectrum.nightfalls_blade_enchantability")
				.define("nightfalls_blade_enchantability", 1);
		
		MaxLevelForEffectsInLesserPotionPendant = builder
				.translation("config.spectrum.max_level_for_effects_in_lesser_potion_pendant")
				.define("max_level_for_effects_in_lesser_potion_pendant", 3);
		
		MaxLevelForEffectsInGreaterPotionPendant = builder
				.translation("config.spectrum.max_level_for_effects_in_greater_potion_pendant")
				.define("max_level_for_effects_in_greater_potion_pendant", 1);
		
		DecayIsStoppedBySupportedClaimMods = builder
				.translation("config.spectrum.decay_is_stopped_by_supported_claim_mods")
				.comment("""
			True will prevent the spread of Decay blocks in claims.
			Only enable when necessary and communicate to your players that those blocks will not work in their claims.
			If any player comes to the Spectrum devs claiming that decay does not spread for them, and therefore they could not progress
			You will get put on the 'bad pack devs' list and this config setting removed again
			""")
				.define("decay_is_stopped_by_supported_claim_mods", false);
		
		REIListsUnrevealedRecipesAsNotUnlocked = builder
				.translation("config.spectrum.rei_lists_unrevealed_recipes_as_not_unlocked")
				.comment("""
			By Default, Roughly Enough Items will show a 'recipe not unlocked yet' screen for not yet unlocked recipes.
			Setting this value to false will instead not show this screen, showing no recipes whatsoever, until unlocked
			""")
				.define("rei_lists_unrevealed_recipes_as_not_unlocked", true);
		
		DragonrotDripstoneDripChance = builder
				.translation("config.spectrum.dragonrot_dripstone_drip_chance")
				.comment("The chance for Dripstone to fill a Cauldron with Dragonrot")
				.define("dragonrot_dripstone_drip_chance", 0.01D);
		LiquidCrystalDripstoneDripChance = builder
				.translation("config.spectrum.liquid_crystal_dripstone_drip_chance")
				.comment("The chance for Dripstone to fill a Cauldron with Liquid Crystal")
				.define("liquid_crystal_dripstone_drip_chance", 0.01D);
		MidnightSolutionDripstoneDripChance = builder
				.translation("config.spectrum.midnight_solution_dripstone_drip_chance")
				.comment("The chance for Dripstone to fill a Cauldron with Midnight Solution")
				.define("midnight_solution_dripstone_drip_chance", 0.01D);
	}
	
	public final boolean canPedestalCraftVanillaRecipes() {
		return VanillaRecipeCraftingTimeTicks.get() > 0;
	}
	
	public static boolean spawnsShootingStars(ResourceLocation dimensionId) {
		return isResourceLocationRegexMatch(SpectrumConfig.CONFIG.ShootingStarDimensions.get(), dimensionId);
	}
	
	public static boolean spawnsStormStones(ResourceLocation dimensionId) {
		return isResourceLocationRegexMatch(SpectrumConfig.CONFIG.StormStoneDimensions.get(), dimensionId);
	}
	
	public static boolean isResourceLocationRegexMatch(List<? extends String> matches, ResourceLocation target) {
		for (String match : matches) {
			if (target.toString().matches(match)) {
				return true;
			}
		}
		return false;
	}
	
}
