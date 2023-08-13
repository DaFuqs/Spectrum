package de.dafuqs.spectrum.blocks.threat_conflux;

import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ThreatConfluxBlockEntity extends BlockEntity {

    public static final Archetype ARCHETYPE = Archetype.DEFENSIVE;
    public static final double BASE_EXPLOSION_RADIUS = 5;
    public static final double KILL_ZONE_RADIUS = 0.5;
    public static final float BASE_EXPLOSION_DAMAGE = 20;
    public static final float KILL_ZONE_DAMAGE = 2500;
    
    private List<ExplosionEffectModifier> explosionEffects = new ArrayList<>();

    public ThreatConfluxBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.THREAT_CONFLUX, pos, state);
    }
    
    public void explode(@NotNull ServerWorld world, BlockPos pos) {
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
        world.playSound(null, center.getX(), center.getY(), center.getZ(), SpectrumSoundEvents.BLOCK_THREAT_CONFLUX_EXPLODE, SoundCategory.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.3F);
        
        spawnExplosionParticles(world, center, explosionEffects, blastRadius);
        
        var blastBox = Box.of(center, blastRadius * 2, blastRadius * 2, blastRadius * 2);
        var blocks = BlockPos.stream(blastBox).toList();
        var affectedEntities = world.getOtherEntities(null, blastBox);
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
    
    // the client does not know about the block entities data
    // we have to send it from server => client
    private static void spawnExplosionParticles(ServerWorld world, Vec3d center, List<ExplosionEffectModifier> effectModifiers, double blastRadius) {
        var random = world.getRandom();
        var types = new ArrayList<>(effectModifiers.stream().map(mod -> mod.getParticleEffects(ARCHETYPE)).filter(Optional::isPresent).map(Optional::get).toList());
        types.add(SpectrumParticleTypes.PRIMORDIAL_SMOKE);
        
        world.spawnParticles(SpectrumParticleTypes.PRIMORDIAL_FLAME, center.getX(), center.getY(), center.getZ(), 30, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, 0.0);
        
        double particles = blastRadius * blastRadius + random.nextInt((int) (blastRadius * 2)) * (types.size() / 2F + 0.5);
        for (int i = 0; i < particles; i++) {
            var r = random.nextDouble() * blastRadius;
            var orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
            var particle = orientation.toVector(r).add(center);
            Collections.shuffle(types);
            
            world.spawnParticles(types.get(0), particle.getX(), particle.getY(), particle.getZ(), 1, 0, 0, 0, 0);
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
        ExplosionEffectModifier.encode(nbt, explosionEffects);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        explosionEffects = ExplosionEffectModifier.decode(nbt);
    }
    
}
