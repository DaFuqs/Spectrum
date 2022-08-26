package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.MovingMinecartSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class CraftingBlockSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private static List<CraftingBlockSoundInstance> playingSoundInstances = new ArrayList<>();
	
	RegistryKey<World> worldKey;
	BlockPos sourceBlockPos;
	Block sourceBlock;
	int maxDurationTicks;
	
	private int ticksPlayed = 0;
	private boolean done;
	
	protected CraftingBlockSoundInstance(SoundEvent soundEvent, RegistryKey<World> worldKey, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		super(soundEvent, SoundCategory.BLOCKS);
		
		this.worldKey = worldKey;
		this.sourceBlockPos = sourceBlockPos;
		this.sourceBlock = sourceBlock;
		this.maxDurationTicks = maxDurationTicks;
		
		this.repeat = true;
		this.repeatDelay = 0;
	}
	
	public static void startSoundInstance(SoundEvent soundEvent, BlockPos sourceBlockPos, Block sourceBlock, int maxDurationTicks) {
		stopPlayingOnPos(sourceBlockPos);
		
		CraftingBlockSoundInstance newInstance = new CraftingBlockSoundInstance(soundEvent, MinecraftClient.getInstance().world.getRegistryKey(), sourceBlockPos, sourceBlock, maxDurationTicks);
		playingSoundInstances.add(newInstance);
		SpectrumClient.minecraftClient.getSoundManager().play(newInstance);
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
	
	@Override
	public void tick() {
		this.ticksPlayed++;
		this.volume = Math.max(0, SpectrumCommon.CONFIG.BlockSoundVolume - sourceBlockPos.getManhattanDistance(MinecraftClient.getInstance().player.getBlockPos()) / 64F);
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
		BlockState blockState = MinecraftClient.getInstance().world.getBlockState(sourceBlockPos);
		return !blockState.getBlock().equals(sourceBlock);
	}
	
	protected final void setDone() {
		this.ticksPlayed = this.maxDurationTicks;
		this.done = true;
		this.repeat = false;
	}
	
}