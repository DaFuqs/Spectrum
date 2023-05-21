package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.particle_spawner.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;

public class ParticleSpawnerScreenHandler extends ScreenHandler {
	
	protected final PlayerEntity player;
	protected ParticleSpawnerBlockEntity particleSpawnerBlockEntity;
	
	public ParticleSpawnerScreenHandler(int syncId, PlayerInventory inventory) {
		super(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, syncId);
		this.player = inventory.player;
	}
	
	public ParticleSpawnerScreenHandler(int syncId, PlayerInventory inv, ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
		this(syncId, inv);
		this.particleSpawnerBlockEntity = particleSpawnerBlockEntity;
	}
	
	public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
		this(syncId, playerInventory, packetByteBuf.readBlockPos());
	}
	
	public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos) {
		super(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, syncId);
		this.player = playerInventory.player;
		BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
			this.particleSpawnerBlockEntity = particleSpawnerBlockEntity;
		} else {
			throw new IllegalArgumentException("Particle Spawner GUI called with a position where no ParticleSpawnerBlockEntity exists");
		}
	}
	
	public ParticleSpawnerBlockEntity getBlockEntity() {
		return this.particleSpawnerBlockEntity;
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.particleSpawnerBlockEntity != null && !this.particleSpawnerBlockEntity.isRemoved();
	}
	
}
