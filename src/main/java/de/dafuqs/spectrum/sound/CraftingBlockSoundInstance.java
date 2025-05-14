package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CraftingBlockSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private static List<CraftingBlockSoundInstance> playingSoundInstances = new ArrayList<>();
	
	final ResourceKey<Level> worldKey;
	final BlockPos sourceBlockPos;
	final Block sourceBlock;
	final int maxDurationTicks;
	
	private int ticksPlayed = 0;
	private boolean done;
	
	protected CraftingBlockSoundInstance(SoundEvent soundEvent, ResourceKey<Level> worldKey, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		super(soundEvent, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
		
		this.worldKey = worldKey;
		this.sourceBlockPos = sourceBlockPos;
		this.sourceBlock = sourceBlock;
		this.maxDurationTicks = maxDurationTicks;
		
		this.looping = true;
		this.delay = 0;
		
		this.x = sourceBlockPos.getX() + 0.5;
		this.y = sourceBlockPos.getY() + 0.5;
		this.z = sourceBlockPos.getZ() + 0.5;
		
		updateVolume();
	}
	
	@Environment(EnvType.CLIENT)
    public static void startSoundInstance(SoundEvent soundEvent, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		Minecraft client = Minecraft.getInstance();
		stopPlayingOnPos(sourceBlockPos);
		
		CraftingBlockSoundInstance newInstance = new CraftingBlockSoundInstance(soundEvent, client.level.dimension(), sourceBlockPos, sourceBlock, maxDurationTicks);
		playingSoundInstances.add(newInstance);
		Minecraft.getInstance().getSoundManager().play(newInstance);
	}
	
	// if there is already a sound instance playing at given pos: cancel it
	public static void stopPlayingOnPos(BlockPos blockPos) {
		List<CraftingBlockSoundInstance> newInstances = new ArrayList<>();
		for (CraftingBlockSoundInstance soundInstance : playingSoundInstances) {
			if (soundInstance.sourceBlockPos.equals(blockPos)) {
				soundInstance.setDone();
			} else {
				newInstances.add(soundInstance);
			}
		}
		playingSoundInstances = newInstances;
	}
	
	@Override
	public boolean isStopped() {
		return this.done;
	}
	
	@Override
	public boolean canStartSilent() {
		return true;
	}
	
	private void updateVolume() {
		Minecraft client = Minecraft.getInstance();
		this.volume = Math.max(0, 0.75F * (SpectrumCommon.CONFIG.BlockSoundVolume - sourceBlockPos.distManhattan(client.player.blockPosition()) / 64F));
	}
	
	@Override
	public void tick() {
		this.ticksPlayed++;
		updateVolume();
		if (this.ticksPlayed == maxDurationTicks) {
			this.volume /= 2; // ease out
		}
		
		if (ticksPlayed > maxDurationTicks
				|| !Objects.equals(this.worldKey, Minecraft.getInstance().level.dimension())
				|| shouldStopPlaying()) {
			
			playingSoundInstances.remove(this);
			this.setDone();
		}
	}
	
	private boolean shouldStopPlaying() {
		Minecraft client = Minecraft.getInstance();
		BlockState blockState = client.level.getBlockState(sourceBlockPos);
		return !blockState.getBlock().equals(sourceBlock);
	}
	
	protected final void setDone() {
		this.ticksPlayed = this.maxDurationTicks;
		this.done = true;
		this.looping = false;
	}
	
}
