package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.entity.entity.InkProjectileEntity;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class InkProjectileSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final RegistryKey<World> worldKey;
	private final InkProjectileEntity inkProjectile;
	private final int maxDurationTicks = 280;
	
	private int ticksPlayed = 0;
	private boolean done;
	private boolean playedExplosion;
	
	protected InkProjectileSoundInstance(RegistryKey<World> worldKey, InkProjectileEntity inkProjectile) {
		super(SpectrumSoundEvents.INK_PROJECTILE_LAUNCH, SoundCategory.NEUTRAL, SoundInstance.createRandom());
		
		this.worldKey = worldKey;
		this.inkProjectile = inkProjectile;
		
		this.attenuationType = AttenuationType.NONE;
		this.x = this.inkProjectile.getX();
		this.y = this.inkProjectile.getY();
		this.z = this.inkProjectile.getZ();
		
		this.repeat = false;
		this.repeatDelay = 0;
		this.volume = 1.0F;
	}
	
	@Environment(EnvType.CLIENT)
	public static void startSoundInstance(InkProjectileEntity inkProjectile) {
		InkProjectileSoundInstance newInstance = new InkProjectileSoundInstance(MinecraftClient.getInstance().world.getRegistryKey(), inkProjectile);
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
		
		this.x = this.inkProjectile.getX();
		this.y = this.inkProjectile.getY();
		this.z = this.inkProjectile.getZ();
		
		this.volume = Math.max(0.0F, 0.7F - Math.max(0.0F, inkProjectile.getBlockPos().getManhattanDistance(MinecraftClient.getInstance().player.getBlockPos()) / 128F - 0.2F));
		
		if (ticksPlayed > maxDurationTicks
				|| !Objects.equals(this.worldKey, MinecraftClient.getInstance().world.getRegistryKey())
				|| inkProjectile.isRemoved()) {
			
			this.setDone();
		}
	}
	
	protected final void setDone() {
		this.ticksPlayed = this.maxDurationTicks;
		this.done = true;
		this.repeat = false;
		
		if(inkProjectile.isRemoved() && !playedExplosion) {
			MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, Math.max(0.1F, this.volume / 4), 0.9F + MinecraftClient.getInstance().world.random.nextFloat() * 0.2F);
			spawnImpactParticles(this.inkProjectile);
			playedExplosion = true;
		}
	}
	
	private void spawnImpactParticles(InkProjectileEntity inkProjectile) {
		DyeColor dyeColor = inkProjectile.getDyeColor();
		World world = inkProjectile.getWorld();
		Vec3d targetPos = inkProjectile.getPos();
		Vec3d velocity = inkProjectile.getVelocity();
		
		world.addParticle(SpectrumParticleTypes.getExplosionParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);
		for (int i = 0; i < 10; i++) {
			world.addParticle(SpectrumParticleTypes.getCraftingParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, -velocity.x * 3, -velocity.y * 3, -velocity.z * 3);
		}
	}
	
}