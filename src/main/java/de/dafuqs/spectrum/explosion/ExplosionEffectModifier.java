package de.dafuqs.spectrum.explosion;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class ExplosionEffectModifier {

    public final Identifier id;
    public final ExplosionEffectFamily family;

    protected ExplosionEffectModifier(Identifier id, ExplosionEffectFamily family) {
        this.id = id;
        this.family = family;
    }

    public boolean compatibleWithArchetype(Archetype archetype) {
        return family.acceptsArchetype(archetype);
    }

    abstract void applyForBlockEntity(Archetype archetype, @NotNull BlockEntity blockEntity);

    abstract void applyForEntity(Archetype archetype, @NotNull Entity entity);

    abstract void applyToBlock(Archetype archetype, @NotNull World world, @NotNull BlockPos pos);

    abstract void applyToEntity(Archetype archetype, @NotNull Entity entity);

    public float getBlastPowerModiifer(Archetype archetype, BlockEntity blockEntity) {
        return  1F;
    }

    public float getBlastRadiusModiifer(Archetype archetype, BlockEntity blockEntity) {
        return 1F;
    }

    public float getDamageModiifer(Archetype archetype, BlockEntity blockEntity) {
        return 1F;
    }
}
