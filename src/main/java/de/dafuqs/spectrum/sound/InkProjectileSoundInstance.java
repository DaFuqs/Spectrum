package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.entity.entity.InkProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class InkProjectileSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	RegistryKey<World> worldKey;
	InkProjectileEntity inkProjectile;
	int maxDurationTicks;
	
	private int ticksPlayed = 0;
	private float distance = 0.0F;
	private boolean done;
	
	protected InkProjectileSoundInstance(SoundEvent soundEvent, RegistryKey<World> worldKey, InkProjectileEntity inkProjectile) {
		super(soundEvent, SoundCategory.NEUTRAL);
		
		this.worldKey = worldKey;
		this.inkProjectile = inkProjectile;
		
		this.repeat = false;
		this.repeatDelay = 0;
		this.maxDurationTicks = 280;
		this.volume = 1.0F;
	}
	
	public static void startSoundInstance(SoundEvent soundEvent, InkProjectileEntity inkProjectile) {
		InkProjectileSoundInstance newInstance = new InkProjectileSoundInstance(soundEvent, MinecraftClient.getInstance().world.getRegistryKey(), inkProjectile);
		SpectrumClient.minecraftClient.getSoundManager().play(newInstance);
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
		this.distance = MathHelper.clamp(this.distance + 0.001F, 0.0F, 1.0F);
		this.volume = Math.max(0.0F, 1.0F - inkProjectile.getBlockPos().getManhattanDistance(MinecraftClient.getInstance().player.getBlockPos()) / 64F);
		
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
	}
	
}