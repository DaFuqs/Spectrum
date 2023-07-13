package de.dafuqs.spectrum.blocks.structure;

import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.command.argument.*;
import net.minecraft.nbt.*;
import net.minecraft.server.*;
import net.minecraft.server.command.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PreservationBlockDetectorBlockEntity extends BlockEntity implements CommandOutput {
	
	protected @Nullable BlockState detectedState; // detect this block. Null: any block
	protected @Nullable BlockState changeIntoState; // change into this once triggered. Null: stay as is (can be used again and again)
	protected List<String> commands = List.of(); // get executed in order. First command that fails ends the chain
	
	public PreservationBlockDetectorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_BLOCK_DETECTOR, pos, state);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (this.changeIntoState != null) {
			nbt.putString("change_into_state", BlockArgumentParser.stringifyBlockState(this.changeIntoState));
		}
		if (this.detectedState != null) {
			nbt.putString("detected_state", BlockArgumentParser.stringifyBlockState(this.detectedState));
		}
		if (!this.commands.isEmpty()) {
			NbtList commandList = new NbtList();
			for (String s : this.commands) {
				commandList.add(NbtString.of(s));
			}
			nbt.put("commands", commandList);
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.commands = new ArrayList<>();
		this.changeIntoState = null;
		this.detectedState = null;
		if (nbt.contains("commands", NbtElement.LIST_TYPE)) {
			for (NbtElement e : nbt.getList("commands", NbtElement.STRING_TYPE)) {
				this.commands.add(e.asString());
			}
		}
		if (nbt.contains("change_into_state", NbtElement.STRING_TYPE)) {
			try {
				this.changeIntoState = BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), nbt.getString("change_into_state"), false).blockState();
			} catch (CommandSyntaxException ignored) {
			}
		}
		if (nbt.contains("detected_state", NbtElement.STRING_TYPE)) {
			try {
				this.detectedState = BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), nbt.getString("detected_state"), false).blockState();
			} catch (CommandSyntaxException ignored) {
			}
		}
	}
	
	public void triggerForNeighbor(BlockState state) {
		if ((this.detectedState == null || state.equals(this.detectedState)) && this.world instanceof ServerWorld serverWorld) {
			this.execute(serverWorld);
		}
	}
	
	public void execute(ServerWorld serverWorld) {
		MinecraftServer minecraftServer = serverWorld.getServer();
		if (minecraftServer.areCommandBlocksEnabled() && !this.commands.isEmpty()) {
			ServerCommandSource serverCommandSource = new ServerCommandSource(this, Vec3d.ofCenter(PreservationBlockDetectorBlockEntity.this.pos), Vec2f.ZERO, serverWorld, 2, "PreservationBlockDetector", this.getWorld().getBlockState(this.pos).getBlock().getName(), minecraftServer, null);
			for (String command : this.commands) {
				int success = minecraftServer.getCommandManager().executeWithPrefix(serverCommandSource, command);
				if (success < 1) {
					break;
				}
			}
			if (this.changeIntoState != null) {
				serverWorld.setBlockState(this.pos, this.changeIntoState);
			}
		}
	}
	
	@Override
	public boolean copyItemDataRequiresOperator() {
		return true;
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
