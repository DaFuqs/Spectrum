package de.dafuqs.spectrum.blocks.gemstone;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public class SpectrumGemstoneBlock extends AmethystBlock {

	public static final MapCodec<SpectrumGemstoneBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			propertiesCodec(),
			SoundEvent.DIRECT_CODEC.fieldOf("hit_sound_event").forGetter(b -> b.hitSoundEvent),
			SoundEvent.DIRECT_CODEC.fieldOf("chime_sound_event").forGetter(b -> b.chimeSoundEvent)
	).apply(i, SpectrumGemstoneBlock::new));

	private final SoundEvent hitSoundEvent;
	private final SoundEvent chimeSoundEvent;
	
	public SpectrumGemstoneBlock(Properties settings, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent) {
		super(settings);
		this.hitSoundEvent = hitSoundEvent;
		this.chimeSoundEvent = chimeSoundEvent;
	}

	@Override
	public MapCodec<? extends SpectrumGemstoneBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!world.isClientSide) {
			BlockPos blockPos = hit.getBlockPos();
			world.playSound(null, blockPos, hitSoundEvent, SoundSource.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
			world.playSound(null, blockPos, chimeSoundEvent, SoundSource.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
		}
	}
	
}
