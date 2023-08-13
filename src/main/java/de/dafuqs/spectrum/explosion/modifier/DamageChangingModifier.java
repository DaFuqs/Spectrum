package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectFamily;
import de.dafuqs.spectrum.explosion.ItemBoundModifier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DamageChangingModifier extends ParticleAddingModifier {

    private final DamageSource damageSource;

    public DamageChangingModifier(Identifier id, ExplosionEffectFamily family, DamageSource damageSource, ParticleEffect effect, int color, Item... mappings) {
        super(id, family, effect, color, mappings);
        this.damageSource = damageSource;
    }

    @Override
    public Optional<DamageSource> getDamageSource(Archetype archetype, BlockEntity blockEntity) {
        return Optional.of(damageSource);
    }
}
