package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.status_effects.AscensionStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class SpectrumFoodComponents {
	
	public static final FoodComponent MOONSTRUCK_NECTAR = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2), 1.0F)
			.build();
	
	public static final FoodComponent JADE_JELLY = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.6F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2), 0.2F)
			.build();
	
	public static final FoodComponent STAR_CANDY = new FoodComponent.Builder()
			.hunger(3).saturationModifier(0.2F).snack()
			.build();
	
	public static final FoodComponent PURPLE_STAR_CANDY = new FoodComponent.Builder()
			.hunger(20).saturationModifier(1.2F).snack()
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
			.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100) ,1.0f)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 12000, 1) ,1.0f)
			.build();
	
	public static final FoodComponent GLISTERING_JELLY_TEA = new FoodComponent.Builder()
			.hunger(4).saturationModifier(0.6F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200), 1.0F)
			.build();
	
	public static final FoodComponent GLISTERING_JELLY_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 3), 1.0F)
			.build();
	
	public static final FoodComponent RESTORATION_TEA = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, 1200), 1.0F)
			.build();
	
	public static final FoodComponent RESTORATION_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, 1800), 1.0F)
			.build();
	
	public static final FoodComponent DEMON_TEA = new FoodComponent.Builder()
			.snack().hunger(2).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 800, 1), 2.0F / 3.0F)
			.build();
	
	public static final FoodComponent DEMON_TEA_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, 800, 0), 1.0F)
			.build();
	
	public static final FoodComponent INFUSED_BEVERAGE = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F)
			.build();
	
	public static final FoodComponent SUSPICIOUS_BREW = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F)
			.build();
	
	public static final FoodComponent JADE_WINE = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.build();
	
	public static final FoodComponent PURE_ALCOHOL = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.2F).alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 15, 4), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 30, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 60, 2), 1.0F)
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
			.hunger(6).saturationModifier(0.2F)
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200), 1.0F)
			.build();
	
	public static final FoodComponent HOT_CHOCOLATE_SCONE_BONUS = new FoodComponent.Builder()
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.NOURISHING, 1200), 1.0F)
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
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.DIVINITY, 600), 1.0F)
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
			.hunger(2).saturationModifier(0.2F)
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
			.statusEffect(new StatusEffectInstance(SpectrumStatusEffects.BLAZING, 20 * 30), 1.0F)
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
	
}
