package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public class ParticleSpawnerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
	
	protected ParticleSpawnerConfiguration configuration;
	protected boolean initialized = false;
	
	public ParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(SpectrumBlockEntities.PARTICLE_SPAWNER, blockPos, blockState);
	}
	
	public ParticleSpawnerBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		
		this.configuration = new ParticleSpawnerConfiguration(
				SpectrumParticleTypes.SHOOTING_STAR,
				new Vector3i(80, 40, 0),
				false,
				10.0F,
				new Vector3f(0.0F, 1.0F, 0.0F),
				new Vector3f(0, 0.0F, 0),
				new Vector3f(0.0F, 0.1F, 0),
				new Vector3f(0.1F, 0.1F, 0.1F),
				1.0F,
				0.2F,
				20,
				10,
				0.02F,
				true);
	}
	
	public static void clientTick(World world, BlockPos pos, BlockState state, ParticleSpawnerBlockEntity blockEntity) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof AbstractParticleSpawnerBlock particleSpawnerBlock && particleSpawnerBlock.shouldSpawnParticles(world, pos)) {
			blockEntity.configuration.spawnParticles(world, pos);
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		if (world != null) {
			world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
		}
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.put("particle_config", this.configuration.toNbt());
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.initialized = false;
		if (tag.contains("particle_config", NbtElement.COMPOUND_TYPE)) {
			this.configuration = ParticleSpawnerConfiguration.fromNbt(tag.getCompound("particle_config"));
			this.initialized = true;
		}
	}
	
	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new ParticleSpawnerScreenHandler(syncId, inv, this);
	}
	
	@Override
	public Text getDisplayName() {
		return Text.translatable("block.spectrum.particle_spawner");
	}
	
	public void applySettings(ParticleSpawnerConfiguration configuration) {
		this.configuration = configuration;
		this.initialized = true;
		
		this.updateInClientWorld();
		this.markDirty();
	}
	
	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, @NotNull PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
	}
	
	public ParticleSpawnerConfiguration getConfiguration() {
		return configuration;
	}
	
}
