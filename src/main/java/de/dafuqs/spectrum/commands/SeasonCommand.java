package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public class SeasonCommand {

    public static void register(LiteralCommandNode<ServerCommandSource> root) {
        LiteralCommandNode<ServerCommandSource> seasons = CommandManager.literal("seasons").requires((source) -> source.hasPermissionLevel(2)).build();
        LiteralCommandNode<ServerCommandSource> query = CommandManager.literal("query").executes(SeasonCommand::printSeasonAndPeriod).build();
        LiteralCommandNode<ServerCommandSource> progress = CommandManager.literal("progress").build();
        LiteralCommandNode<ServerCommandSource> progressSeason = CommandManager.literal("season").executes(context -> getProgress(context, false)).build();
        LiteralCommandNode<ServerCommandSource> progressPeriod = CommandManager.literal("period").executes(context -> getProgress(context, true)).build();

        seasons.addChild(query);
        progress.addChild(progressSeason);
        progress.addChild(progressPeriod);
        seasons.addChild(progress);
        root.addChild(seasons);
    }

    private static int printSeasonAndPeriod(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());
        source.sendFeedback(() -> Text.translatable("commands.spectrum.seasons.query", effects.getCurrentPeriod().getName(), effects.getCurrentSeason().getName()), false);
        return 1;
    }

    private static int getProgress(CommandContext<ServerCommandSource> context, boolean period) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());

        if (!period) {
            var progress = "" + ((float) effects.getSeasonalTime() % DDWorldEffectsComponent.SEASON_DURATION) * 100;
            source.sendFeedback(() -> Text.translatable("commands.spectrum.seasons.progress.season", StringUtils.left(progress, 3)), false);
        }
        else {
            var progress = "" + ((float) effects.getSeasonalTime() % DDWorldEffectsComponent.SEASON_PERIOD_INTERVAL) * 100;
            source.sendFeedback(() -> Text.translatable("commands.spectrum.seasons.progress.period", StringUtils.left(progress, 3)), false);
        }

        return 1;
    }
}
