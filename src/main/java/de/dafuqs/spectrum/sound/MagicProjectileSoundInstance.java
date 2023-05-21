package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class MagicProjectileSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    private final RegistryKey<World> worldKey;
    private final MagicProjectileEntity projectile;
    private final int maxDurationTicks = 280;

    private int ticksPlayed = 0;
    private boolean done;
    private boolean playedExplosion;

    protected MagicProjectileSoundInstance(RegistryKey<World> worldKey, MagicProjectileEntity projectile) {
        super(SpectrumSoundEvents.INK_PROJECTILE_LAUNCH, SoundCategory.NEUTRAL, SoundInstance.createRandom());

        this.worldKey = worldKey;
        this.projectile = projectile;

        this.attenuationType = AttenuationType.NONE;
        this.x = this.projectile.getX();
        this.y = this.projectile.getY();
        this.z = this.projectile.getZ();

        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 1.0F;
    }

    @Environment(EnvType.CLIENT)
    public static void startSoundInstance(MagicProjectileEntity inkProjectile) {
        MagicProjectileSoundInstance newInstance = new MagicProjectileSoundInstance(MinecraftClient.getInstance().world.getRegistryKey(), inkProjectile);
        MinecraftClient.getInstance().getSoundManager().play(newInstance);
    }
	
	@Override
	public boolean isDone() {
		return this.done;
	}
	
	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}
	
	@Override
	public void tick() {
        this.ticksPlayed++;

        this.x = this.projectile.getX();
        this.y = this.projectile.getY();
        this.z = this.projectile.getZ();

        this.volume = Math.max(0.0F, 0.7F - Math.max(0.0F, projectile.getBlockPos().getManhattanDistance(MinecraftClient.getInstance().player.getBlockPos()) / 128F - 0.2F));

        if (ticksPlayed > maxDurationTicks
                || !Objects.equals(this.worldKey, MinecraftClient.getInstance().world.getRegistryKey())
                || projectile.isRemoved()) {

            this.setDone();
        }
    }
	
	protected final void setDone() {
		this.ticksPlayed = this.maxDurationTicks;
        this.done = true;
        this.repeat = false;

        if (projectile.isRemoved() && !playedExplosion) {
            MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, Math.max(0.1F, this.volume / 4), 0.9F + MinecraftClient.getInstance().world.random.nextFloat() * 0.2F);
            spawnImpactParticles(this.projectile);
            playedExplosion = true;
        }
    }

    private void spawnImpactParticles(MagicProjectileEntity projectile) {
        DyeColor dyeColor = projectile.getDyeColor();
        World world = projectile.getWorld();
        Vec3d targetPos = projectile.getPos();
        Vec3d velocity = projectile.getVelocity();

        world.addParticle(SpectrumParticleTypes.getExplosionParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);
        for (int i = 0; i < 10; i++) {
            world.addParticle(SpectrumParticleTypes.getCraftingParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, -velocity.x * 3, -velocity.y * 3, -velocity.z * 3);
        }
    }

}