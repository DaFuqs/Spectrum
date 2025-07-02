package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import net.fabricmc.loader.api.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

import java.io.*;


public class DumpRegistriesCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> dumpRegistries = Commands.literal("dump_registries")
				.requires((source) -> source.hasPermission(2))
				.executes((context) -> execute(context.getSource()))
				.build();
		root.addChild(dumpRegistries);
	}
	
	private static int execute(CommandSourceStack source) {
		File directory = FabricLoader.getInstance().getGameDir().resolve("registry_dump").toFile();
		
		source.registryAccess().registries().forEach(registry -> {
			File file = new File(directory, registry.key().location().toString().replace(":", "/") + ".txt");
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				for (ResourceKey<?> e : registry.value().registryKeySet()) {
					writer.write(e.location().toString());
					writer.write(System.lineSeparator());
				}
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		
		source.sendSystemMessage(Component.literal("Registries exported to directory 'registry_dump'"));
		
		return 0;
	}
	
}
