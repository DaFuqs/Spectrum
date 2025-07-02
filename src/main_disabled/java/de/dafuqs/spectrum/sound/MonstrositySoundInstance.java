package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;

@Environment(EnvType.CLIENT)
public class MonstrositySoundInstance extends AbstractTickableSoundInstance {
	
	private static int instances = 0;
	private final SpectrumBossEntity bossEntity;
	
	private MonstrositySoundInstance(SpectrumBossEntity bossEntity) {
		super(SpectrumSoundEvents.MUSIC_CREDITS, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
		this.bossEntity = bossEntity;
		this.looping = true;
		instances++;
	}
	
	public static void startSoundInstance(SpectrumBossEntity bossEntity) {
		Minecraft.getInstance().getSoundManager().play(new MonstrositySoundInstance(bossEntity));
	}
	
	@Override
	public void tick() {
		Minecraft client = Minecraft.getInstance();
		if (instances > 1 || (!bossEntity.isAlive() || bossEntity.isRemoved())) {
			instances--;
			this.stop();
			return;
		}
		
		Player player = client.player;
		if (player != null) {
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}
	}
	
}