package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

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

        // pedestal recipes that use gemstone powder not available at that tier yet
        for(PedestalCraftingRecipe pedestalRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
            if(pedestalRecipe.getTier() == PedestalRecipeTier.BASIC || pedestalRecipe.getTier() == PedestalRecipeTier.SIMPLE) {
                if(pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.BLACK) > 0) {
                    SpectrumCommon.log(Level.WARN, "Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() +  "' is using onyx powder as input! Players will not have access to Onyx at that tier");
                }
                if(pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.WHITE) > 0) {
                    SpectrumCommon.log(Level.WARN, "Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() +  "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
                }
            } else if(pedestalRecipe.getTier() == PedestalRecipeTier.ADVANCED) {
                if(pedestalRecipe.getGemstoneDustInputs().get(GemstoneColor.WHITE) > 0) {
                    SpectrumCommon.log(Level.WARN, "Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() +  "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
                }
            }
        }

        // impossible to unlock pedestal recipes
        for(PedestalCraftingRecipe pedestalCraftingRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
            List<Identifier> advancementIdentifiers = pedestalCraftingRecipe.getRequiredAdvancementIdentifiers();
            if(advancementIdentifiers == null || advancementIdentifiers.isEmpty()) {
                SpectrumCommon.log(Level.WARN, "Pedestal recipe '" + pedestalCraftingRecipe.getId() + "' has no required advancements set!");
            } else {
                for (Identifier advancementIdentifier : advancementIdentifiers) {
                    if (!doesAdvancementExist(advancementIdentifier)) {
                        SpectrumCommon.log(Level.WARN, "Advancement '" + advancementIdentifier + "' in pedestal recipe '" + pedestalCraftingRecipe.getId() + "' does not exist");
                    }
                }
            }
        }

        // impossible to unlock fusion shrine recipes
        for(FusionShrineRecipe fusionShrineRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.FUSION_SHRINE)) {
            if(!doesAdvancementExist(fusionShrineRecipe.getRequiredAdvancementIdentifier())) {
                SpectrumCommon.log(Level.WARN, "Advancement '" + fusionShrineRecipe.getRequiredAdvancementIdentifier() + "' in fusion shrine recipe '" + fusionShrineRecipe.getId() + "' does not exist");
            }
        }

        // impossible to unlock block cloaks
        for(Map.Entry<Identifier, List<Cloakable>> cloaks : BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks().entrySet()) {
            if(!doesAdvancementExist(cloaks.getKey())) {
                SpectrumCommon.log(Level.WARN, "Advancement '" + cloaks.getKey().toString() + "' for block / item cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
            }
        }

        SpectrumCommon.log(Level.INFO, "##### SANITY CHECK FINISHED ######");
        return 0;
    }

    private static boolean doesAdvancementExist(Identifier identifier) {
        return SpectrumCommon.minecraftServer.getAdvancementLoader().get(identifier) != null;
    }

}
