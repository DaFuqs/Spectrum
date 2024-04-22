package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.loader.api.*;
import net.minecraft.registry.*;
import net.minecraft.server.command.*;
import net.minecraft.text.*;

import java.io.*;


public class DumpRegistriesCommand {

	public static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> dumpRegistries = CommandManager.literal("dump_registries").executes((context) -> execute(context.getSource())).build();
		root.addChild(dumpRegistries);
	}

	private static int execute(ServerCommandSource source) {
		File directory = FabricLoader.getInstance().getGameDir().resolve("registry_dump").toFile();

		source.getRegistryManager().streamAllRegistries().forEach(entry -> {
			File file = new File(directory, entry.key().getValue().toString().replace(":", "-") + ".txt");
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				for (RegistryKey<?> e : entry.value().getKeys()) {
					writer.write(e.getValue().toString());
					writer.write(System.lineSeparator());
				}
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		source.sendMessage(Text.literal("Registries exported to directory 'registry_dump'"));

		return 0;
	}

}
