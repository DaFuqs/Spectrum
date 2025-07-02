package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

@Environment(EnvType.CLIENT)
public class EnderSpliceChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final Player player;
	private boolean done;
	
	public EnderSpliceChargingSoundInstance(Player player) {
		super(SpectrumSoundEvents.ENDER_SPLICE_CHARGES, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
		this.looping = true;
		this.delay = 0;
		this.volume = 0.6F;
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}
	
	@Override
	public boolean isStopped() {
		return this.done;
	}
	
	@Override
	public boolean canStartSilent() {
		return true;
	}
	
	@Override
	public void tick() {
		if (player == null || player.getUseItemRemainingTicks() <= 0 || player.getTicksUsingItem() > 47 || !player.getUseItem().is(SpectrumItems.ENDER_SPLICE)) {
			this.setDone();
		} else {
			this.x = this.player.getX();
			this.y = this.player.getY();
			this.z = this.player.getZ();
			
			showParticles();
		}
	}
	
	private void showParticles() {
		Level world = player.getCommandSenderWorld();
		Vec3 pos = player.position();
		RandomSource random = world.random;
		
		for (int i = 0; i < 10; i++) {
			player.getCommandSenderWorld().addParticle(
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
		this.looping = false;
	}
}
