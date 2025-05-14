package de.dafuqs.spectrum.registries;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.commands.*;
import net.fabricmc.fabric.api.command.v2.*;
import net.minecraft.commands.*;

public class SpectrumCommands {
	
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LiteralCommandNode<CommandSourceStack> spectrumNode = Commands.literal("spectrum").build();
			ShootingStarCommand.register(spectrumNode);
			SanityCommand.register(spectrumNode);
			PrintConfigCommand.register(spectrumNode);
			PrimordialFireCommand.register(spectrumNode);
			DumpRegistriesCommand.register(spectrumNode);
			DumpTagsCommand.register(spectrumNode);
			ResetShadersCommand.register(spectrumNode);
			

			dispatcher.getRoot().addChild(spectrumNode);
		});
	}
}
