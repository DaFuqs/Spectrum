package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParticleAddingModifier extends ExplosionModifier {
    
    private final ParticleEffect particleEffect;
    
    public ParticleAddingModifier(Identifier id, ExplosionModifierType type, ParticleEffect particleEffect, int displayColor) {
        super(id, type, displayColor);
        this.particleEffect = particleEffect;
    }
    
    @Override
    public void applyToEntities(@NotNull List<Entity> entity) {
    }
    
    @Override
    public void applyToBlocks(@NotNull World world, @NotNull List<BlockPos> blocks) {
    }
    
    @Override
    public void applyToWorld(@NotNull World world, @NotNull Vec3d center) {
    }
    
    @Override
    public Optional<ParticleEffect> getParticleEffects() {
        return Optional.of(particleEffect);
    }
}
