package de.dafuqs.spectrum.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class SpectrumCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            ShootingStarCommand.register(dispatcher);
        });
    }
}
