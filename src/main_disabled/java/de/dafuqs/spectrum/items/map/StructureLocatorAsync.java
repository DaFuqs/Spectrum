package de.dafuqs.spectrum.items.map;

import com.mojang.datafixers.util.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.structure.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class StructureLocatorAsync {
	
	private final ServerLevel world;
	private final Optional<HolderSet<Structure>> structures;
	private final AtomicBoolean pinging;
	private final long delayMillis;
	
	private long nextTime = 0;
	
	public StructureLocatorAsync(ServerLevel world, ResourceLocation targetId, long delayMillis) {
		this.world = world;
		this.structures = world.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolder(targetId).map(HolderSet::direct);
		this.pinging = new AtomicBoolean(false);
		this.delayMillis = delayMillis;
	}
	
	public void ping(BlockPos pos, BiConsumer<LevelAccessor, BlockPos> acceptor) {
		if (structures.isEmpty() || pinging.get())
			return;
		
		long time = System.currentTimeMillis();
		if (time < nextTime) return;
		nextTime = time + delayMillis;
		
		CompletableFuture.runAsync(() -> {
			pinging.set(true);
			ChunkGenerator generator = world.getChunkSource().getGenerator();
			Pair<BlockPos, Holder<Structure>> pair = generator.findNearestMapStructure(world, structures.get(), pos, 100, false);
			// TODO Get the centerpoint of the structure region
			if (pair != null) {
				acceptor.accept(world, pair.getFirst());
			}
			pinging.set(false);
		}, Util.backgroundExecutor());
	}
	
}
