package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.commands.*;
import net.fabricmc.fabric.api.command.v2.*;

public class SpectrumCommands {
	
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ShootingStarCommand.register(dispatcher);
			SanityCommand.register(dispatcher);
			PrintConfigCommand.register(dispatcher);
		});
	}
}
