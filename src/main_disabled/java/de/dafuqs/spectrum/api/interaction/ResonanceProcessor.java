package de.dafuqs.spectrum.api.interaction;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public abstract class ResonanceProcessor {
	
	public static boolean preventNextXPDrop;
	
	public static final Codec<ResonanceProcessor> CODEC = SpectrumRegistries.RESONANCE_PROCESSOR_TYPE.byNameCodec()
			.dispatch(ResonanceProcessor::getCodec, codec -> codec);
	
	public BrokenBlockPredicate blockPredicate;
	
	public ResonanceProcessor(BrokenBlockPredicate blockPredicate) {
		this.blockPredicate = blockPredicate;
	}
	
	public abstract boolean process(BlockState state, BlockEntity blockEntity, List<ItemStack> droppedStacks);
	
	public static void applyResonance(RegistryAccess drm, BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		drm.registryOrThrow(SpectrumRegistryKeys.RESONANCE_PROCESSOR).forEach(entry -> entry.process(minedState, blockEntity, droppedStacks));
	}
	
	public abstract MapCodec<? extends ResonanceProcessor> getCodec();
	
}
