package de.dafuqs.spectrum.blocks.gemstone;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpectrumGemstoneBlock extends AmethystBlock {
	
	private final SoundEvent hitSoundEvent;
	private final SoundEvent chimeSoundEvent;
	
	public SpectrumGemstoneBlock(Settings settings, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent) {
		super(settings);
		this.hitSoundEvent = hitSoundEvent;
		this.chimeSoundEvent = chimeSoundEvent;
	}
	
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (!world.isClient) {
			BlockPos blockPos = hit.getBlockPos();
			world.playSound(null, blockPos, hitSoundEvent, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
			world.playSound(null, blockPos, chimeSoundEvent, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
		}
	}
	
}
