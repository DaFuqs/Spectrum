package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class MonstrositySoundInstance extends MovingSoundInstance {
	
	private final SpectrumBossEntity bossEntity;
	
	private MonstrositySoundInstance(SpectrumBossEntity bossEntity) {
		super(SpectrumSoundEvents.BOSS_THEME, SoundCategory.RECORDS, SoundInstance.createRandom());
		this.bossEntity = bossEntity;
		this.repeat = true;
	}
	
	public static void startSoundInstance(SpectrumBossEntity bossEntity) {
		MinecraftClient.getInstance().getSoundManager().play(new MonstrositySoundInstance(bossEntity));
	}
	
	@Override
	public void tick() {
		if (!bossEntity.isAlive() && !bossEntity.isRemoved()) {
			this.setDone();
		} else {
			this.x = bossEntity.getX();
			this.y = bossEntity.getY();
			this.z = bossEntity.getZ();
		}
	}
	
}