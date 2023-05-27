package de.dafuqs.spectrum.spells;

import com.google.common.collect.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
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
    @Nullable
    public Entity entity;
    public float power;
    private final DamageSource damageSource;
    protected final Map<PlayerEntity, Vec3d> affectedPlayers;

    public MoonstoneStrike(World world, @Nullable Entity entity, double x, double y, double z, float power) {
        this(world, entity, null, x, y, z, power);
    }

    public MoonstoneStrike(World world, @Nullable Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power) {
        this.affectedPlayers = Maps.newHashMap();
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.x = x;
        this.y = y;
        this.z = z;
        this.damageSource = damageSource == null ? SpectrumDamageSources.moonstoneBlast(world, this) : damageSource;
    }

    public static MoonstoneStrike create(World world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power) {
        MoonstoneStrike moonstoneStrike = new MoonstoneStrike(world, entity, damageSource, x, y, z, power);

        if (world.isClient) {
            world.playSound(x, y, z, SpectrumSoundEvents.MOONSTONE_STRIKE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
            world.addParticle(SpectrumParticleTypes.MOONSTONE_STRIKE, x, y, z, 1.0, 0.0, 0.0);
        } else {
            moonstoneStrike.damageEntities();
            SpectrumS2CPacketSender.sendMoonstoneBlast((ServerWorld) world, moonstoneStrike);
        }

        return moonstoneStrike;
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

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public void damageEntities() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        
        float q = this.power * 2.0F;
        int k = MathHelper.floor(this.x - (double)q - 1.0);
        int l = MathHelper.floor(this.x + (double)q + 1.0);
        int r = MathHelper.floor(this.y - (double)q - 1.0);
        int s = MathHelper.floor(this.y + (double)q + 1.0);
        int t = MathHelper.floor(this.z - (double)q - 1.0);
        int u = MathHelper.floor(this.z + (double)q + 1.0);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, r, t, l, s, u));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
    
        for (Entity entity : list) {
            if (!entity.isImmuneToExplosion()) {
                double w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) q;
                if (w <= 1.0) {
                    double x = entity.getX() - this.x;
                    double y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double z = entity.getZ() - this.z;
                    double aa = Math.sqrt(x * x + y * y + z * z);
                    if (aa != 0.0) {
                        x /= aa;
                        y /= aa;
                        z /= aa;
                        double ab = getExposure(vec3d, entity);
                        double ac = (1.0 - w) * ab;
                        entity.damage(this.getDamageSource(), (float) ((int) ((ac * ac + ac) / 2.0 * 7.0 * (double) q + 1.0)));
                        double ad = ac;
                        if (entity instanceof LivingEntity) {
                            ad = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ac);
                        }
                    
                        entity.setVelocity(entity.getVelocity().add(x * ad, y * ad, z * ad));
                        if (entity instanceof PlayerEntity playerEntity) {
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, new Vec3d(x * ac, y * ac, z * ac));
                            }
                        }
                    }
                }
            }
        }
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
        return this.affectedPlayers;
    }

    @Nullable
    public LivingEntity getCausingEntity() {
        if (this.entity == null) {
            return null;
        } else if (this.entity instanceof TntEntity) {
            return ((TntEntity)this.entity).getOwner();
        } else if (this.entity instanceof LivingEntity) {
            return (LivingEntity)this.entity;
        } else {
            if (this.entity instanceof ProjectileEntity) {
                Entity entity = ((ProjectileEntity)this.entity).getOwner();
                if (entity instanceof LivingEntity) {
                    return (LivingEntity)entity;
                }
            }

            return null;
        }
    }
    
}
