package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.dynamic.*;
import de.dafuqs.spectrum.recipe.pedestal.dynamic.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.*;
import de.dafuqs.spectrum.recipe.titration_barrel.dynamic.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;

public class SpectrumCustomRecipeSerializers {
	
	private static void register(RecipeSerializer recipeSerializer, String id) {
		Registry.register(Registries.RECIPE_SERIALIZER, SpectrumCommon.locate(id), recipeSerializer);
	}
	
	public static void registerRecipeSerializers() {
		// Crafting
		register(RepairAnythingRecipe.SERIALIZER, "repair_anything");
		register(ClearInkRecipe.SERIALIZER, "clear_ink");
		register(ClearEnderSpliceRecipe.SERIALIZER, "clear_ender_splice");
		register(ClearPotionFillableRecipe.SERIALIZER, "clear_potion_fillable");
		register(ClearCraftingTabletRecipe.SERIALIZER, "clear_crafting_tablet");
		register(ColorBondingRibbonRecipe.SERIALIZER, "color_bonding_ribbon");
		register(WrapPresentRecipe.SERIALIZER, "wrap_present");
		
		// Pedestal
		register(StarCandyRecipe.SERIALIZER, "pedestal_star_candy");
		
		// Fusion Shrine
		register(ShootingStarHardeningRecipe.SERIALIZER, "shooting_star_hardening");
		
		// Spirit Instiller
		register(SpawnerCreatureChangeRecipe.SERIALIZER, "spirit_instiller_spawner_creature_change");
		register(SpawnerMaxNearbyEntitiesChangeRecipe.SERIALIZER, "spirit_instiller_spawner_max_nearby_entities_change");
		register(SpawnerRequiredPlayerRangeChangeRecipe.SERIALIZER, "spirit_instiller_spawner_spawner_player_range_change");
		register(SpawnerSpawnCountChangeRecipe.SERIALIZER, "spirit_instiller_spawner_spawn_count_change");
		register(SpawnerSpawnDelayChangeRecipe.SERIALIZER, "spirit_instiller_spawner_spawn_delay_change");
		
		register(HardcorePlayerRevivalRecipe.SERIALIZER, "spirit_instiller_hardcore_player_revival");
		
		// Titration Barrel
		register(JadeWineRecipe.SERIALIZER, "titration_barrel_jade_wine");
		register(SuspiciousBrewRecipe.SERIALIZER, "titration_barrel_suspicious_brew");
	}
	
}
