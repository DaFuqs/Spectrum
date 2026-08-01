package de.dafuqs.spectrum.api.predicate.location;

import com.mojang.serialization.*;
import io.netty.buffer.*;
import net.minecraft.commands.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;

import java.util.concurrent.atomic.*;

public record CommandPredicate(String command) implements CommandSource {
	
	public static final Codec<CommandPredicate> CODEC = Codec.STRING.xmap(CommandPredicate::new, CommandPredicate::command);
	public static final StreamCodec<ByteBuf, CommandPredicate> PACKET_CODEC = ByteBufCodecs.STRING_UTF8.map(CommandPredicate::new, CommandPredicate::command);
	
	public boolean test(ServerLevel world, BlockPos pos) {
		AtomicBoolean passed = new AtomicBoolean(false);
		MinecraftServer minecraftServer = world.getServer();
		CommandSourceStack serverCommandSource = new CommandSourceStack(this, Vec3.atCenterOf(pos), Vec2.ZERO, world, 2, "SpectrumCommandWorldCondition", world.getBlockState(pos).getBlock().getName(), minecraftServer, null)
				.withCallback((successful, returnValue) -> passed.set(returnValue > 0));
		minecraftServer.getCommands().performPrefixedCommand(serverCommandSource, command);
		return passed.get();
	}
	
	@Override
	public void sendSystemMessage(Component message) {
	}
	
	@Override
	public boolean acceptsSuccess() {
		return false;
	}
	
	@Override
	public boolean acceptsFailure() {
		return false;
	}
	
	@Override
	public boolean shouldInformAdmins() {
		return false;
	}
	
}
