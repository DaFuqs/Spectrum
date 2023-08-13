package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectFamily;
import de.dafuqs.spectrum.explosion.ItemBoundModifier;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ParticleAddingModifier extends ItemBoundModifier {

    private final ParticleEffect particleEffect;

    public ParticleAddingModifier(Identifier id, ExplosionEffectFamily family, ParticleEffect particleEffect, int color, Item... mappings) {
        super(id, family, color, mappings);
        this.particleEffect = particleEffect;
    }

    @Override
    public void applyToEntities(Archetype archetype, @NotNull List<Entity> entity) {}

    @Override
    public void applyToBlocks(Archetype archetype, @NotNull World world, @NotNull List<BlockPos> blocks) {}

    @Override
    public void applyToWorld(Archetype archetype, @NotNull World world, @NotNull Vec3d center) {}

    @Override
    public Optional<ParticleEffect> getParticleEffects(Archetype archetype) {
        return Optional.of(particleEffect);
    }
}
