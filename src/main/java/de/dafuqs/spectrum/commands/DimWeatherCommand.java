package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DimWeatherCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("spectrum_weather")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(literal("state")
                        .then(literal("set")
                                .then(argument("state", IdentifierArgumentType.identifier())
                                        .suggests(DimWeatherCommand::getSuggestions)
                                        .executes(DimWeatherCommand::setWeatherState)))
                        .then(literal("query")
                                .executes(DimWeatherCommand::printWeatherState))
                        .then(literal("debug")
                                .executes(DimWeatherCommand::printDebugInfo))
                        .then(literal("unlock")
                                .executes(DimWeatherCommand::clearWeatherState))
                )
                .then(literal("aquifer")
                        .then(literal("set")
                                .then(literal("empty")
                                        .executes(context -> setAquiferFill(context, 0)))
                                .then(literal("full")
                                        .executes(context -> setAquiferFill(context, DDWorldEffectsComponent.AQUIFER_CAP)))
                                .then(argument("fill", FloatArgumentType.floatArg())
                                        .executes(context -> setAquiferFill(context, FloatArgumentType.getFloat(context, "fill")))))
                        .then(literal("query")
                                .executes(DimWeatherCommand::printAquiferFill))
                        .then(literal("rates")
                                .then(literal("get")
                                        .executes(DimWeatherCommand::printAquiferRates))
                                .then(literal("formula")
                                        .executes(DimWeatherCommand::aquiferFillFormulaReminder)))
                )
        );
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

        var altStates = state.getAltStates();
        if (altStates.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.alts.empty"), false);
        }
        else {
            source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.alts.present"), false);
            for (Map.Entry<RegistryKey<Biome>, WeatherState> entry : altStates.entrySet()) {
                source.sendFeedback(() -> Text.translatable("commands.spectrum.weather.debug.alts.entry", entry.getValue().getName(), Util.createTranslationKey("biome", entry.getKey().getValue())), false);
            }
        }

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
