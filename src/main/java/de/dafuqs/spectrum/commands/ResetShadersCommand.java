package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import net.minecraft.server.command.*;

public class ResetShadersCommand {
	
	public static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> config = CommandManager.literal("resetShaders").executes((context) -> {
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
				execute();
			return 0;
		}).build();
		root.addChild(config);
	}
	
	private static void execute() {
		SpectrumShaders.clearDimensionShaders();
	}
}
