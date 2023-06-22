package de.dafuqs.spectrum.compat.patchouli.pages;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import net.fabricmc.loader.api.*;
import net.minecraft.util.*;

import java.util.*;

public interface GatedPatchouliPage {
	
	// unlocks that would be reported in the sanity check,
	// but are manually proven to be ok (since the entry advancement + page advancement match all required criteria already)
	// format: <page identifier> : <page unlock>
	Map<Identifier, Identifier> SANITY_WHITELIST = Map.of(
			SpectrumCommon.locate("resources/jade_vines"), SpectrumCommon.locate("midgame/build_spirit_instiller_structure"), // covered by entry adv + page adv
			SpectrumCommon.locate("cuisine/tarts"), SpectrumCommon.locate("craft_salted_jaramel_trifle_or_tart"), // secret recipe showing up once crafted
			SpectrumCommon.locate("cuisine/trifles"), SpectrumCommon.locate("craft_salted_jaramel_trifle_or_tart"), // secret recipe showing up once crafted
			SpectrumCommon.locate("resources/bloodstone"), SpectrumCommon.locate("unlocks/blocks/crystallarieum"), // covered by entry adv + page adv
			SpectrumCommon.locate("resources/malachite"), SpectrumCommon.locate("unlocks/blocks/crystallarieum"), // covered by entry adv + page adv
			SpectrumCommon.locate("creating_life/egg_laying_wooly_pig"), SpectrumCommon.locate("midgame/remember_egg_laying_wooly_pig") // recipe should only be revealed after remembering it
	);
	
	static void runSanityCheck(Identifier entryId, int pageNr, String pageAdvancement, GatedRecipe... recipes) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			if (pageAdvancement != null && !pageAdvancement.isEmpty()) {
				for (GatedRecipe recipe : recipes) {
					if (recipe == null) {
						SpectrumCommon.logWarning("Patchouli page " + entryId + " page " + pageNr + " is missing its recipe");
						continue;
					}
					Identifier recipeAdvId = recipe.getRequiredAdvancementIdentifier();
					Identifier combinedAdvId = recipeAdvId == null ? recipe.getRecipeTypeUnlockIdentifier() : recipeAdvId;
					if (combinedAdvId == null) {
						SpectrumCommon.logWarning("Patchouli page " + entryId + "[" + pageNr + "] references advancement " + pageAdvancement + " for a recipe that does not have an unlock: " + recipeAdvId);
						continue;
					}
					if (SANITY_WHITELIST.containsKey(entryId) && SANITY_WHITELIST.get(entryId).equals(new Identifier(pageAdvancement))) {
						continue;
					}
					if (!combinedAdvId.toString().equals(pageAdvancement)) {
						SpectrumCommon.logWarning("Patchouli page " + entryId + "[" + pageNr + "] references advancement " + pageAdvancement + " that differs from the one set in the recipe: " + recipeAdvId);
					}
				}
			}
		}
	}
	
}
