package de.dafuqs.spectrum.registries;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.commands.*;
import net.minecraft.commands.*;
import net.neoforged.neoforge.event.*;

public class SpectrumCommands {
	
	public static void register(RegisterCommandsEvent event) {
		LiteralCommandNode<CommandSourceStack> spectrumNode = Commands.literal("spectrum").build();
		ShootingStarCommand.register(spectrumNode);
		SanityCommand.register(spectrumNode);
		PrintConfigCommand.register(spectrumNode);
		PrimordialFireCommand.register(spectrumNode);
		DumpRegistriesCommand.register(spectrumNode);
		DumpTagsCommand.register(spectrumNode);
		ResetShadersCommand.register(spectrumNode);
		
		event.getDispatcher().getRoot().addChild(spectrumNode);
	}
}
