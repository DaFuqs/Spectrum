package de.dafuqs.spectrum.items.map;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.server.*;
import net.minecraft.server.world.*;
import net.minecraft.structure.*;
import net.minecraft.util.*;
import net.minecraft.util.logging.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.placement.*;
import net.minecraft.world.gen.structure.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class StructureLocatorAsync {

    private final MinecraftServer server;
    private final ServerWorld world;
    private final StructureLocatorAsync.Acceptor acceptor;
    private final Identifier targetId;
    private final int maxRadius;
    private ChunkPos center;
    private RegistryEntry<Structure> registryEntry;

    @Nullable
    private LocatorThread thread;
    private int radius;

    public StructureLocatorAsync(ServerWorld world, StructureLocatorAsync.Acceptor acceptor, Identifier targetId, ChunkPos center, int maxRadius) {
        this.server = world.getServer();
        this.world = world;
        this.acceptor = acceptor;
        this.targetId = targetId;
        this.center = center;
        this.maxRadius = maxRadius;

        thread = null;
        radius = 1;

        start();
    }

    private void start() {
        thread = new LocatorThread();
        thread.start();
    }

    public void move(int deltaX, int deltaZ) {
        if (deltaX == 0 && deltaZ == 0) return;

        cancel();

        // If we move two chunks in a direction, continuing at the same radius would skip a strip of chunks.
        // So, we reduce the radius to make sure nothing is skipped. Of course, outer chunks would get
        // skipped regardless.
        radius -= Math.max(Math.abs(deltaX), Math.abs(deltaZ));
        if (radius < 1) radius = 1;

        center = new ChunkPos(center.x + deltaX, center.z + deltaZ);

        start();
    }

    public void cancel() {
        if (thread == null) return;

        thread.stopRunning();
        thread.interrupt();

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
                continue;
            }
            break;
        }

        thread = null;
    }

    private class LocatorThread extends Thread {

        private static final int MAX_RUNNING_TASKS = 32;
        private static final AtomicInteger currentRunningThreads = new AtomicInteger(0);

        private final Semaphore semaphore;
        private boolean running;
        private boolean ringHadTargets;

        public LocatorThread() {
            super("Structure Locator #" + currentRunningThreads.getAndIncrement());
            setUncaughtExceptionHandler(new UncaughtExceptionLogger(SpectrumCommon.LOGGER));
            semaphore = new Semaphore(MAX_RUNNING_TASKS);
        }

        public void stopRunning() {
            running = false;
        }

        @Override
        public void run() {
            running = true;
            ringHadTargets = false;

            registryEntry = getRegistryEntry();
            if (registryEntry == null) return;

            checkConcentricRingsStructures();

            while(running && !ringHadTargets && radius <= maxRadius) {
                for (int i = 0; running && i < radius * 2; i++) {
                    searchChunk(center.x - radius + i, center.z + radius);     // Top-left     -> Top-right
                    searchChunk(center.x + radius,     center.z + radius - i); // Top-right    -> Bottom-right
                    searchChunk(center.x + radius - i, center.z - radius);     // Bottom-right -> Bottom-left
                    searchChunk(center.x - radius,     center.z - radius + i); // Bottom-left  -> Top-left
                }

                radius++;
            }
        }

        private RegistryEntry<Structure> getRegistryEntry() {
            Registry<Structure> registry = world.getRegistryManager().getOptional(RegistryKeys.STRUCTURE).orElse(null);
            if (registry == null) return null;

            Structure structure = registry.get(targetId);
            if (structure == null) return null;

            return registry.getEntry(structure);
        }

        private void checkConcentricRingsStructures() {
            StructurePlacementCalculator calculator = world.getChunkManager().getStructurePlacementCalculator();

            double minDistance = Double.MAX_VALUE;
            ChunkPos concentricStart = null;

            for (StructurePlacement placement : calculator.getPlacements(registryEntry)) {
                if (placement instanceof ConcentricRingsStructurePlacement concentricRingsStructurePlacement) {
                    List<ChunkPos> positions = calculator.getPlacementPositions(concentricRingsStructurePlacement);
                    if (positions != null) {
                        for (ChunkPos pos : positions) {
                            double dx = (double) pos.x - (double) center.x;
                            double dz = (double) pos.z - (double) center.z;
                            double distance = dx * dx + dz * dz;
                            if (distance < minDistance) {
                                minDistance = distance;
                                concentricStart = pos;
                            }
                        }
                    }
                }
            }

            if (concentricStart != null) {
                acceptTarget(concentricStart);
            }
        }

        private void searchChunk(int x, int z) {
            while (running) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ignored) {
                    continue;
                }

                server.send(new ServerTask(server.getTicks(), () -> {
                    ChunkPos chunkPos = new ChunkPos(x, z);
                    StructureStart target = locateStructureAtChunk(chunkPos);
                    if (target != null) {
                        acceptTarget(chunkPos);
                    }
                    semaphore.release();
                }));

                break;
            }
        }

        @Nullable
        private StructureStart locateStructureAtChunk(ChunkPos pos) {
            StructureAccessor accessor = world.getStructureAccessor();
            Structure structure = registryEntry.value();

            StructurePresence presence = accessor.getStructurePresence(pos, structure, false);
            if (presence == StructurePresence.START_NOT_PRESENT) return null;

            Chunk chunk = world.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_STARTS);
            return accessor.getStructureStart(ChunkSectionPos.from(chunk), structure, chunk);
        }
        
        private void acceptTarget(ChunkPos target) {
            synchronized (this) {
                if (running) {
                    ringHadTargets = true;
                    acceptor.accept(world, target);
                }
            }
        }

    }

    public interface Acceptor {
        void accept(WorldAccess world, ChunkPos target);
    }

}
