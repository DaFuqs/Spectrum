package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import net.fabricmc.loader.api.*;
import net.minecraft.commands.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;

import java.io.*;


public class DumpTagsCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> dumpRegistries = Commands.literal("dump_tags")
				.requires((source) -> source.hasPermission(2))
				.executes((context) -> execute(context.getSource()))
				.build();
		root.addChild(dumpRegistries);
	}
	
	private static int execute(CommandSourceStack source) {
		File baseDir = FabricLoader.getInstance().getGameDir().resolve("tag_dump").toFile();
		baseDir.mkdirs();
		
		source.registryAccess().registries().forEach(registry -> {
			
			registry.value().getTags().forEach(pair -> {
				ResourceLocation registryId = pair.getSecond().key().registry().location();
				ResourceLocation tagId = pair.getSecond().key().location();
				File tagFile = new File(baseDir, tagId.getNamespace() + "/" + registryId.getPath() + "/" + tagId.getPath() + ".txt");
				
				try {
					tagFile.getParentFile().mkdirs();
					tagFile.createNewFile();
					
					FileWriter writer = new FileWriter(tagFile);
					for (Holder<?> entry : pair.getSecond()) {
						writer.write(entry.unwrapKey().get().location().toString());
						writer.write(System.lineSeparator());
					}
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		});
		
		source.sendSystemMessage(Component.literal("Tags exported to directory 'tag_dump'"));
		
		return 0;
	}
	
}
