package de.dafuqs.spectrum.recipe.potion_workshop;

public class PotionMod {
	public int flatDurationBonusTicks = 0;
	public float flatPotencyBonus = 0.0F;
	
	public float multiplicativeDurationModifier = 1.0F;
	public float multiplicativePotencyModifier = 1.0F;
	
	public float flatPotencyBonusPositiveEffects = 0.0F;
	public float flatPotencyBonusNegativeEffects = 0.0F;
	
	public float additionalRandomPositiveEffectCount = 0;
	public float additionalRandomNegativeEffectCount = 0;
	
	public float chanceToAddLastEffect = 0.0F;
	public float lastEffectPotencyModifier = 1.0F;
	
	public float flatYieldBonus = 0;
	
	public boolean makeSplashing = false;
	public boolean makeLingering = false;
	
	public boolean noParticles = false;
	public boolean unidentifiable = false;
	public boolean makeEffectsPositive = false;
	public boolean potentDecreasingEffect = false;
	public boolean negateDecreasingDuration = false;
	public boolean fastDrinkable = false;
}