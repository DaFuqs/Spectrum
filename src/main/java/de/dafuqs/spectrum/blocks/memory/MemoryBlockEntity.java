package de.dafuqs.spectrum.blocks.memory;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
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
	public static @NotNull Tuple<Integer, Integer> getEggColorsForEntity(EntityType<?> entityType) {
		SpawnEggItem spawnEggItem = SpawnEggItem.byId(entityType);
		if (spawnEggItem != null) {
			return new Tuple<>(spawnEggItem.getColor(0), spawnEggItem.getColor(1));
		}
		return new Tuple<>(0x222222, 0xDDDDDD);
	}
	
	public static int getManifestAdvanceSteps(@NotNull Level world, @NotNull BlockPos blockPos) {
		BlockState belowBlockState = world.getBlockState(blockPos.below());
		if (belowBlockState.is(SpectrumBlockTags.MEMORY_NEVER_MANIFESTERS)) {
			return 0;
		} else if (belowBlockState.is(SpectrumBlockTags.MEMORY_VERY_FAST_MANIFESTERS)) {
			return 8;
		} else if (belowBlockState.is(SpectrumBlockTags.MEMORY_FAST_MANIFESTERS)) {
			return 3;
		} else {
			return 1;
		}
	}
	
	public void setData(LivingEntity livingEntity, @NotNull ItemStack creatureSpawnItemStack) {
		if (livingEntity instanceof Player playerEntity)
			setOwner(playerEntity);
		
		if (creatureSpawnItemStack.getItem() instanceof MemoryItem) {
			this.memoryItemStack = creatureSpawnItemStack.copy();
			this.memoryItemStack.setCount(1);
		}
		
		if (livingEntity.level() instanceof ServerLevel serverWorld)
			serverWorld.getChunkSource().blockChanged(worldPosition);
		
		this.setChanged();
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		if (nbt.contains("MemoryItem", Tag.TAG_COMPOUND)) {
			this.memoryItemStack = ItemStack.parseOptional(registryLookup, nbt.getCompound("MemoryItem"));
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.memoryItemStack.isEmpty()) {
			CodecHelper.writeNbt(nbt, "MemoryItem", ItemStack.CODEC, memoryItemStack);
		}
	}
	
	public void advanceManifesting(ServerLevel world, BlockPos blockPos) {
		int ticksToManifest = MemoryItem.getTicksToManifest(this.memoryItemStack);
		if (ticksToManifest > 0) {
			int additionalManifestAdvanceSteps = getManifestAdvanceSteps(world, blockPos);
			if (additionalManifestAdvanceSteps > 0) {
				int newTicksToManifest = ticksToManifest - additionalManifestAdvanceSteps;
				if (newTicksToManifest <= 0) {
					this.manifest(world, blockPos);
				} else {
					Optional<EntityType<?>> entityTypeOptional = MemoryItem.getEntityType(this.memoryItemStack);
					if (entityTypeOptional.isPresent()) {
						MemoryItem.setTicksToManifest(this.memoryItemStack, newTicksToManifest);
						PlayMemoryManifestingParticlesPayload.playMemoryManifestingParticles(world, blockPos, entityTypeOptional.get(), 3);
						world.playSound(null, this.worldPosition, SpectrumSoundEvents.BLOCK_MEMORY_ADVANCE, SoundSource.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
						this.setChanged();
					}
				}
			}
		}
	}
	
	protected void manifest(@NotNull ServerLevel world, BlockPos blockPos) {
		manifest(world, blockPos, this.memoryItemStack, this.ownerUUID);
	}
	
	public static boolean manifest(@NotNull ServerLevel world, BlockPos blockPos, ItemStack memoryItemStack, @Nullable UUID ownerUUID) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof SimpleWaterloggedBlock && blockState.getValue(BlockStateProperties.WATERLOGGED)) {
			world.setBlockAndUpdate(blockPos, Blocks.WATER.defaultBlockState());
		} else {
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
		}
		
		Optional<Entity> hatchedEntityOptional = hatchEntity(world, blockPos, memoryItemStack);
		
		if (hatchedEntityOptional.isPresent()) {
			Entity hatchedEntity = hatchedEntityOptional.get();
			
			PlayMemoryManifestingParticlesPayload.playMemoryManifestingParticles(world, blockPos, hatchedEntity.getType(), 10);
			
			if (hatchedEntity instanceof Mob hatchedMobEntity) {
				hatchedMobEntity.setPersistenceRequired();
				hatchedMobEntity.playAmbientSound();
				hatchedMobEntity.spawnAnim();
			}
			if (ownerUUID != null) {
				EntityHelper.addPlayerTrust(hatchedEntity, ownerUUID);
			}
			
			Player owner = PlayerOwned.getPlayerEntityIfOnline(ownerUUID);
			if (owner instanceof ServerPlayer serverPlayerEntity) {
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
				this.tint1 = MemoryItem.getEggColor(this.memoryItemStack, 0);
				this.tint2 = MemoryItem.getEggColor(this.memoryItemStack, 1);
			}
		}
		
		if (tintIndex == 0) {
			return tint1;
		} else {
			return tint2;
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	public static Optional<Entity> hatchEntity(ServerLevel world, BlockPos blockPos, ItemStack memoryItemStack) {
		return Optional.ofNullable(memoryItemStack.get(SpectrumDataComponentTypes.MEMORY))
				.flatMap(memory -> MemoryItem.getEntityType(memoryItemStack)
						.map(entityType -> {
							// alignPosition: center the mob in the center of the blockPos
							Entity entity = entityType.spawn(world, memoryItemStack, null, blockPos, MobSpawnType.SPAWN_EGG, true, false);
							if (entity instanceof Mob mobEntity && !memory.spawnAsAdult())
								mobEntity.setBaby(true);
							return entity;
						})
				);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(@NotNull Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	public ItemStack getMemoryItemStack() {
		return this.memoryItemStack;
	}
	
}
