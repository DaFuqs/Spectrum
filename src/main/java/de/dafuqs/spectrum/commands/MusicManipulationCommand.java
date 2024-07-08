package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.dafuqs.spectrum.cca.OnPrimordialFireComponent;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class MusicManipulationCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("spectrum_music")
			.requires((source) -> source.hasPermissionLevel(2))
				.then(CommandManager.argument("target", EntityArgumentType.player())
						.executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "target")))));
	}

	private static int execute(ServerCommandSource source, ServerPlayerEntity player) {
		SpectrumS2CPacketSender.playMutableMusic(player);
		return 1;
	}

}
