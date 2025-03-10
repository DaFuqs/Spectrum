package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;

public class SpectrumFoodComponents {
	
	public static final FoodComponent MOONSTRUCK_NECTAR = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2), 1.0F)
			.build();
	
	public static final FoodComponent JADE_JELLY = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2), 0.2F)
			.build();
	
	public static final FoodComponent GLASS_PEACH = new FoodComponent.Builder()
			.hunger(3).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 300, 0), 0.05F)
			.build();

	public static final FoodComponent FISSURE_PLUM = new FoodComponent.Builder()
			.hunger(3).saturationModifier(1.5F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0), 0.1F)
			.build();

	public static final FoodComponent NIGHTDEW_SPROUT = new FoodComponent.Builder()
			.hunger(-1).saturationModifier(0).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 200, 0), 0.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 200, 0), 0.5F)
			.build();

	public static final FoodComponent NECTARDEW_BURGEON = new FoodComponent.Builder()
			.hunger(-1).saturationModifier(0).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.ETERNAL_SLUMBER, 6000, 0), 0.95F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FATAL_SLUMBER, 100, 0), 0.05F)
			.build();

	public static final FoodComponent STAR_CANDY = new FoodComponent.Builder()
			.hunger(3).saturationModifier(0.2F).snack()
			.build();
	
	public static final FoodComponent PURPLE_STAR_CANDY = new FoodComponent.Builder()
			.hunger(20).saturationModifier(1.2F).snack()
			.build();
	
	public static final FoodComponent ENCHANTED_GOLDEN_CARROT = new FoodComponent.Builder()
			.hunger(6).saturationModifier(1.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 0), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400, 0), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 1), 1.0F)
			.build();

	public static final FoodComponent JARAMEL = new FoodComponent.Builder()
			.hunger(1).saturationModifier(0.2F).snack().alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 300, 1), 1.0F)
			.build();
	
	public static final FoodComponent LUCKY_ROLL = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.ANOTHER_ROLL, 1200), 1.0F)
			.build();
	
	public static final FoodComponent TRIPLE_MEAT_POT_PIE = new FoodComponent.Builder()
			.hunger(20).saturationModifier(1.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 12000, 1), 1.0F)
			.build();
	
	public static final FoodComponent GLISTERING_JELLY_TEA = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.6F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200), 1.0F)
			.build();
	
	public static final FoodComponent GLISTERING_JELLY_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 3), 1.0F)
			.build();
	
	public static final FoodComponent RESTORATION_TEA = new FoodComponent.Builder()
			.hunger(2).saturationModifier(2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, 1200), 1.0F)
			.build();
	
	public static final FoodComponent RESTORATION_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, 1800), 1.0F)
			.build();
	
	public static final FoodComponent BODACIOUS_BERRY_BAR = new FoodComponent.Builder()
			.hunger(8).saturationModifier(1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 4), 1.0F)
			.build();
	
	public static final FoodComponent DEMON_TEA = new FoodComponent.Builder()
			.snack().hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 800, 1), 2.0F / 3.0F)
			.build();
	
	public static final FoodComponent DEMON_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 800, 0), 1.0F)
			.build();
	
	public static final FoodComponent BEVERAGE = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F)
			.alwaysEdible()
			.build();
	
	public static final FoodComponent PURE_ALCOHOL = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 15, 4), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 30, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 60, 2), 1.0F)
			.alwaysEdible()
			.build();

	public static final FoodComponent EVERNECTAR = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FATAL_SLUMBER, 20 * 10), 1.0F)
			.build();
	
	public static final FoodComponent KIMCHI = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.6F)
			.build();
	
	public static final FoodComponent CLOTTED_CREAM = new FoodComponent.Builder()
			.alwaysEdible()
			.build();
	
	public static final FoodComponent FRESH_CHOCOLATE = new FoodComponent.Builder()
			.snack().hunger(4).saturationModifier(0.6F)
			.build();

	public static final FoodComponent HOT_CHOCOLATE = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200), 1.0F)
			.build();
	
	public static final FoodComponent HOT_CHOCOLATE_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200), 1.0F)
			.build();

	public static final FoodComponent KARAK_CHAI = new FoodComponent.Builder()
			.hunger(5).saturationModifier(1F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 2400, 1), 1.0F)
			.build();

	public static final FoodComponent KARAK_CHAI_SCONE_BONUS = new FoodComponent.Builder()
			.hunger(5).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 6000, 2), 1.0F)
			.build();

	public static final FoodComponent AZALEA_TEA = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.1F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 1200), 1.0F)
			.build();

	public static final FoodComponent AZALEA_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 1200), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 1200), 1.0F)
			.build();
	
	public static final FoodComponent SCONE = new FoodComponent.Builder()
			.snack().hunger(3).saturationModifier(0.6F)
			.build();
	
	public static final FoodComponent FREIGEIST = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.ASCENSION, AscensionStatusEffect.MUSIC_INTRO_TICKS), 1.0F)
			.build();
	
	public static final FoodComponent INCANDESCENT_AMALGAM = new FoodComponent.Builder()
			.hunger(1).saturationModifier(0.2F).alwaysEdible()
			.build();
	
	public static final FoodComponent DIVINATION_HEART = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.DIVINITY, 600, DivinityStatusEffect.ASCENSION_AMPLIFIER), 1.0F)
			.build();

	public static final FoodComponent BITTER_OILS = new FoodComponent.Builder()
			.hunger(0).saturationModifier(0).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.DEADLY_POISON, 200), 1.0F)
			.build();
	
	public static final FoodComponent ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 15, 4), 1.0F)
			.build();

	public static final FoodComponent TOPAZ_ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 15), 1.0F)
			.build();

	public static final FoodComponent AMETHYST_ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20 * 15, 4), 1.0F)
			.build();

	public static final FoodComponent CITRINE_ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 15, 2), 1.0F)
			.build();

	public static final FoodComponent ONYX_ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 15, 1), 1.0F)
			.build();

	public static final FoodComponent MOONSTONE_ROCK_CANDY = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20 * 15), 1.0F)
			.build();
	
	public static final FoodComponent BLOODBOIL_SYRUP = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 400), 1.0F)
			.build();
	
	public static final FoodComponent HONEY_PASTRY = new FoodComponent.Builder()
			.hunger(6).saturationModifier(1.5F)
			.build();
	
	public static final FoodComponent JARAMEL_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.build();
	
	public static final FoodComponent SALTED_JARAMEL_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200), 1.0F)
			.build();
	
	public static final FoodComponent ASHEN_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.LAVA_GLIDING, 20 * 30), 1.0F)
			.build();
	
	public static final FoodComponent WEEPING_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 20 * 30), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 20 * 30), 1.0F)
			.build();
	
	public static final FoodComponent WHISPY_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.build();
	
	public static final FoodComponent PUFF_TART = new FoodComponent.Builder()
			.hunger(8).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.PROJECTILE_REBOUND, 20 * 30), 1.0F)
			.build();
	
	public static final FoodComponent JARAMEL_TRIFLE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.2F)
			.build();
	
	public static final FoodComponent SALTED_JARAMEL_TRIFLE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200), 1.0F)
			.build();
	
	public static final FoodComponent MONSTER_TRIFLE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200), 0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1200), 0.2F)
			.build();

	public static final FoodComponent DEMON_TRIFLE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 1200), 2.0F / 3.0F)
			.build();

	public static final FoodComponent MYCEYLON_APPLE_PIE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.3F)
			.build();

	public static final FoodComponent MYCEYLON_PUMPKIN_PIE = new FoodComponent.Builder()
			.hunger(10).saturationModifier(0.3F)
			.build();

	public static final FoodComponent MYCEYLON_COOKIE = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.1F)
			.build();

	public static final FoodComponent ALOE_LEAF = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.1F)
			.build();

	public static final FoodComponent PRICKLY_BAYLEAF = new FoodComponent.Builder()
			.hunger(1).saturationModifier(0.3F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 0, 0), 0.6F)
			.build();
	
	public static final FoodComponent TRIPLE_MEAT_POT_STEW = new FoodComponent.Builder()
			.hunger(20).saturationModifier(1.2F).meat()
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 12000, 1), 1.0F)
			.build();
	
	public static final FoodComponent DRAGONBONE_BROTH = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.6F).meat()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, 2400, 1), 1.0F)
			.build();
	
	public static final FoodComponent AQUA_REGIA = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.6F)
			.build();

	public static final FoodComponent BAGNUN = new FoodComponent.Builder()
			.hunger(10).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2000), 1.0F)
			.build();

	public static final FoodComponent BANYASH = new FoodComponent.Builder()
			.hunger(10).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 2000), 1.0F)
			.build();

	public static final FoodComponent BERLINER = new FoodComponent.Builder()
			.hunger(9).saturationModifier(1.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 2400), 1.0F)
			.build();

	public static final FoodComponent CHAUVE_SOURIS_AU_VIN = new FoodComponent.Builder()
			.hunger(20).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.EFFECT_PROLONGING, 6000, 0), 1.0F)
			.build();

	public static final FoodComponent CRAWFISH = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.6F)
			.build();

	public static final FoodComponent CRAWFISH_COCKTAIL = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.9F).snack()
			.build();

	public static final FoodComponent CREAM_PASTRY = new FoodComponent.Builder()
			.hunger(4).saturationModifier(1.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 3600, 1), 1.0F)
			.build();

	public static final FoodComponent FADED_KOI = new FoodComponent.Builder()
			.hunger(2).saturationModifier(10F)
			.build();

	public static final FoodComponent FISHCAKE = new FoodComponent.Builder()
			.hunger(8).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1000, 1), 1)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SWIFTNESS, 1000, 1), 1.0F)
			.build();

	public static final FoodComponent LIZARD_MEAT = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.6F).meat()
			.build();
	
	public static final FoodComponent COOKED_LIZARD_MEAT = new FoodComponent.Builder()
			.hunger(4).saturationModifier(1.6F).meat()
			.build();

	public static final FoodComponent GOLDEN_BRISTLE_TEA = new FoodComponent.Builder()
			.hunger(6).saturationModifier(1F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH), 0.5F)
			.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 3), 1)
			.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0), 0.5F)
			.build();

	public static final FoodComponent GOLDEN_BRISTLE_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.hunger(1).saturationModifier(0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH), 1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, 1200, 1), 1.0F)
			.build();

	public static final FoodComponent HARE_ROAST = new FoodComponent.Builder()
			.hunger(12).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200), 1)
			.build();

	public static final FoodComponent JUNKET = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.8F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 800, 2), 1)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SWIFTNESS, 800, 2), 1.0F)
			.build();

	public static final FoodComponent KOI = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.5F)
			.build();

	public static final FoodComponent MEATLOAF = new FoodComponent.Builder()
			.hunger(20).saturationModifier(2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, 6000, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 6000, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 6000, 0), 1.0F)
			.build();

	public static final FoodComponent MEATLOAF_SANDWICH = new FoodComponent.Builder()
			.hunger(9).saturationModifier(1.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.MAGIC_ANNULATION, 600, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 600, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0), 1.0F)
			.build();

	public static final FoodComponent MELLOW_SHALLOT_SOUP = new FoodComponent.Builder()
			.hunger(7).saturationModifier(0.8F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.EFFECT_PROLONGING, 600, 3), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, 600, 0), 1.0F)
			.build();

	public static final FoodComponent NECTERED_VIOGNIER = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.6F)
			.build();

	public static final FoodComponent PEACHES_FLAMBE = new FoodComponent.Builder()
			.hunger(14).saturationModifier(1.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 3000, 5), 1.0F)
			.build();

	public static final FoodComponent PEACH_CREAM = new FoodComponent.Builder()
			.hunger(8).saturationModifier(1F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 3000, 1), 1.0F)
			.build();

	public static final FoodComponent PEACH_CREAM_SCONE_BONUS = new FoodComponent.Builder()
			.hunger(0).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 6000, 3), 1.0F)
			.build();

	public static final FoodComponent PEACH_JAM = new FoodComponent.Builder()
			.hunger(16).saturationModifier(1F)
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 0), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 0), 1.0F)
			.build();

	public static final FoodComponent RABBIT_CREAM_PIE = new FoodComponent.Builder()
			.hunger(16).saturationModifier(0.8F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1), 1.0F)
			.build();

	public static final FoodComponent SEDATIVES = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.5F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.STIFFNESS, 300, 4), 1.0F)
			.build();

	public static final FoodComponent SLUSHSLIDE = new FoodComponent.Builder()
			.hunger(9).saturationModifier(0.6F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.SWIFTNESS, 2400, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 2400, 1), 1.0F)
			.build();

	public static final FoodComponent SURSTROMMING = new FoodComponent.Builder()
			.hunger(5).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 6000, 10), 1.0F)
			.build();

	public static final FoodComponent CHEONG = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.6F)
			.build();

	public static final FoodComponent MERMAIDS_JAM = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 400, 0), 1.0F)
			.build();

	public static final FoodComponent MERMAIDS_POPCORN = new FoodComponent.Builder()
			.hunger(6).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 0), 1.0F)
			.build();

	public static final FoodComponent LE_FISHE_AU_CHOCOLAT = new FoodComponent.Builder()
			.hunger(10).saturationModifier(1.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0), 0.5F)
			.build();
	
	public static final FoodComponent STUFFED_PETALS = new FoodComponent.Builder()
			.hunger(10).saturationModifier(1.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 0), 1.0F)
			.build();
	
	public static final FoodComponent PASTICHE = new FoodComponent.Builder()
			.hunger(16).saturationModifier(1.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 3300, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 3300, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1800, 0), 1.0F)
			.build();
	
	public static final FoodComponent VITTORIAS_ROAST = new FoodComponent.Builder()
			.hunger(16).saturationModifier(1.6F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 3600, 1), 1.0F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.TOUGHNESS, 1800, 1), 1.0F)
			.build();
	
}
