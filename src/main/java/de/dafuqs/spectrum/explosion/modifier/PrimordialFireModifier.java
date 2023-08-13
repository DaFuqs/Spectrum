package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.blocks.PrimordialFireBlock;
import de.dafuqs.spectrum.cca.OnPrimordialFireComponent;
import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectFamily;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PrimordialFireModifier extends DamageChangingModifier {


    public PrimordialFireModifier(Identifier id, ExplosionEffectFamily family, DamageSource damageSource, ParticleEffect effect, int color, Item... mappings) {
        super(id, family, damageSource, effect, color, mappings);
    }

    @Override
    public void applyToBlocks(Archetype archetype, @NotNull World world, @NotNull List<BlockPos> blocks) {
        for (BlockPos pos : blocks) {
            if (world.getRandom().nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {
                world.setBlockState(pos, PrimordialFireBlock.getState(world, pos));
            }
        }
        super.applyToBlocks(archetype, world, blocks);
    }

    @Override
    public void applyToEntities(Archetype archetype, @NotNull List<Entity> entity) {
        for (Entity affected : entity) {
            if (affected instanceof LivingEntity livingEntity)
                OnPrimordialFireComponent.addPrimordialFireTicks(livingEntity, 10);
        }
    }

    @Override
    public float getBlastRadiusModifer(Archetype archetype, BlockEntity blockEntity) {
        return 1.25F;
    }
}
