package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class ShootingStarCommand {
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_spawn_shooting_star").requires((source) -> {
			return source.hasPermissionLevel(2);
		}).then(CommandManager.argument("targets", EntityArgumentType.players()).executes((context) -> {
			return execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), 1);
		}).then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes((context) -> {
			return execute(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"));
		})))));
	}
	
	private static int execute(ServerCommandSource source, Collection<? extends ServerPlayerEntity> targets, int amount) {
		for (ServerPlayerEntity entity : targets) {
			for (int i = 0; i < amount; i++) {
				ShootingStarEntity.spawnShootingStar((ServerWorld) entity.world, entity);
			}
		}
		source.sendFeedback(new TranslatableText("commands.spectrum.spawn_shooting_star.success", amount), false);
		return amount;
	}
	
}
