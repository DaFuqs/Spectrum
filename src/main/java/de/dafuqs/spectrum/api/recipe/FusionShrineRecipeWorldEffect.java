package de.dafuqs.spectrum.api.recipe;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.server.*;
import net.minecraft.server.command.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;

import java.util.*;

/**
 * Effects that are played when crafting with the fusion shrine
 */
public interface FusionShrineRecipeWorldEffect {
	
	Map<String, FusionShrineRecipeWorldEffect> TYPES = new HashMap<>();

	FusionShrineRecipeWorldEffect NOTHING = register("nothing", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) { }
	});
	
	static FusionShrineRecipeWorldEffect register(String id, FusionShrineRecipeWorldEffect effect) {
		TYPES.put(id, effect);
		return effect;
	}
	
	static FusionShrineRecipeWorldEffect fromString(String string) {
		if (string == null || string.isBlank()) {
			return NOTHING;
		}
		if (string.startsWith("/")) {
			return new CommandRecipeWorldEffect(string);
		}
		
		FusionShrineRecipeWorldEffect effect = TYPES.get(string);
		if (effect == null) {
			SpectrumCommon.logError("Unknown fusion shrine world effect '" + string + "'. Will be ignored.");
			return NOTHING;
		}
		return effect;
	}
	
	/**
	 * True for all effects that should just play once.
	 * Otherwise, it will be triggered each tick of the recipe
	 */
	boolean isOneTimeEffect();
	
	void trigger(ServerWorld world, BlockPos pos);
	
	abstract class EveryTickRecipeWorldEffect implements FusionShrineRecipeWorldEffect {
		
		public EveryTickRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
	}
	
	abstract class SingleTimeRecipeWorldEffect implements FusionShrineRecipeWorldEffect {
		
		public SingleTimeRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return true;
		}
		
	}
	
	class CommandRecipeWorldEffect implements FusionShrineRecipeWorldEffect, CommandOutput {
		
		protected final String command;
		
		public CommandRecipeWorldEffect(String command) {
			this.command = command;
		}
		
		public static CommandRecipeWorldEffect fromJson(JsonObject json) {
			return new CommandRecipeWorldEffect(json.getAsString());
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			MinecraftServer minecraftServer = world.getServer();
			ServerCommandSource serverCommandSource = new ServerCommandSource(this, Vec3d.ofCenter(pos), Vec2f.ZERO, world, 2, "FusionShrine", world.getBlockState(pos).getBlock().getName(), minecraftServer, null);
			minecraftServer.getCommandManager().executeWithPrefix(serverCommandSource, command);
		}
		
		@Override
		public void sendMessage(Text message) { }
		
		@Override
		public boolean shouldReceiveFeedback() {
			return false;
		}
		
		@Override
		public boolean shouldTrackOutput() {
			return false;
		}
		
		@Override
		public boolean shouldBroadcastConsoleToOps() {
			return false;
		}
	}
	
}
