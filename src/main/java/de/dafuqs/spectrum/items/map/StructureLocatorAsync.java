package de.dafuqs.spectrum.items.map;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StructureLocatorAsync extends StructureLocator {

    @SuppressWarnings("unchecked")
    private static final CompletableFuture<Void>[] DUMMY_ARRAY = (CompletableFuture<Void>[]) Array.newInstance(CompletableFuture.class, 0);

    private CompletableFuture<Void> nextRing;

    public StructureLocatorAsync(ServerWorld world, StructureLocator.Acceptor acceptor, Identifier targetId, ChunkPos center, int maxRadius) {
        super(world, acceptor, targetId, center, maxRadius);

        searchChunksInRing(1);
    }

    public void cancel() {
        nextRing.cancel(true);
    }

    private void searchChunksInRing(int radius) {
        List<CompletableFuture<Void>> futures = new ArrayList<>(radius * 2 * 4);
        CompletableFuture<CompletableFuture<Void>> start = new CompletableFuture<>();

        for (int i = 0; i < radius * 2; i++) {
            addSearchTask(start, futures, center.x - radius + i, center.z + radius);     // Top-left     -> Top-right
            addSearchTask(start, futures, center.x + radius,     center.z + radius - i); // Top-right    -> Bottom-right
            addSearchTask(start, futures, center.x + radius - i, center.z - radius);     // Bottom-right -> Bottom-left
            addSearchTask(start, futures, center.x - radius,     center.z - radius + i); // Bottom-left  -> Top-left
        }

        nextRing = CompletableFuture.allOf(futures.toArray(DUMMY_ARRAY)).thenRun(() -> {
            if (radius < maxRadius) {
                searchChunksInRing(radius + 1);
            }
        });

        start.complete(nextRing);
    }

    private void addSearchTask(CompletableFuture<CompletableFuture<Void>> start, List<CompletableFuture<Void>> futures, int x, int z) {
        CompletableFuture<Void> future = CompletableFuture.allOf(start).thenRunAsync(() -> {
            StructureStart target = locateStructureAtChunk(new ChunkPos(x, z));

            if (target != null) {
                acceptTarget(target);
            }
        }, server);

        futures.add(future);
    }

    private void acceptTarget(StructureStart target) {
        synchronized (this) {
            acceptor.accept(world, target);
            nextRing.cancel(true);
        }
    }

}
