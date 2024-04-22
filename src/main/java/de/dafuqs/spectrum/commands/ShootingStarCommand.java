package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.dafuqs.spectrum.entity.spawners.*;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;

import java.util.*;

public class ShootingStarCommand {

	public static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> spawnShootingStar = CommandManager.literal("spawn_shooting_star")
				.requires((source) -> source.hasPermissionLevel(2)).build();
		ArgumentCommandNode<ServerCommandSource, EntitySelector> targets = CommandManager.argument("targets", EntityArgumentType.players())
				.executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), 1)).build();
		ArgumentCommandNode<ServerCommandSource, Integer> targetsAmount = CommandManager.argument("amount", IntegerArgumentType.integer(1))
				.executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))).build();

		targets.addChild(targetsAmount);
		spawnShootingStar.addChild(targets);
		root.addChild(spawnShootingStar);
	}

	private static int execute(ServerCommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		for (ServerPlayerEntity entity : targets) {
			for (int i = 0; i < amount; i++) {
				ShootingStarSpawner.spawnShootingStar((ServerWorld) entity.getWorld(), entity);
			}
		}
		source.sendFeedback(() -> Text.translatable("commands.spectrum.spawn_shooting_star.success", amount), false);
		return amount;
	}

}
