package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ParticleSpawnerScreenHandler extends ScreenHandler {

   protected final ScreenHandlerContext context;
   protected final PlayerEntity player;
   protected ParticleSpawnerBlockEntity particleSpawnerBlockEntity;

   public ParticleSpawnerScreenHandler(int syncId, PlayerInventory inventory) {
      this(syncId, inventory, ScreenHandlerContext.EMPTY);
   }

   public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
      super(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, syncId);
      this.context = context;
      this.player = playerInventory.player;
   }

    public ParticleSpawnerScreenHandler(int syncId, PlayerInventory inv, ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
        this(syncId, inv);
        this.particleSpawnerBlockEntity = particleSpawnerBlockEntity;
    }

    @Environment(EnvType.CLIENT)
    public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory, packetByteBuf.readBlockPos());
    }

    @Environment(EnvType.CLIENT)
    public ParticleSpawnerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos readBlockPos) {
        super(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, syncId);
        this.player = MinecraftClient.getInstance().player;
        this.context = null;
        BlockEntity blockEntity = MinecraftClient.getInstance().world.getBlockEntity(readBlockPos);
        if(blockEntity instanceof ParticleSpawnerBlockEntity) {
            this.particleSpawnerBlockEntity = (ParticleSpawnerBlockEntity) blockEntity;
        }
    }

    public boolean canUse(PlayerEntity player) {
      return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
   }

   @Environment(EnvType.CLIENT)
   public ParticleSpawnerBlockEntity getBlockEntity() {
       return this.particleSpawnerBlockEntity;
   }

}
