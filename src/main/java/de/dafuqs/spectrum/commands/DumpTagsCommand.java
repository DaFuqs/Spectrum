package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import net.fabricmc.loader.api.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.command.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.io.*;


public class DumpTagsCommand {

	public static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> dumpRegistries = CommandManager.literal("dump_tags")
				.requires((source) -> source.hasPermissionLevel(2))
				.executes((context) -> execute(context.getSource()))
				.build();
		root.addChild(dumpRegistries);
	}

	private static int execute(ServerCommandSource source) {
		File baseDir = FabricLoader.getInstance().getGameDir().resolve("tag_dump").toFile();
		baseDir.mkdirs();

		source.getRegistryManager().streamAllRegistries().forEach(registry -> {

			registry.value().streamTagsAndEntries().forEach(pair -> {
				Identifier registryId = pair.getSecond().getTag().registry().getValue();
				Identifier tagId = pair.getSecond().getTag().id();
				File tagFile = new File(baseDir, tagId.getNamespace() + "/" + registryId.getPath() + "/" + tagId.getPath() + ".txt");

				try {
					tagFile.getParentFile().mkdirs();
					tagFile.createNewFile();

					FileWriter writer = new FileWriter(tagFile);
					for (RegistryEntry<?> entry : pair.getSecond()) {
						writer.write(entry.getKey().get().getValue().toString());
						writer.write(System.lineSeparator());
					}
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		});

		source.sendMessage(Text.literal("Tags exported to directory 'tag_dump'"));

		return 0;
	}

}
