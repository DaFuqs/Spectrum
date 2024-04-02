package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CraftingBlockSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private static List<CraftingBlockSoundInstance> playingSoundInstances = new ArrayList<>();
	
	final RegistryKey<World> worldKey;
	final BlockPos sourceBlockPos;
	final Block sourceBlock;
	final int maxDurationTicks;
	
	private int ticksPlayed = 0;
	private boolean done;
	
	protected CraftingBlockSoundInstance(SoundEvent soundEvent, RegistryKey<World> worldKey, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		super(soundEvent, SoundCategory.BLOCKS, SoundInstance.createRandom());
		
		this.worldKey = worldKey;
		this.sourceBlockPos = sourceBlockPos;
		this.sourceBlock = sourceBlock;
		this.maxDurationTicks = maxDurationTicks;
		
		this.repeat = true;
		this.repeatDelay = 0;
		
		this.x = sourceBlockPos.getX() + 0.5;
		this.y = sourceBlockPos.getY() + 0.5;
		this.z = sourceBlockPos.getZ() + 0.5;
		
		updateVolume();
	}
	
	@Environment(EnvType.CLIENT)
    public static void startSoundInstance(SoundEvent soundEvent, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		MinecraftClient client = MinecraftClient.getInstance();
		stopPlayingOnPos(sourceBlockPos);
		
		CraftingBlockSoundInstance newInstance = new CraftingBlockSoundInstance(soundEvent, client.world.getRegistryKey(), sourceBlockPos, sourceBlock, maxDurationTicks);
		playingSoundInstances.add(newInstance);
		MinecraftClient.getInstance().getSoundManager().play(newInstance);
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
	public boolean isDone() {
		return this.done;
	}
	
	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}
	
	private void updateVolume() {
		MinecraftClient client = MinecraftClient.getInstance();
		this.volume = Math.max(0, 0.75F * (SpectrumCommon.CONFIG.BlockSoundVolume - sourceBlockPos.getManhattanDistance(client.player.getBlockPos()) / 64F));
	}
	
	@Override
	public void tick() {
		this.ticksPlayed++;
		updateVolume();
		if (this.ticksPlayed == maxDurationTicks) {
			this.volume /= 2; // ease out
		}
		
		if (ticksPlayed > maxDurationTicks
				|| !Objects.equals(this.worldKey, MinecraftClient.getInstance().world.getRegistryKey())
				|| shouldStopPlaying()) {
			
			playingSoundInstances.remove(this);
			this.setDone();
		}
	}
	
	private boolean shouldStopPlaying() {
		MinecraftClient client = MinecraftClient.getInstance();
		BlockState blockState = client.world.getBlockState(sourceBlockPos);
		return !blockState.getBlock().equals(sourceBlock);
	}
	
	protected final void setDone() {
		this.ticksPlayed = this.maxDurationTicks;
		this.done = true;
		this.repeat = false;
	}
	
}
