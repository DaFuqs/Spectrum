package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.advancement.HasAdvancementCriterion;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import io.netty.util.internal.StringUtil;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ProgressionSanityCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_test_progression_sanity").requires((source) -> {
			return source.hasPermissionLevel(2);
		}).executes((context) -> {
			return execute(context.getSource());
		})));
	}

	private static int execute(ServerCommandSource source) {
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK START ######");

		// Build an empty hashmap of hashmaps for counting used gem colors for each tier
		// This info can be used to balance the usage times a bit
		HashMap<PedestalRecipeTier, HashMap<GemstoneColor, Integer>> usedColorsForEachTier = new HashMap<>();
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<GemstoneColor, Integer> colorMap = new HashMap<>();
			for(GemstoneColor gemstoneColor : GemstoneColor.values()) {
				colorMap.put(gemstoneColor, 0);
			}
			usedColorsForEachTier.put(pedestalRecipeTier, colorMap);
		}

		// pedestal recipes that use gemstone powder not available at that tier yet
		for(PedestalCraftingRecipe pedestalRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			/* There are some recipes that use advanced ingredients by design
			   despite being of a low tier, like black colored lamps.
			   While the player does not have access to that yet it is no problem at all
			   To exclude those recipes in these warnings there is a boolean flag in the recipe jsons
			 */
			if(!pedestalRecipe.isExcludeRequirementsInDebugCommand()) {
				if (pedestalRecipe.getTier() == PedestalRecipeTier.BASIC || pedestalRecipe.getTier() == PedestalRecipeTier.SIMPLE) {
					if (pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.BLACK) > 0) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using onyx powder as input! Players will not have access to Onyx at that tier");
					}
					if (pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.WHITE) > 0) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
					}
				} else if (pedestalRecipe.getTier() == PedestalRecipeTier.ADVANCED) {
					if (pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.WHITE) > 0) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
					}
				}
			}
			for(Map.Entry<GemstoneColor, Integer> gemstoneDustInput : pedestalRecipe.getGemstoneDustInputs().entrySet()) {
				usedColorsForEachTier.get(pedestalRecipe.getTier()).put(gemstoneDustInput.getKey(), usedColorsForEachTier.get(pedestalRecipe.getTier()).get(gemstoneDustInput.getKey()) + gemstoneDustInput.getValue());
			}
		}
		
		// impossible to unlock pedestal recipes
		for(PedestalCraftingRecipe pedestalCraftingRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			List<Identifier> advancementIdentifiers = pedestalCraftingRecipe.getRequiredAdvancementIdentifiers();
			if(advancementIdentifiers == null || advancementIdentifiers.isEmpty()) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Unlocks] Pedestal recipe '" + pedestalCraftingRecipe.getId() + "' has no required advancements set!");
			} else {
				for (Identifier advancementIdentifier : advancementIdentifiers) {
					if (!doesAdvancementExist(advancementIdentifier)) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Unlocks] Advancement '" + advancementIdentifier + "' in pedestal recipe '" + pedestalCraftingRecipe.getId() + "' does not exist");
					}
				}
			}
		}

		// impossible to unlock fusion shrine recipes
		for(FusionShrineRecipe fusionShrineRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.FUSION_SHRINE)) {
			if(!doesAdvancementExist(fusionShrineRecipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Fusion Shrine Recipe Unlocks] Advancement '" + fusionShrineRecipe.getRequiredAdvancementIdentifier() + "' in fusion shrine recipe '" + fusionShrineRecipe.getId() + "' does not exist");
			}
		}

		// impossible to unlock block cloaks
		for(Map.Entry<Identifier, List<Cloakable>> cloaks : BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks().entrySet()) {
			if(!doesAdvancementExist(cloaks.getKey())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Block Cloaks] Advancement '" + cloaks.getKey().toString() + "' for block / item cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
			}
		}

		// "has advancement" criteria with nonexistent advancements
		for(Advancement advancement : SpectrumCommon.minecraftServer.getAdvancementLoader().getAdvancements()) {
			for(AdvancementCriterion criterion : advancement.getCriteria().values()) {
				CriterionConditions conditions = criterion.getConditions();
				Identifier id = conditions.getId();
				if(id.equals(HasAdvancementCriterion.ID) && conditions instanceof  HasAdvancementCriterion.Conditions hasAdvancementConditions) {
					Identifier advancementIdentifier = hasAdvancementConditions.getAdvancementIdentifier();
					Advancement advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
					if(advancementCriterionAdvancement == null) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Has_Advancement Criteria] Advancement '" + advancement.getId() + "' references advancement '" + advancementIdentifier  + "' that does not exist");
					}
				}
			}
		}
		
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK PEDESTAL RECIPE STATISTICS ######");
		for(PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<GemstoneColor, Integer> entry = usedColorsForEachTier.get(pedestalRecipeTier);
			SpectrumCommon.log(Level.INFO, "[SANITY: Pedestal Recipe Gemstone Usages] Gemstone Powder for tier " + StringUtils.leftPad(pedestalRecipeTier.toString(), 8) +
					": C:" + StringUtils.leftPad(entry.get(GemstoneColor.CYAN).toString(), 3) +
					" M:" + StringUtils.leftPad(entry.get(GemstoneColor.MAGENTA).toString(), 3) +
					" Y:" + StringUtils.leftPad(entry.get(GemstoneColor.YELLOW).toString(), 3) +
					" K:" + StringUtils.leftPad(entry.get(GemstoneColor.BLACK).toString(), 3) +
					" W:" + StringUtils.leftPad(entry.get(GemstoneColor.WHITE).toString(), 3));
		}
		
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK FINISHED ######");
		
		if(source.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(new TranslatableText("commands.spectrum.progression_sanity.success"), false);
		}
		
		return 0;
	}

	private static boolean doesAdvancementExist(Identifier identifier) {
		return SpectrumCommon.minecraftServer.getAdvancementLoader().get(identifier) != null;
	}

}
