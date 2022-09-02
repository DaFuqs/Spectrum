package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.commands.PrintConfigCommand;
import de.dafuqs.spectrum.commands.SanityCommand;
import de.dafuqs.spectrum.commands.ShootingStarCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SpectrumCommands {
	
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ShootingStarCommand.register(dispatcher);
			SanityCommand.register(dispatcher);
			PrintConfigCommand.register(dispatcher);
		});
	}
}
