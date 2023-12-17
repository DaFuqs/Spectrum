package de.dafuqs.spectrum.items.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class StructureLocator {

    protected final MinecraftServer server;
    protected final ServerWorld world;
    protected final Acceptor acceptor;
    protected final ChunkPos center;
    protected final Identifier targetId;
    protected final int maxRadius;

    public StructureLocator(ServerWorld world, StructureLocator.Acceptor acceptor, Identifier targetId, ChunkPos center, int maxRadius) {
        this.server = world.getServer();
        this.world = world;
        this.acceptor = acceptor;
        this.targetId = targetId;
        this.center = center;
        this.maxRadius = maxRadius;
    }

    @Nullable
    protected StructureStart locateStructureAtChunk(ChunkPos pos) {
        return null;
    }

    public interface Acceptor {
        void accept(WorldAccess world, StructureStart target);
    }
}
