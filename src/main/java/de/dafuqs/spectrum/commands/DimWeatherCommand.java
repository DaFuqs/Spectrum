package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DimWeatherCommand {

    public static void register(LiteralCommandNode<ServerCommandSource> root) {
        var weather = CommandManager.literal("weather").requires((source) -> source.hasPermissionLevel(2)).build();
        var state = CommandManager.literal("state").build();
        var stateSet = CommandManager.literal("set").build();

        var stateSetState = CommandManager.argument("state", IdentifierArgumentType.identifier())
                .suggests(DimWeatherCommand::getSuggestions)
                .executes(DimWeatherCommand::setWeatherState)
                .build();

        var stateQuery = CommandManager.literal("query").executes(DimWeatherCommand::printWeatherState).build();
        var stateDebug = CommandManager.literal("debug").executes(DimWeatherCommand::printDebugInfo).build();
        var stateUnlock = CommandManager.literal("unlock").executes(DimWeatherCommand::clearWeatherState).build();
        var aquifer = CommandManager.literal("aquifer").build();
        var aquiferSet = CommandManager.literal("set").build();
        var aquiferSetEmpty = CommandManager.literal("empty").executes(context -> setAquiferFill(context, 0)).build();
        var aquiferSetFull = CommandManager.literal("full").executes(context -> setAquiferFill(context, DDWorldEffectsComponent.AQUIFER_CAP)).build();

        var aquiferSetFill = CommandManager.argument("fill", FloatArgumentType.floatArg())
                .executes(context -> setAquiferFill(context, FloatArgumentType.getFloat(context, "fill")))
                .build();

        var aquiferQuery = CommandManager.literal("query").executes(DimWeatherCommand::printAquiferFill).build();
        var aquiferRates = CommandManager.literal("rates").build();
        var aquiferRatesGet = CommandManager.literal("get").executes(DimWeatherCommand::printAquiferRates).build();
        var aquiferRatesFormula = CommandManager.literal("formula").executes(DimWeatherCommand::aquiferFillFormulaReminder).build();

        aquiferRates.addChild(aquiferRatesFormula);
        aquiferRates.addChild(aquiferRatesGet);

        aquiferSet.addChild(aquiferSetFill);
        aquiferSet.addChild(aquiferSetFull);
        aquiferSet.addChild(aquiferSetEmpty);

        stateSet.addChild(stateSetState);

        aquifer.addChild(aquiferRates);
        aquifer.addChild(aquiferQuery);
        aquifer.addChild(aquiferSet);

        state.addChild(stateUnlock);
        state.addChild(stateDebug);
        state.addChild(stateQuery);
        state.addChild(stateSet);

        weather.addChild(aquifer);
        weather.addChild(state);

        root.addChild(weather);
    }

    public static CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        var reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        return CommandSource.suggestIdentifiers(SpectrumRegistries.WEATHER_STATES.getKeys().stream().map(RegistryKey::getValue), builder);
    }

    public static int setWeatherState(CommandContext<ServerCommandSource> context) {
        var source = (ServerCommandSource) context.getSource();
        var id = IdentifierArgumentType.getIdentifier(context, "state");
        var state = SpectrumRegistries.WEATHER_STATES.get(id);
        var effects = DDWorldEffectsComponent.of(source.getWorld());

        if (state == null) {
            source.sendError(Text.translatable("commands.spectrum.weather.set.fail", id.toString()));
            return 0;
        }

        effects.setForced(true);
        effects.setWeatherState(state);
        DDWorldEffectsComponent.sync((context.getSource()).getWorld());
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.set.success.state", state.getName()), true);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.set.success.lock"), true);
        return 1;
    }

    public static int printWeatherState(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());

        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.query.state", effects.getWeatherState().getName()), false);
        if (effects.isForced()){
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.query.forced"), false);
        }
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.query.ticks", effects.getWeatherTicks()), false);

        return 1;
    }

    public static int printDebugInfo(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());
        var state = effects.getWeatherState();
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.identifier", state.getName(), state.getId().toString()), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.translation", state.getOrCreateTranslationKey()), false);
        if (state.hasCeilingParticles()) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.ceilling"), false);
        }
        if (state.hasAirParticles()) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.air"), false);
        }
        if (state.hasGroundParticles()) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.ground"), false);
        }
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.windStrength", state.getWindIntensityModifier()), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.thirst", state.getThirst()), false);

        return 1;
    }

    public static int clearWeatherState(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());

        if (!effects.isForced()) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.clear.fail"), false);
            return 0;
        }

        var world = source.getWorld();
        var period = DDWorldEffectsComponent.of(world).getCurrentPeriod();
        var state = DDWorldEffectsComponent.of(world).getCurrentSeason().getStateFor(period);
        effects.setForced(false);
        effects.setWeatherState(state);
        DDWorldEffectsComponent.sync(context.getSource().getWorld());
        source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.clear.success"), true);
        return 1;
    }

    public static int setAquiferFill(CommandContext<ServerCommandSource> context, float fill) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());

        if (effects.verifyFillRequest(fill)) {
            effects.setAquiferFill(fill);
            DDWorldEffectsComponent.sync(source.getWorld());

            if (fill == 0) {
                source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.set.empty"), true);
            }
            else if (fill == DDWorldEffectsComponent.AQUIFER_CAP) {
                source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.set.full"), true);
            }
            else {
                source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.set.arbitrary", fill), true);
            }

            return 1;
        }
        else {
            source.sendError(Text.translatable("commands.spectrum.aquifer.fill.oob", 0F, DDWorldEffectsComponent.AQUIFER_CAP));
            return 0;
        }
    }

    public static int printAquiferFill(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var effects = DDWorldEffectsComponent.of(source.getWorld());
        var fill = effects.getAquiferFill();

        if (fill == 0) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.query.empty", fill), false);
        }
        else if (fill == DDWorldEffectsComponent.AQUIFER_CAP) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.query.full", fill), false);
        }
        else {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.fill.query.arbitrary", fill), false);
        }

        return 1;
    }

    public static int printAquiferRates(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var modifiers = DDWorldEffectsComponent.of(source.getWorld()).getFillRateModifiers();

        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.dayTime", modifiers.get(0)), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.surfaceRain", modifiers.get(1)), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.moonPhase", modifiers.get(2)), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.saturation", modifiers.get(3)), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.seasonal", modifiers.get(4)), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.rates.composited", modifiers.get(5)), false);

        return 1;
    }

    public static int aquiferFillFormulaReminder(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();

        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.formula.preamble"), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.formula.function"), false);
        source.sendFeedback(() -> Text.translatable("commands.spectrum.aquifer.formula.note", DDWorldEffectsComponent.BASE_AQUIFER_FLOW), false);

        return 1;
    }
}
