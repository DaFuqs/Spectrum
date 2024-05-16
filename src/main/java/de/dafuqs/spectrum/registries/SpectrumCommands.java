package de.dafuqs.spectrum.registries;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.commands.*;
import net.fabricmc.fabric.api.command.v2.*;
import net.minecraft.server.command.*;

public class SpectrumCommands {
	
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LiteralCommandNode<ServerCommandSource> spectrumNode = CommandManager.literal("spectrum").build();
			ShootingStarCommand.register(spectrumNode);
			SanityCommand.register(spectrumNode);
			PrintConfigCommand.register(spectrumNode);
			PrimordialFireCommand.register(spectrumNode);
			DumpRegistriesCommand.register(spectrumNode);
			DumpTagsCommand.register(spectrumNode);
			DimWeatherCommand.register(spectrumNode);
			SeasonCommand.register(spectrumNode);

			dispatcher.getRoot().addChild(spectrumNode);
		});
	}
}
