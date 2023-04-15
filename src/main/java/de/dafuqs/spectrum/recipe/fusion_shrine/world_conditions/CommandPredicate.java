package de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import net.minecraft.server.*;
import net.minecraft.server.command.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;

public class CommandPredicate implements WorldConditionPredicate, CommandOutput {
	
	public String command;
	
	public CommandPredicate(String command) {
		this.command = command;
	}
	
	public static CommandPredicate fromJson(JsonObject json) {
		return new CommandPredicate(json.get("command").getAsString());
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		MinecraftServer minecraftServer = world.getServer();
		ServerCommandSource serverCommandSource = new ServerCommandSource(this, Vec3d.ofCenter(pos), Vec2f.ZERO, world, 2, "FusionShrine", world.getBlockState(pos).getBlock().getName(), minecraftServer, null);
		return minecraftServer.getCommandManager().executeWithPrefix(serverCommandSource, command) > 0;
	}
	
	@Override
	public void sendMessage(Text message) {
	
	}
	
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