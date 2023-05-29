package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class JadeVineRootsBlockEntity extends BlockEntity {
	
	private BlockState fenceBlockState;
	private long lastGrowthTick = -1;
	private boolean wasExposedToSunlight = false;
	
	public JadeVineRootsBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.JADE_VINE_ROOTS, pos, state);
		this.fenceBlockState = Blocks.OAK_FENCE.getDefaultState();
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("LastGrowthTick", NbtElement.LONG_TYPE)) {
			this.lastGrowthTick = nbt.getLong("LastGrowthTick");
		}
		if (nbt.contains("WasExposedToSunlight")) {
			this.wasExposedToSunlight = nbt.getBoolean("WasExposedToSunlight");
		}
		if (nbt.contains("FenceBlockIdentifier", NbtElement.STRING_TYPE)) {
			Identifier fenceBlockIdentifier = Identifier.tryParse(nbt.getString("FenceBlockIdentifier"));
			Block block = Registries.BLOCK.get(fenceBlockIdentifier);
			if (block instanceof FenceBlock) {
				this.fenceBlockState = block.getDefaultState();
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putLong("LastGrowthTick", this.lastGrowthTick);
		nbt.putBoolean("WasExposedToSunlight", this.wasExposedToSunlight);
		if (this.fenceBlockState != null) {
			nbt.putString("FenceBlockIdentifier", Registries.BLOCK.getId(this.fenceBlockState.getBlock()).toString());
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
		this.wasExposedToSunlight = false;
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
	
	public boolean wasExposedToSunlight() {
		return wasExposedToSunlight;
	}
	
	public void setExposedToSunlight(boolean wasExposedToSunlight) {
		this.wasExposedToSunlight = wasExposedToSunlight;
		this.markDirty();
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		if (this.fenceBlockState != null) {
			nbtCompound.putString("FenceBlockIdentifier", Registries.BLOCK.getId(this.fenceBlockState.getBlock()).toString());
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
