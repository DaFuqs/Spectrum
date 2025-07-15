package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.*;
import de.dafuqs.spectrum.cca.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.selector.*;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;

import java.util.*;

public class PrimordialFireCommand {
	
	public static void register(LiteralCommandNode<CommandSourceStack> root) {
		LiteralCommandNode<CommandSourceStack> primordialFire = Commands.literal("primordial_fire").requires((source) -> source.hasPermission(2)).build();
		ArgumentCommandNode<CommandSourceStack, EntitySelector> targets = Commands.argument("targets", EntityArgument.entities())
				.executes((context) -> execute(context.getSource(), EntityArgument.getEntities(context, "targets"), 200)).build();
		ArgumentCommandNode<CommandSourceStack, Integer> targetsDuration = Commands.argument("duration", IntegerArgumentType.integer(0))
				.executes((context) -> execute(context.getSource(), EntityArgument.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "duration"))).build();
		
		targets.addChild(targetsDuration);
		primordialFire.addChild(targets);
		root.addChild(primordialFire);
	}
	
	private static int execute(CommandSourceStack source, Collection<? extends Entity> targets, int ticks) {
		int affectedTargets = 0;
		
		for (Entity entity : targets) {
			if (entity instanceof LivingEntity livingEntity) {
				OnPrimordialFireComponent.setPrimordialFireTicks(livingEntity, ticks);
				affectedTargets++;
			}
		}
		
		if (ticks > 0) {
			source.sendSuccess(() -> Component.translatable("commands.spectrum.primordial_fire.put_on.success", targets.size()), false);
		} else {
			source.sendSuccess(() -> Component.translatable("commands.spectrum.primordial_fire.put_out.success", targets.size()), false);
		}
		
		return affectedTargets;
	}
	
}
