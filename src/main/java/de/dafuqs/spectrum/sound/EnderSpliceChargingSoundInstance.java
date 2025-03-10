package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

@Environment(EnvType.CLIENT)
public class EnderSpliceChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private boolean done;
	
	public EnderSpliceChargingSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.ENDER_SPLICE_CHARGES, SoundCategory.NEUTRAL, SoundInstance.createRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.6F;
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
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
		if (player == null || player.getItemUseTimeLeft() <= 0 || player.getItemUseTime() > 47 || !player.getActiveItem().isOf(SpectrumItems.ENDER_SPLICE)) {
			this.setDone();
		} else {
			this.x = this.player.getX();
			this.y = this.player.getY();
			this.z = this.player.getZ();
			
			showParticles();
		}
	}
	
	private void showParticles() {
		World world = player.getEntityWorld();
		Vec3d pos = player.getPos();
		Random random = world.random;
		
		for (int i = 0; i < 10; i++) {
			player.getEntityWorld().addParticle(
					ParticleTypes.PORTAL,
					pos.x,
					pos.y + 1,
					pos.z,
					random.nextDouble() * 1.6D - 0.8D,
					random.nextDouble() * 1.6D - 0.8D,
					random.nextDouble() * 1.6D - 0.8D);
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}
