package de.dafuqs.spectrum.blocks.pastel_network;

import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.world.*;

public class PastelPersistentState extends PersistentState {

    private static final String NAME = "spectrum_pastel";

    public PastelPersistentState() {
        super();
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static PastelPersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(PastelPersistentState::fromNbt, PastelPersistentState::new, NAME);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        return Pastel.getServerInstance().toNbt(tag);
    }

    public static PastelPersistentState fromNbt(NbtCompound nbt) {
        PastelPersistentState state = new PastelPersistentState();
        //Pastel.serverInstanceFromNbt(false, nbt);
        return state;
    }

}
