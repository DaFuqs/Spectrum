package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.entity.spawners.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.selector.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;

import java.util.*;

public class ShootingStarCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> spawnShootingStar = Commands.literal("spawn_shooting_star")
				.requires((source) -> source.hasPermission(2)).build();
		ArgumentCommandNode<CommandSourceStack, EntitySelector> targets = Commands.argument("targets", EntityArgument.players())
				.executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets"), 1)).build();
		ArgumentCommandNode<CommandSourceStack, Integer> targetsAmount = Commands.argument("amount", IntegerArgumentType.integer(1))
				.executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))).build();
		
		targets.addChild(targetsAmount);
		spawnShootingStar.addChild(targets);
		root.addChild(spawnShootingStar);
	}
	
	private static int execute(CommandSourceStack source, Collection<? extends ServerPlayer> targets, int amount) {
		for (ServerPlayer entity : targets) {
			for (int i = 0; i < amount; i++) {
				ShootingStarSpawner.spawnShootingStar((ServerLevel) entity.level(), entity);
			}
		}
		source.sendSuccess(() -> Component.translatable("commands.spectrum.spawn_shooting_star.success", amount), false);
		return amount;
	}

}
