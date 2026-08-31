package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.commands.*;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.neoforged.neoforge.server.command.*;

import java.util.*;


public class AdvancementSyncCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> baseNode = Commands.literal("advancement_sync")
				.requires((source) -> source.hasPermission(0))
					.executes((context) -> list(context.getSource()))
				.requires((source) -> source.hasPermission(2))
					.then(Commands.argument("target", EntityArgument.player())
							.executes((context) -> list(context.getSource(), EntityArgument.getPlayer(context, "target"))))
				.requires((source) -> source.hasPermission(2))
					.then(Commands.literal("create")
							.then(Commands.argument("targets", EntityArgument.players())
									.then(Commands.argument("option", EnumArgument.enumArgument(AdvancementSyncer.Option.class))
											.executes((context) -> create(context.getSource(), EntityArgument.getPlayers(context, "targets"), context.getArgument("option", AdvancementSyncer.Option.class))))))
					.then(Commands.literal("clear")
							.then(Commands.argument("target", EntityArgument.player())
									.executes((context) -> clear(context.getSource(), EntityArgument.getPlayer(context, "target"))))).build();
		
		root.addChild(baseNode);
	}
	
	private static int list(CommandSourceStack source) {
		if (source.getPlayer() == null) {
			return -1;
		}
		return list(source, source.getPlayer());
	}
	
	private static int list(CommandSourceStack source, ServerPlayer player) {
		MinecraftServer server = source.getServer();
		AdvancementSyncer advancementSyncer = AdvancementSyncer.getInstance(server);
		List<AdvancementSyncer.Party> parties = advancementSyncer.getSyncs(player);
		
		if (parties.isEmpty()) {
			source.sendSystemMessage(Component.literal("No relationships."));
		} else {
			source.sendSystemMessage(Component.literal("Relationships:"));
			for (AdvancementSyncer.Party party : parties) {
				StringBuilder s = new StringBuilder();
				s.append("- ");
				s.append(party.option().getSerializedName());
				s.append(": ");
				List<String> playerEntries = new ArrayList<>();
				for (UUID uuid : party.players()) {
					ServerPlayer p = server.getPlayerList().getPlayer(uuid);
					playerEntries.add(p == null ? uuid.toString() : p.getDisplayName().getString());
				}
				s.append(String.join(", ", playerEntries));
				source.sendSystemMessage(Component.literal(s.toString()));
			}
		}
		
		return parties.size();
	}
	
	private static int create(CommandSourceStack source, Collection<ServerPlayer> players, AdvancementSyncer.Option option) {
		if(players.size() < 2) {
			source.sendSystemMessage(Component.literal("Advancement Syncing requires at least two players."));
			return 0;
		}
		
		AdvancementSyncer advancementSyncer = AdvancementSyncer.getInstance(source.getServer());
		advancementSyncer.sync(players, option);
		for(ServerPlayer p : players) {
			if(option.persistent) {
				if(option.spectrumOnly) {
					p.sendSystemMessage(Component.literal("You were added to an advancement party of " + players.size() + " players."));
				} else {
					p.sendSystemMessage(Component.literal("You were added to an advancement party of " + players.size() + " players (Spectrum only)."));
				}
			}
		}
		return players.size();
	}
	
	private static int clear(CommandSourceStack source, ServerPlayer player) {
		AdvancementSyncer advancementSyncer = AdvancementSyncer.getInstance(source.getServer());
		int removedCount = advancementSyncer.clear(player);
		player.sendSystemMessage(Component.literal("Your advancement parties were cleared: " + removedCount));
		return removedCount;
	}
	
}
