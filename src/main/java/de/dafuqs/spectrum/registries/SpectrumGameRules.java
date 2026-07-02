package de.dafuqs.spectrum.registries;

import net.minecraft.world.level.*;

public class SpectrumGameRules {
	
	public static final GameRules.Key<GameRules.BooleanValue> RULE_EXTINGUISHPRIMORDIALFIRE = GameRules.register(
			"spectrum_extinguishPrimordialFire", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_DRAGONROT_SOURCE_CONVERSION = GameRules.register(
			"spectrum_dragonrotSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_LIQUID_CRYSTAL_SOURCE_CONVERSION = GameRules.register(
			"spectrum_liquidCrystalSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_MIDNIGHT_SOLUTION_SOURCE_CONVERSION = GameRules.register(
			"spectrum_midnightSolutionSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_SLUDGE_SOURCE_CONVERSION = GameRules.register(
			"spectrum_sludgeSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	
	public static void register() {
	
	}

}
