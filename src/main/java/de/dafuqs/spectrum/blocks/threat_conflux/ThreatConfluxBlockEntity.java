package de.dafuqs.spectrum.blocks.threat_conflux;

import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectModifier;
import de.dafuqs.spectrum.helpers.Orientation;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ThreatConfluxBlockEntity extends BlockEntity {

    public static final Archetype ARCHETYPE = Archetype.DEFENSIVE;
    public static final double BASE_EXPLOSION_RADIUS = 5;
    public static final double KILL_ZONE_RADIUS = 0.5;
    public static final float BASE_EXPLOSION_DAMAGE = 20;
    public static final float KILL_ZONE_DAMAGE = 2500;
    private int ticksUntilArmed = 50;
    private int detonationTicks = -1;
    private List<ExplosionEffectModifier> explosionEffects = new ArrayList<>();

    public ThreatConfluxBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.THREAT_CONFLUX, pos, state);
    }

    public static void tick(@NotNull World world, BlockPos pos, BlockState state, ThreatConfluxBlockEntity conflux) {
        if (conflux.ticksUntilArmed > 0) {
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

        var killDamage = KILL_ZONE_DAMAGE;
        var killRadius = KILL_ZONE_RADIUS;
        var blastRadius = BASE_EXPLOSION_RADIUS;
        var blastDamage = BASE_EXPLOSION_DAMAGE;
        var damageSource = SpectrumDamageSources.INCANDESCENCE;

        for (ExplosionEffectModifier explosionEffect : explosionEffects) {
            var radiusMod = explosionEffect.getBlastRadiusModifer(ARCHETYPE, this);
            var damageMod = explosionEffect.getDamageModifer(ARCHETYPE, this);

            blastRadius *= radiusMod;
            blastDamage *= damageMod;
            killRadius *= Math.max((radiusMod - 1) / 3 + 1, 1);
            killDamage *= (damageMod - 1) / 2 + 1;
            var effectDamage = explosionEffect.getDamageSource(ARCHETYPE, this);
            if (effectDamage.isPresent())
                damageSource = effectDamage.get();
        }

        var center = Vec3d.ofCenter(pos);
        world.playSound(null, center.getX(), center.getY(), center.getZ(), SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.BLOCKS, 0.5F, 2F);
        world.playSound(null, center.getX(), center.getY(), center.getZ(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.BLOCKS, 2F, 0.2F + world.getRandom().nextFloat() * 0.3F);
        world.playSound(null, center.getX(), center.getY(), center.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2F, 0.7F + world.getRandom().nextFloat() * 0.3F);

        if (world.isClient()) {
            handleExplosionEffects(center, explosionEffects, blastRadius);
            return;
        }

        var serverWorld = (ServerWorld) world;
        var blastBox = Box.of(center, blastRadius * 2, blastRadius * 2, blastRadius * 2);
        var blocks = BlockPos.stream(blastBox).toList();
        var affectedEntities = serverWorld.getOtherEntities(null, blastBox);
        double finalBlastRadius = blastRadius;

        affectedEntities = affectedEntities.stream().filter(entity -> entity.getPos().distanceTo(center) < finalBlastRadius).toList();

        for (Entity entity : affectedEntities) {
            var entityDistance = Math.max(entity.getPos().distanceTo(center) - entity.getWidth() / 2, 0);
            if (entityDistance <= killRadius) {
                entity.damage(damageSource, entity.getType().isIn(ConventionalEntityTypeTags.BOSSES) ? killDamage / 25F : killDamage);
            }
            else {
                var finalDamage = MathHelper.lerp(entityDistance / blastRadius, blastDamage, blastDamage / 2);
                entity.damage(damageSource, (float) finalDamage);
            }
        }

        for (ExplosionEffectModifier explosionEffect : explosionEffects) {
            explosionEffect.applyToWorld(ARCHETYPE, world, center);
            explosionEffect.applyToBlocks(ARCHETYPE, world, blocks);
            explosionEffect.applyToEntities(ARCHETYPE, affectedEntities);
        }
    }

    @Environment(EnvType.CLIENT)
    private void handleExplosionEffects(Vec3d center, List<ExplosionEffectModifier> effectModifiers, double blastRadius) {
        var random = world.getRandom();
        var types = new ArrayList<>(effectModifiers.stream().map(mod -> mod.getParticleEffects(ARCHETYPE)).filter(Optional::isPresent).map(Optional::get).toList());
        types.add(SpectrumParticleTypes.PRIMORDIAL_SMOKE);

        for (int i = 0; i < 30; i++) {
            world.addImportantParticle(SpectrumParticleTypes.PRIMORDIAL_FLAME, true, center.getX(), center.getY(), center.getZ(), random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25);
        }

        var particles = blastRadius * blastRadius + random.nextInt((int) (blastRadius * 2)) * (types.size() / 2 + 0.5);
        for (int i = 0; i < particles; i++) {
            var r = random.nextDouble() * blastRadius;
            var orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
            var particle = orientation.toVector(r).add(center);
            Collections.shuffle(types);
            world.addParticle(types.get(0), particle.getX(), particle.getY(), particle.getZ(), 0, 0, 0);
        }
    }

    public void arm(@NotNull World world, BlockPos pos) {
        world.setBlockState(pos, getCachedState().with(ThreatConfluxBlock.ARMED, true));
        world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.BLOCKS, 2F, 0.1F + world.getRandom().nextFloat() * 0.3F);
        markDirty();
    }

    public void tryDetonate(BlockState state) {
        if (!state.isOf(SpectrumBlocks.THREAT_CONFLUX) || !state.get(ThreatConfluxBlock.ARMED))
            return;

        if (detonationTicks < 0) {
            detonationTicks = 15;
            world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.INCANDESCENT_CHARGE, SoundCategory.BLOCKS, 1, 2F);
            markDirty();
        }
    }

    public void parseStack(ItemStack stack) {
        var effects = ExplosionEffectModifier.decodeStack(stack);
        effects.ifPresent(modifiers -> explosionEffects = modifiers);
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("ticksUntilArmed", ticksUntilArmed);
        nbt.putInt("detonationTicks", detonationTicks);
        ExplosionEffectModifier.encode(nbt, explosionEffects);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        ticksUntilArmed = nbt.getInt("ticksUntilArmed");
        detonationTicks = nbt.getInt("detonationTicks");
        explosionEffects = ExplosionEffectModifier.decode(nbt);
    }
}
