package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

public class ParticleSpawnerScreenHandler extends ScreenHandler {
	
   protected final PlayerEntity player;
   protected ParticleSpawnerBlockEntity particleSpawnerBlockEntity;

   public ParticleSpawnerScreenHandler(int syncId, PlayerInventory inventory) {
	  this(syncId, inventory, ScreenHandlerContext.EMPTY);
   }

   public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
	  super(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, syncId);
	  this.player = playerInventory.player;
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
		if(blockEntity instanceof ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
			this.particleSpawnerBlockEntity = particleSpawnerBlockEntity;
		} else {
			SpectrumCommon.log(Level.WARN, "Particle Spawner GUI called with a position where no ParticleSpawnerBlockEntity exists");
		}
	}
   
   public ParticleSpawnerBlockEntity getBlockEntity() {
	   return this.particleSpawnerBlockEntity;
   }
	
	@Override
	public boolean canUse(PlayerEntity player) {
	   return this.particleSpawnerBlockEntity != null && !this.particleSpawnerBlockEntity.isRemoved();
	}
	
}
