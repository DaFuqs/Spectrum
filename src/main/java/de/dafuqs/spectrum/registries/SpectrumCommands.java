package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.commands.ProgressionSanityCommand;
import de.dafuqs.spectrum.commands.ShootingStarCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class SpectrumCommands {
	
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ShootingStarCommand.register(dispatcher);
			ProgressionSanityCommand.register(dispatcher);
		});
	}
}
