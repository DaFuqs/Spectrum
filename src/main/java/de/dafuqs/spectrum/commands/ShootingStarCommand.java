package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;

import java.util.*;

public class ShootingStarCommand {
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_spawn_shooting_star").requires((source) -> source.hasPermissionLevel(2)).then(CommandManager.argument("targets", EntityArgumentType.players()).executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), 1)).then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount")))))));
	}
	
	private static int execute(ServerCommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		for (ServerPlayerEntity entity : targets) {
			for (int i = 0; i < amount; i++) {
				ShootingStarEntity.spawnShootingStar((ServerWorld) entity.world, entity);
			}
		}
		source.sendFeedback(Text.translatable("commands.spectrum.spawn_shooting_star.success", amount), false);
		return amount;
	}
	
}
