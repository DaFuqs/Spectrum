package de.dafuqs.spectrum.blocks.threat_conflux;

import de.dafuqs.spectrum.explosion.ExplosionEffectModifier;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThreatConfluxBlockEntity extends BlockEntity {

    private int ticksUntilArmed = 50;
    private int detonationTicks = -1;
    private final List<ExplosionEffectModifier> explosionEffects = new ArrayList<>();

    public ThreatConfluxBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.THREAT_CONFLUX, pos, state);
    }

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, ThreatConfluxBlockEntity conflux) {
        if (conflux.ticksUntilArmed > 50) {
            conflux.ticksUntilArmed--;
            return;
        }

        if (!state.get(ThreatConfluxBlock.ARMED) && conflux.ticksUntilArmed <= 0) {
            conflux.arm(world, pos);
        }

        if (conflux.detonationTicks == 0) {
            conflux.explode(world, pos);
            return;
        }

        if (conflux.detonationTicks > 0) {
            conflux.detonationTicks--;
        }
    }

    public void explode(@NotNull World world, BlockPos pos) {
        world.removeBlock(pos, false);
    }

    public void arm(@NotNull World world, BlockPos pos) {
        world.setBlockState(pos, getCachedState().with(ThreatConfluxBlock.ARMED, true));
        world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.INCANDESCENT_ARM, SoundCategory.BLOCKS, 1, 0.7F + world.getRandom().nextFloat() * 0.2F);
    }

    public void tryDetonate(BlockState state) {
        if (!state.isOf(SpectrumBlocks.THREAT_CONFLUX) || !state.get(ThreatConfluxBlock.ARMED))
            return;

        if (detonationTicks < 0) {
            detonationTicks = 50;
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
