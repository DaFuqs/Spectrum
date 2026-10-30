package de.dafuqs.spectrum.dimensions;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.blocks.portal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.portal.*;
import net.minecraft.world.level.saveddata.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ConservatoryDataStore extends SavedData {
	
	public record Entry(UUID player, ResourceLocation returnLevel, BlockPos returnPos, BlockPos conservatoryPos) {
		
		public Optional<Holder.Reference<Level>> getReturnLevel(HolderLookup.Provider lookup) {
			HolderLookup.RegistryLookup<Level> dimensionRegistry = lookup.lookup(Registries.DIMENSION).get();
			return dimensionRegistry.get(ResourceKey.create(Registries.DIMENSION, this.returnLevel));
		}
	}
	
	protected final Collection<Entry> entries = new ArrayList<>();
	
	public static ConservatoryDataStore load(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
		ConservatoryDataStore dataStore = new ConservatoryDataStore();
		
		ListTag entries = compoundTag.getList("entries", CompoundTag.TAG_COMPOUND);
		for(int i = 0; i < entries.size(); ++i) {
			CompoundTag entry = entries.getCompound(i);
			UUID player = entry.getUUID("player");
			ResourceLocation returnLevel = ResourceLocation.parse(entry.getString("return_level"));
			BlockPos returnPos = NbtUtils.readBlockPos(entry, "return_pos").get();
			BlockPos conservatoryPos = NbtUtils.readBlockPos(entry, "conservatory_pos").get();
			dataStore.entries.add(new Entry(player, returnLevel, returnPos, conservatoryPos));
		}
		
		return dataStore;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
		ListTag list = new ListTag();
		for(Entry entry : this.entries) {
			CompoundTag t = new CompoundTag();
			t.putString("return_level", entry.returnLevel.toString());
			t.put("return_pos", NbtUtils.writeBlockPos(entry.returnPos));
			t.put("conservatory_pos", NbtUtils.writeBlockPos(entry.conservatoryPos));
			t.putUUID("player", entry.player);
			list.add(t);
		}
		
		tag.put("entries", list);
		return tag;
	}
	
	public static ConservatoryDataStore getInstance(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(ConservatoryDataStore::new, ConservatoryDataStore::load), "conservatory_data_store");
	}
	
	protected static final int STRUCTURE_HEIGHT = 64;
	protected static final int STRUCTURE_OFFSET = 1024;
	protected static final int MAX_ALLOWED_PLAYER_DISTANCE_FROM_ORIGIN = 512;
	
	
	public static void teleportToConservatory(BlockPos origin, ServerLevel currentLevel, ServerPlayer player, ResourceLocation structure) {
		ServerLevel targetLevel = currentLevel.getServer().getLevel(SpectrumDimensionKeys.CONSERVATORY_KEY);
		if (targetLevel != null) {
			BlockPos targetPos = ConservatoryDataStore.getInstance(currentLevel.getServer()).getOrCreateConservatoryDestinationForExternalPlayer(currentLevel, origin, player, structure);
			if(targetPos != null) {
				player.changeDimension(new DimensionTransition(targetLevel, Vec3.atCenterOf(targetPos), Vec3.ZERO, player.getYRot(), player.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
				BedrockPortalBlock.teleportToSafePosition(targetLevel, player, targetPos, 5);
				CriteriaTriggers.CHANGED_DIMENSION.trigger(player, currentLevel.dimension(), targetLevel.dimension());
			}
		}
	}
	
	public static void teleportBackToEntrance(ServerLevel currentLevel, ServerPlayer player) {
		Pair<ServerLevel, BlockPos> returnPos = ConservatoryDataStore.getInstance(currentLevel.getServer()).getReturnDestinationForPlayer(currentLevel, player);
		if(returnPos == null) {
			// if we can't find where to exit the player, we make them respawn
			currentLevel.getServer().getPlayerList().respawn(player, true, Entity.RemovalReason.CHANGED_DIMENSION);
		} else {
			player.changeDimension(new DimensionTransition(returnPos.getFirst(), Vec3.atCenterOf(returnPos.getSecond()), Vec3.ZERO, player.getYRot(), player.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
			BedrockPortalBlock.teleportToSafePosition(returnPos.getFirst(), player, returnPos.getSecond(), 3);
			CriteriaTriggers.CHANGED_DIMENSION.trigger(player, currentLevel.dimension(), returnPos.getFirst().dimension());
		}
	}
	
	private @Nullable BlockPos getOrCreateConservatoryDestinationForExternalPlayer(ServerLevel externalLevel, BlockPos externalPos, Player player, ResourceLocation structureToGenerate) {
		UUID uuid = player.getUUID();
		for(Entry entry : this.entries) {
			if(entry.returnPos.equals(externalPos) && entry.player.equals(uuid)) {
				return entry.conservatoryPos;
			}
		}
		
		BlockPos.MutableBlockPos conservatoryPos = new BlockPos.MutableBlockPos(0, STRUCTURE_HEIGHT, 0);
		for(Entry entry : this.entries) {
			if(entry.returnPos.equals(conservatoryPos)) {
				conservatoryPos.setY(entry.conservatoryPos.getY() + STRUCTURE_OFFSET);
			}
		}
		
		ServerLevel conservatory = externalLevel.getServer().getLevel(SpectrumDimensionKeys.CONSERVATORY_KEY);
		Structure structure = externalLevel.registryAccess().registry(Registries.STRUCTURE).get().get(structureToGenerate);
		if(conservatory == null || structure == null) {
			return null;
		}
		placeStructure(conservatory, structure, conservatoryPos);
		
		Entry newEntry = new Entry(uuid, externalLevel.dimension().location(), externalPos, conservatoryPos);
		this.entries.add(newEntry);
		this.setDirty();
		return newEntry.conservatoryPos;
	}
	
	public boolean isPlayerOutsideConservatoryBounds(Level conservatory, Player player) {
		UUID uuid = player.getUUID();
		for(Entry entry : this.entries) {
			if(entry.conservatoryPos.equals(conservatory) && entry.player.equals(uuid)) {
				return entry.conservatoryPos.distManhattan(player.blockPosition()) > MAX_ALLOWED_PLAYER_DISTANCE_FROM_ORIGIN;
			}
		}
		
		// why are they here in the first place?!
		return true;
	}
	
	private @Nullable Pair<ServerLevel, BlockPos> getReturnDestinationForPlayer(ServerLevel conservatory, Player player) {
		UUID uuid = player.getUUID();
		for(Entry entry : this.entries) {
			if(entry.player.equals(uuid) && entry.conservatoryPos.distManhattan(player.blockPosition()) > MAX_ALLOWED_PLAYER_DISTANCE_FROM_ORIGIN) {
				Optional<Holder.Reference<Level>> returnLevel = entry.getReturnLevel(conservatory.registryAccess());
				return returnLevel
						.map(levelReference -> new Pair<>((ServerLevel) levelReference.value(), entry.returnPos))
						.orElse(null);
			}
		}
		return null;
	}
	
	private static boolean placeStructure(ServerLevel conservatory, Structure structure, BlockPos pos) {
		ChunkGenerator chunkgenerator = conservatory.getChunkSource().getGenerator();
		StructureStart structurestart = structure.generate(conservatory.registryAccess(), chunkgenerator, chunkgenerator.getBiomeSource(), conservatory.getChunkSource().randomState(),
				conservatory.getStructureManager(), conservatory.getSeed(), new ChunkPos(pos), 0, conservatory, (b) -> true);
		
		if (!structurestart.isValid()) {
			return false;
		}
		
		BoundingBox boundingbox = structurestart.getBoundingBox();
		ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
		ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
		ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((cp) -> structurestart.placeInChunk(conservatory, conservatory.structureManager(), chunkgenerator, conservatory.getRandom(), new BoundingBox(cp.getMinBlockX(), conservatory.getMinBuildHeight(), cp.getMinBlockZ(), cp.getMaxBlockX(), conservatory.getMaxBuildHeight(), cp.getMaxBlockZ()), cp));
		return true;
	}
	
}
