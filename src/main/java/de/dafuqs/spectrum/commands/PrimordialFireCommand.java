package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import de.dafuqs.spectrum.cca.*;
import net.minecraft.command.argument.*;
import net.minecraft.entity.*;
import net.minecraft.server.command.*;
import net.minecraft.text.*;

import java.util.*;

public class PrimordialFireCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("spectrum_primordial_fire")
			.requires((source) -> source.hasPermissionLevel(2))
			.then(CommandManager.argument("targets", EntityArgumentType.entities())
				.executes((context) -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets"), 200))
			.then(CommandManager.argument("duration", IntegerArgumentType.integer(0))
				.executes((context) -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "duration"))))));
	}

	private static int execute(ServerCommandSource source, Collection<? extends Entity> targets, int ticks) {
		int affectedTargets = 0;

		for (Entity entity : targets) {
			if(entity instanceof LivingEntity livingEntity) {
				OnPrimordialFireComponent.setPrimordialFireTicks(livingEntity, ticks);
				affectedTargets++;
			}
		}

		if(ticks > 0) {
			source.sendFeedback(() -> Text.translatable("commands.spectrum.primordial_fire.put_on.success", targets.size()), false);
		} else {
			source.sendFeedback(() -> Text.translatable("commands.spectrum.primordial_fire.put_out.success", targets.size()), false);
		}

		return affectedTargets;
	}

}
