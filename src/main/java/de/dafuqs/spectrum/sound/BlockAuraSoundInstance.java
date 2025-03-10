package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BlockAuraSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	public static final List<BlockAuraSoundInstance> INSTANCES = new ArrayList<>();
	private static final int MAX_DISTANCE = 48;
	private static final int SPACING = 16;
	private static final float MIN_VOLUME = 0.01F;
	private static final float MAX_VOLUME = 1.0F;
	private static final float VOLUME_EASING_STEPS = 0.01F;
	
	private final static List<BlockPos> toRemove = new ArrayList<>();
	
	private int age = 0;
	private final Queue<BlockPos> sources = new LinkedList<>();
	private final World world;
	
	private BlockAuraSoundInstance(SoundEvent sound, World world, BlockPos source) {
		super(sound, SoundCategory.AMBIENT, SoundInstance.createRandom());
		this.volume = MIN_VOLUME;
		this.repeat = true;
		this.repeatDelay = 0;
		this.relative = false;
		this.world = world;
		this.sources.add(source);
		
		updatePositionAndCount();
	}
	
	@Override
	public void tick() {
		age++;
		if (age % 100 == 0) {
			sources.poll();
		}
		if (age % 10 == 0) {
			updatePositionAndCount();
		}
		
		float targetVolume = (float) MathHelper.clamp(sources.size() * 0.05 - 0.5, MIN_VOLUME, MAX_VOLUME);
		if (this.volume < targetVolume) {
			this.volume += VOLUME_EASING_STEPS;
		} else if (this.volume > targetVolume) {
			this.volume -= VOLUME_EASING_STEPS;
		}
		
		double cameraEntityEyeY = MinecraftClient.getInstance().getCameraEntity().getEyeY();
		var pitchMod = MathHelper.clamp((Math.abs(cameraEntityEyeY - this.y) - 2F) / 64F, 0, 0.334F);
		if (cameraEntityEyeY < this.y) {
			pitchMod *= -1;
		}
		this.pitch = (float) (1 + pitchMod);
		
		if (volume > 0.25) {
			Vec3d pos = new Vec3d(this.x, this.y, this.z);
			float chance = volume / 2F;
			ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, Support.getIntFromDecimalWithChance(chance, random), true, new Vec3d(24, 8, 24), -8, true, pos, new Vec3d(0, 0.04D + random.nextDouble() * 0.06, 0));
			ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_MOTE_SMALL, Support.getIntFromDecimalWithChance(chance, random), false, new Vec3d(16, 8, 16), -6, false, pos, Vec3d.ZERO);
			ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_MOTE, Support.getIntFromDecimalWithChance(chance, random), true, new Vec3d(16, 6, 16), -4, false, pos, Vec3d.ZERO);
		}
	}
	
	private void updatePositionAndCount() {
		int x = 0;
		int y = 0;
		int z = 0;
		for (BlockPos source : sources) {
			if (!world.isChunkLoaded(source) || !world.getBlockState(source).isIn(SpectrumBlockTags.AZURITE_ORES)) { // tag is hardcoded for now. But should we have more blocks like that, we can easily split it
				toRemove.add(source);
			} else {
				x += source.getX();
				y += source.getY();
				z += source.getZ();
			}
		}
		for (BlockPos source : toRemove) {
			sources.remove(source);
		}
		toRemove.clear();
		
		int count = sources.size();
		if (count > 0) {
			this.x = (double) x / count;
			this.y = (double) y / count;
			this.z = (double) z / count;
		}
	}
	
	@Override
	public boolean isDone() {
		boolean done;
		
		if (volume <= 0) {
			done = true;
		} else {
			Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
			done = cameraEntity == null || cameraEntity.getPos().squaredDistanceTo(x, y, z) > MAX_DISTANCE * MAX_DISTANCE;
		}
		
		if (done) {
			INSTANCES.remove(this);
		}
		return done;
	}
	
	public static void addToExistingInstanceOrCreateNewOne(World world, BlockPos pos) {
		double nearestDistance = Double.MAX_VALUE;
		@Nullable BlockAuraSoundInstance nearest = null;
		for (BlockAuraSoundInstance instance : INSTANCES) {
			double squaredDistance = pos.getSquaredDistance(instance.x, instance.y, instance.z);
			if (squaredDistance < nearestDistance) {
				nearestDistance = squaredDistance;
				nearest = instance;
			}
		}
		
		if (nearestDistance < SPACING * SPACING * SPACING) {
			if (nearest.sources.contains(pos)) {
				return;
			}
			nearest.sources.add(pos.toImmutable());
		} else {
			BlockAuraSoundInstance newInstance = new BlockAuraSoundInstance(SpectrumSoundEvents.CRYSTAL_AURA, world, pos.toImmutable());
			INSTANCES.add(newInstance);
			MinecraftClient.getInstance().getSoundManager().play(newInstance);
		}
	}
	
	public static void clear() {
		INSTANCES.forEach(i -> i.volume = Integer.MIN_VALUE);
		INSTANCES.clear();
	}
}