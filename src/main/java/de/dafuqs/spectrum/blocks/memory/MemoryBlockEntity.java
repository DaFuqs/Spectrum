package de.dafuqs.spectrum.blocks.memory;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MemoryBlockEntity extends BlockEntity implements PlayerOwned {
	
	protected ItemStack memoryItemStack = ItemStack.EMPTY; // zero or negative values: never hatch
	protected UUID ownerUUID;
	
	//  color rendering cache
	private int tint1 = -1;
	private int tint2 = -1;
	
	public MemoryBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.MEMORY, pos, state);
	}
	
	@Contract("_ -> new")
	public static @NotNull Pair<Integer, Integer> getEggColorsForEntity(EntityType<?> entityType) {
		SpawnEggItem spawnEggItem = SpawnEggItem.forEntity(entityType);
		if (spawnEggItem != null) {
			return new Pair<>(spawnEggItem.getColor(0), spawnEggItem.getColor(1));
		}
		return new Pair<>(0x222222, 0xDDDDDD);
	}
	
	public static int getManifestAdvanceSteps(@NotNull World world, @NotNull BlockPos blockPos) {
		BlockState belowBlockState = world.getBlockState(blockPos.down());
		if (belowBlockState.isIn(SpectrumBlockTags.MEMORY_NEVER_MANIFESTERS)) {
			return 0;
		} else if (belowBlockState.isIn(SpectrumBlockTags.MEMORY_VERY_FAST_MANIFESTERS)) {
			return 8;
		} else if (belowBlockState.isIn(SpectrumBlockTags.MEMORY_FAST_MANIFESTERS)) {
			return 3;
		} else {
			return 1;
		}
	}
	
	public void setData(LivingEntity livingEntity, @NotNull ItemStack creatureSpawnItemStack) {
		if (livingEntity instanceof PlayerEntity playerEntity) {
			setOwner(playerEntity);
		}
		if (creatureSpawnItemStack.getItem() instanceof MemoryItem) {
			this.memoryItemStack = creatureSpawnItemStack.copy();
			this.memoryItemStack.setCount(1);
		}
		if (!livingEntity.getWorld().isClient()) {
			this.updateInClientWorld();
		}
		this.markDirty();
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		if (nbt.contains("MemoryItem", NbtElement.COMPOUND_TYPE)) {
			this.memoryItemStack = ItemStack.fromNbt(nbt.getCompound("MemoryItem"));
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.memoryItemStack != null) {
			NbtCompound creatureSpawnCompound = new NbtCompound();
			memoryItemStack.writeNbt(creatureSpawnCompound);
			nbt.put("MemoryItem", creatureSpawnCompound);
		}
	}
	
	public void advanceManifesting(ServerWorld world, BlockPos blockPos) {
		int ticksToManifest = MemoryItem.getTicksToManifest(this.memoryItemStack.getNbt());
		if (ticksToManifest > 0) {
			int additionalManifestAdvanceSteps = getManifestAdvanceSteps(world, blockPos);
			if (additionalManifestAdvanceSteps > 0) {
				int newTicksToManifest = ticksToManifest - additionalManifestAdvanceSteps;
				if (newTicksToManifest <= 0) {
					this.manifest(world, blockPos);
				} else {
					Optional<EntityType<?>> entityTypeOptional = MemoryItem.getEntityType(this.memoryItemStack.getNbt());
					if (entityTypeOptional.isPresent()) {
						MemoryItem.setTicksToManifest(this.memoryItemStack, newTicksToManifest);
						SpectrumS2CPacketSender.playMemoryManifestingParticles(world, blockPos, entityTypeOptional.get(), 3);
						world.playSound(null, this.pos, SpectrumSoundEvents.BLOCK_MEMORY_ADVANCE, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
						this.markDirty();
					}
				}
			}
		}
	}
	
	protected void manifest(@NotNull ServerWorld world, BlockPos blockPos) {
		manifest(world, blockPos, this.memoryItemStack, this.ownerUUID);
	}
	
	public static boolean manifest(@NotNull ServerWorld world, BlockPos blockPos, ItemStack memoryItemStack, @Nullable UUID ownerUUID) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof Waterloggable && blockState.get(Properties.WATERLOGGED)) {
			world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
		} else {
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
		}
		
		Optional<Entity> hatchedEntityOptional = hatchEntity(world, blockPos, memoryItemStack);
		
		if (hatchedEntityOptional.isPresent()) {
			Entity hatchedEntity = hatchedEntityOptional.get();
			
			SpectrumS2CPacketSender.playMemoryManifestingParticles(world, blockPos, hatchedEntity.getType(), 10);
			
			if (hatchedEntity instanceof MobEntity hatchedMobEntity) {
				hatchedMobEntity.setPersistent();
				hatchedMobEntity.playAmbientSound();
				hatchedMobEntity.playSpawnEffects();
			}
			if (ownerUUID != null) {
				EntityHelper.addPlayerTrust(hatchedEntity, ownerUUID);
			}
			
			PlayerEntity owner = PlayerOwned.getPlayerEntityIfOnline(ownerUUID);
			if (owner instanceof ServerPlayerEntity serverPlayerEntity) {
				SpectrumAdvancementCriteria.MEMORY_MANIFESTING.trigger(serverPlayerEntity, hatchedEntity);
			}
			
			return true;
		}
		
		return false;
	}
	
	public int getEggColor(int tintIndex) {
		if (tint1 == -1) {
			if (this.memoryItemStack == null) {
				this.tint1 = 0x222222;
				this.tint2 = 0xDDDDDD;
			} else {
				this.tint1 = MemoryItem.getEggColor(this.memoryItemStack.getNbt(), 0);
				this.tint2 = MemoryItem.getEggColor(this.memoryItemStack.getNbt(), 1);
			}
		}
		
		if (tintIndex == 0) {
			return tint1;
		} else {
			return tint2;
		}
	}
	
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	public static Optional<Entity> hatchEntity(ServerWorld world, BlockPos blockPos, ItemStack memoryItemStack) {
		NbtCompound nbt = memoryItemStack.getNbt();
		if (nbt == null) {
			return Optional.empty();
		}
		
		Optional<EntityType<?>> entityType = MemoryItem.getEntityType(nbt);
		if (entityType.isPresent()) {
			// alignPosition: center the mob in the center of the blockPos
			Entity entity = entityType.get().spawnFromItemStack(world, memoryItemStack, null, blockPos, SpawnReason.SPAWN_EGG, true, false);
			if (entity != null) {
				if (memoryItemStack.hasCustomName()) {
					entity.setCustomName(memoryItemStack.getName());
				}
				if (entity instanceof MobEntity mobEntity) {
					if (!nbt.getBoolean("SpawnAsAdult")) {
						mobEntity.setBaby(true);
					}
				}
				return Optional.of(entity);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(@NotNull PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		markDirty();
	}
	
	public ItemStack getMemoryItemStack() {
		return this.memoryItemStack;
	}
	
}
