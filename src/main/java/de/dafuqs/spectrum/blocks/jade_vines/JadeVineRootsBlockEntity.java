package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JadeVineRootsBlockEntity extends BlockEntity {
	
	private BlockState fenceBlockState;
	private long lastGrowthTick = -1;
	
	public JadeVineRootsBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.JADE_VINE_ROOTS, pos, state);
		this.fenceBlockState = Blocks.OAK_FENCE.getDefaultState();
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("LastGrowthTick", NbtElement.LONG_TYPE)) {
			this.lastGrowthTick = nbt.getLong("LastGrowthTick");
		}
		if (nbt.contains("FenceBlockIdentifier", NbtElement.STRING_TYPE)) {
			Identifier fenceBlockIdentifier = Identifier.tryParse(nbt.getString("FenceBlockIdentifier"));
			Block block = Registry.BLOCK.get(fenceBlockIdentifier);
			if (block instanceof FenceBlock) {
				this.fenceBlockState = block.getDefaultState();
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putLong("LastGrowthTick", this.lastGrowthTick);
		if (this.fenceBlockState != null) {
			nbt.putString("FenceBlockIdentifier", Registry.BLOCK.getId(this.fenceBlockState.getBlock()).toString());
		}
	}
	
	public boolean isLaterNight(@NotNull World world) {
		long dayTime = world.getTimeOfDay();
		if (TimeHelper.getTimeOfDay(dayTime).isNight()) { // timeOfDay % 24000 >= 13000 && timeOfDay % 24000 < 23000
			return TimeHelper.getDay(dayTime + 1000) != TimeHelper.getDay(lastGrowthTick + 1000);
		}
		return false;
	}
	
	public long getLastGrownTime() {
		return lastGrowthTick;
	}
	
	public void setLastGrownTime(long time) {
		this.lastGrowthTick = time;
		this.markDirty();
	}
	
	public BlockState getFenceBlockState() {
		return fenceBlockState;
	}
	
	public void setFenceBlockState(BlockState fenceBlockState) {
		this.fenceBlockState = fenceBlockState;
		this.markDirty();
		this.updateInClientWorld();
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		if (this.fenceBlockState != null) {
			nbtCompound.putString("FenceBlockIdentifier", Registry.BLOCK.getId(this.fenceBlockState.getBlock()).toString());
		}
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
}
