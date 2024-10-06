package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class MonstrositySoundInstance extends MovingSoundInstance {
	
	private static int instances = 0;
	private final SpectrumBossEntity bossEntity;
	
	private MonstrositySoundInstance(SpectrumBossEntity bossEntity) {
		super(SpectrumSoundEvents.MUSIC_CREDITS, SoundCategory.RECORDS, SoundInstance.createRandom());
		this.bossEntity = bossEntity;
		this.repeat = true;
		instances++;
	}
	
	public static void startSoundInstance(SpectrumBossEntity bossEntity) {
		MinecraftClient.getInstance().getSoundManager().play(new MonstrositySoundInstance(bossEntity));
	}
	
	@Override
	public void tick() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (instances > 1 || (!bossEntity.isAlive() || bossEntity.isRemoved())) {
			instances--;
			this.setDone();
			return;
		}
		
		PlayerEntity player = client.player;
		if (player != null) {
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}
	}
	
}