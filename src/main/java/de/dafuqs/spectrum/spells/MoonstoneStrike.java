package de.dafuqs.spectrum.spells;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.hit.HitResult.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.RaycastContext.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MoonstoneStrike {

	private final World world;
	private final double x;
	private final double y;
    private final double z;
    public final @Nullable Entity entity;
    public final float power;
    public final float knockbackMod;
    private final DamageSource damageSource;
	protected final Map<PlayerEntity, Vec3d> affectedPlayers;

    public MoonstoneStrike(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power, float knockbackMod) {
        this.affectedPlayers = Maps.newHashMap();
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.knockbackMod = knockbackMod;
        this.x = x;
        this.y = y;
        this.z = z;
        this.damageSource = damageSource == null ? SpectrumDamageSources.moonstoneBlast(world, this) : damageSource;
    }

    public static void create(World world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power) {
        create(world, entity, damageSource, x, y, z, power, power);
    }

    public static void create(World world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power, float knockbackMod) {
        MoonstoneStrike moonstoneStrike = new MoonstoneStrike(world, entity, damageSource, x, y, z, power, knockbackMod);

        if (world.isClient) {
            world.playSound(x, y, z, SpectrumSoundEvents.MOONSTONE_STRIKE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
            world.addParticle(SpectrumParticleTypes.MOONSTONE_STRIKE, x, y, z, 1.0, 0.0, 0.0);
        } else {
            moonstoneStrike.damageAndKnockbackEntities();
            SpectrumS2CPacketSender.sendMoonstoneBlast((ServerWorld) world, moonstoneStrike);
            moonstoneStrike.affectWorld();
        }
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public float getPower() {
        return power;
    }

    public float getKnockbackMod() {
        return knockbackMod;
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
        return this.affectedPlayers;
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (!(d < 0.0) && !(e < 0.0) && !(f < 0.0)) {
            int i = 0;
            int j = 0;

            for(double k = 0.0; k <= 1.0; k += d) {
                for(double l = 0.0; l <= 1.0; l += e) {
                    for(double m = 0.0; m <= 1.0; m += f) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (entity.world.raycast(new RaycastContext(vec3d, source, ShapeType.COLLIDER, FluidHandling.NONE, entity)).getType() == Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    public void damageAndKnockbackEntities() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        
        float power2 = this.power * 2.0F;
        int minX = MathHelper.floor(this.x - (double) power2 - 1.0);
        int maxX = MathHelper.floor(this.x + (double) power2 + 1.0);
        int minY = MathHelper.floor(this.y - (double) power2 - 1.0);
        int maxY = MathHelper.floor(this.y + (double) power2 + 1.0);
        int minZ = MathHelper.floor(this.z - (double) power2 - 1.0);
        int maxZ = MathHelper.floor(this.z + (double) power2 + 1.0);
        Vec3d center = new Vec3d(this.x, this.y, this.z);

        for (Entity entity : this.world.getOtherEntities(this.entity, new Box(minX, minY, minZ, maxX, maxY, maxZ))) {
            if (!entity.isImmuneToExplosion()) {
                double w = Math.sqrt(entity.squaredDistanceTo(center)) / (double) power2;
                if (w <= 1.0) {
                    double difX = entity.getX() - this.x;
                    double difY = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double difZ = entity.getZ() - this.z;
                    double sqrt = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
                    if (sqrt != 0.0) {
                        difX /= sqrt;
                        difY /= sqrt;
                        difZ /= sqrt;
                        double exposure = getExposure(center, entity);
                        double ac = (1.0 - w) * exposure;
                        entity.damage(this.getDamageSource(), (float) ((int) ((ac * ac + ac) / 2.0 * 7.0 * (double) power2 + 1.0)));
                        double knockback = ac * this.knockbackMod;
                        if (entity instanceof LivingEntity) {
                            knockback = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ac);
                        }

                        entity.setVelocity(entity.getVelocity().add(difX * knockback, difY * knockback, difZ * knockback));
                        if (entity instanceof PlayerEntity playerEntity) {
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, new Vec3d(difX * ac, difY * ac, difZ * ac));
                            }
                        }
                    }
                }
            }
        }
    }

    public void affectWorld() {
        LivingEntity cause = getCausingEntity();
        int range = (int) this.power / 2;
        for (BlockPos pos : BlockPos.iterateOutwards(new BlockPos(this.x, this.y, this.z), range, range, range)) {
            BlockState blockState = this.world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (block instanceof MoonstoneStrikeableBlock moonstoneStrikeableBlock) {
                moonstoneStrikeableBlock.onMoonstoneStrike(this.world, pos, cause);
            }
        }
    }

    public @Nullable LivingEntity getCausingEntity() {
        if (this.entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else if (this.entity instanceof ProjectileEntity projectileEntity && projectileEntity.getOwner() instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }
    
}
