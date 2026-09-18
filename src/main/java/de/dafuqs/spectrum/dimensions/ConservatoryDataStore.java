package de.dafuqs.spectrum.dimensions;

import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.commands.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.saveddata.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ConservatoryDataStore extends SavedData {
	
	public record Entry(UUID player, BlockPos overworldPos, BlockPos conservatoryPos) { }
	
	protected final Collection<Entry> entries = new ArrayList<>();
	
	public static ConservatoryDataStore load(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
		ConservatoryDataStore dataStore = new ConservatoryDataStore();
		
		ListTag entries = compoundTag.getList("entries", CompoundTag.TAG_COMPOUND);

		for(int i = 0; i < entries.size(); ++i) {
			CompoundTag entry = entries.getCompound(i);
			UUID player = entry.getUUID("player");
			BlockPos overworldPos = NbtUtils.readBlockPos(entry, "overworld_pos").get();
			BlockPos conservatoryPos = NbtUtils.readBlockPos(entry, "conservatory_pos").get();
			dataStore.entries.add(new Entry(player, overworldPos, conservatoryPos));
		}
		
		return dataStore;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
		ListTag list = new ListTag();
		for(Entry entry : this.entries) {
			CompoundTag t = new CompoundTag();
			t.put("overworld_pos", NbtUtils.writeBlockPos(entry.overworldPos));
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
	
	public BlockPos getOrCreateConservatoryDestinationForOverworldPlayer(ServerLevel overworld, BlockPos overworldPos, Player player) {
		UUID uuid = player.getUUID();
		for(Entry entry : this.entries) {
			if(entry.overworldPos.equals(overworldPos) && entry.player.equals(uuid)) {
				return entry.conservatoryPos;
			}
		}
		
		BlockPos.MutableBlockPos conservatoryPos = new BlockPos.MutableBlockPos(0, STRUCTURE_HEIGHT, 0);
		for(Entry entry : this.entries) {
			if(entry.overworldPos.equals(conservatoryPos)) {
				conservatoryPos.setY(entry.conservatoryPos.getY() + STRUCTURE_OFFSET);
			}
		}
		
		ServerLevel conservatory = overworld.getServer().getLevel(SpectrumDimensionKeys.CONSERVATORY_KEY);
		Structure structure = overworld.registryAccess().registry(Registries.STRUCTURE).get().get(SpectrumCommon.locate("conservatory/test"));
		placeStructure(conservatory, structure, conservatoryPos);
		
		Entry newEntry = new Entry(uuid, overworldPos, conservatoryPos);
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
	
	public @Nullable BlockPos getOverworldDestinationForPlayer(ServerLevel conservatory, BlockPos portalPos, Player player) {
		UUID uuid = player.getUUID();
		for(Entry entry : this.entries) {
			if(entry.player.equals(uuid) && entry.conservatoryPos.distManhattan(player.blockPosition()) > MAX_ALLOWED_PLAYER_DISTANCE_FROM_ORIGIN) {
				return entry.overworldPos;
			}
		}
		return null;
	}
	
	public static boolean placeStructure(ServerLevel conservatory, Structure structure, BlockPos pos) {
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
