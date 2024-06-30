package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.helpers.BlockReference;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Marks a block as being interactable with via a Blending Stamp and defines its behaviour.
 */
public interface Stampable {

    String BLENDING_DATA_TAG = "spectrum:blending_data";


    /**
     * Creates a reference to a Stampable object.
     */
    BlendingData recordData(Optional<PlayerEntity> user, BlockReference reference, World world);

    /**
     * Call this to request a stampable to process the provided data.
     * @return Whether the state of the impresser changed.
     */
    boolean handleImpression(Optional<UUID> stamper, Optional<PlayerEntity> user, BlockReference reference, World world);

    BlendCategory getBlendCategory();

    boolean canUserStamp(Optional<PlayerEntity> stamper);

    static NbtCompound saveBlendingData(BlendingData data) {
        var compound = new NbtCompound();

        data.stamper.ifPresent(uuid -> compound.putUuid("stamper", uuid));
        if (data.reference == null)
            throw new IllegalStateException("Attempted to save stamp data without a BlockReference!");

        compound.putLong("source", data.reference.pos.asLong());

        return compound;
    }

    static Optional<Stampable.BlendingData> loadBlendingData(World world, NbtCompound nbt) {
        var sourcePair = findSource(world, nbt);
        var source = sourcePair.getLeft();

        if (source.isEmpty())
            return Optional.empty();

        var stamper = Optional.<UUID>empty();

        if (nbt.containsUuid("stamper"))
            stamper = Optional.of(nbt.getUuid("stamper"));

        return Optional.of(new BlendingData(stamper, sourcePair.getRight(), source.get()));
    }

    private static Pair<Optional<Stampable>, BlockReference> findSource(World world, NbtCompound nbt) {
        Stampable stampInteractable = null;
        BlockReference reference;

        if (!nbt.contains("source"))
            return new Pair<>(Optional.empty(), null);


        var pos = BlockPos.fromLong(nbt.getLong("source"));
        var state = world.getBlockState(pos);
        reference = BlockReference.of(state, pos);

        if (state.getBlock()instanceof Stampable interactable)
            stampInteractable = interactable;

        if (world.getBlockEntity(pos) instanceof Stampable interactable) {
            stampInteractable = interactable;
            reference.appendBE((BlockEntity) interactable);
        }

        return new Pair<>(Optional.ofNullable(stampInteractable), reference);
    }

    default boolean verifyStampData(BlendingData data) {
        if (data.source.getBlendCategory() == BlendCategory.UNIQUE) {
            return verifyUniqueStampData(data);
        }
        return data.source.getBlendCategory() == this.getBlendCategory();
    }

    /**
     * Override for unique type interactables.
     */
    @ApiStatus.OverrideOnly
    default boolean verifyUniqueStampData(BlendingData data) {
        return true;
    }

    void onImpressedOther(BlendingData data);

    record BlendingData(Optional<UUID> stamper, BlockReference reference, Stampable source) {

        public BlendingData(@Nullable Entity stamper, BlockReference reference, Stampable source) {
            this(Optional.ofNullable(stamper).map(Entity::getUuid), reference, source);
        }

        public boolean verifyStampData(BlendingData data) {
            return source.verifyStampData(data);
        }

        public void notifySourceOfChange(BlendingData data) {
            source.onImpressedOther(data);
        }

        public boolean canUserStamp(PlayerEntity player) {
            return source.canUserStamp(Optional.ofNullable(player));
        }
    }

    enum BlendCategory {
        PASTEL_NODE,
        UNIQUE
    }
}
