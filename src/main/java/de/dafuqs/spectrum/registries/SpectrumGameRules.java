package de.dafuqs.spectrum.registries;

import net.minecraft.world.level.*;

public class SpectrumGameRules {
	
	public static final GameRules.Key<GameRules.BooleanValue> RULE_EXTINGUISHPRIMORDIALFIRE = GameRules.register(
			"spectrum:extinguishPrimordialFire", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_DRAGONROT_SOURCE_CONVERSION = GameRules.register(
			"spectrum:dragonrotSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_LIQUID_CRYSTAL_SOURCE_CONVERSION = GameRules.register(
			"spectrum:liquidCrystalSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	public static final GameRules.Key<GameRules.BooleanValue> RULE_MIDNIGHT_SOLUTION_SOURCE_CONVERSION = GameRules.register(
			"spectrum:midnightSolutionSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false)
	);
	
	public static void register() {
	
	}

}
