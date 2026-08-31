package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.*;
import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.neoforged.fml.loading.*;
import org.apache.commons.io.output.*;
import org.jspecify.annotations.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;


public class AdvancementSyncCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> dumpRegistries = Commands.literal("advancement_sync")
				.requires((source) -> source.hasPermission(0))
				.executes((context) -> execute(context.getSource()))
				.build();
		root.addChild(dumpRegistries);
	}
	
	private static int execute(CommandSourceStack source) {
		if(source.getPlayer() == null) {
			return -1;
		}
		
		MinecraftServer server = source.getServer();
		AdvancementSyncer advancementSyncer = AdvancementSyncer.getInstance(server);
		List<AdvancementSyncer.Relationship> relationships = advancementSyncer.getSyncs(source.getPlayer());
		
		if(relationships.isEmpty()) {
			source.sendSystemMessage(Component.literal("No relationships."));
		} else {
			source.sendSystemMessage(Component.literal("Relationships:"));
			for(AdvancementSyncer.Relationship relationship : relationships) {
				StringBuilder s = new StringBuilder();
				s.append("- ");
				s.append(relationship.option().getSerializedName());
				s.append(": ");
				List<String> playerEntries = new  ArrayList<>();
				for(UUID uuid : relationship.players()) {
					ServerPlayer p = server.getPlayerList().getPlayer(uuid);
					playerEntries.add(p == null ? uuid.toString() : p.getDisplayName().getString());
				}
				s.append(String.join(", ", playerEntries));
				source.sendSystemMessage(Component.literal(s.toString()));
			}
		}
		
		return relationships.size();
	}
	
}
