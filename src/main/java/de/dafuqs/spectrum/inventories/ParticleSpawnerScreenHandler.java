package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class ParticleSpawnerScreenHandler extends AbstractContainerMenu {
	
	protected final Player player;
	protected ParticleSpawnerBlockEntity blockEntity;
	
	// clientside
	public ParticleSpawnerScreenHandler(int syncId, Inventory inventory, RegistryFriendlyByteBuf buf) {
		this(syncId, inventory, inventory.player.level().getBlockEntity(BlockPos.STREAM_CODEC.decode(buf), SpectrumBlockEntities.PARTICLE_SPAWNER.get()).orElseThrow());
	}
	
	// serverside
	public ParticleSpawnerScreenHandler(int syncId, Inventory playerInventory, ParticleSpawnerBlockEntity blockEntity) {
		super(SpectrumMenuTypes.PARTICLE_SPAWNER, syncId);
		
		this.player = playerInventory.player;
		this.blockEntity = blockEntity;
	}
	
	public ParticleSpawnerBlockEntity getBlockEntity() {
		return this.blockEntity;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return !this.blockEntity.isRemoved();
	}
	
}
