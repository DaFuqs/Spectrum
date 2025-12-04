package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.commands.*;
import net.neoforged.fml.util.thread.*;

public class ResetShadersCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> config = Commands.literal("resetShaders").executes((context) -> {
			if (EffectiveSide.get().isClient()) {
				execute();
			}
			return 0;
		}).build();
		root.addChild(config);
	}
	
	private static void execute() {
		SpectrumShaders.clearDimensionShaders();
	}
}
